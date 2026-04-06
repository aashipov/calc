mod handler;
use ntex::web::{App, HttpServer, ServiceConfig, middleware};
use std::thread;

const SOCKET: &'static str = "0.0.0.0:8080";

fn config(app: &mut ServiceConfig) {
    app.service(handler::respond_welcome)
        .service(handler::respond_via_exprkt)
        .service(handler::respond_via_meval);
}

#[ntex::main]
async fn main() -> std::io::Result<()> {
    let num_workers = std::cmp::max(2, thread::available_parallelism()?.get());
    let server = HttpServer::new(|| {
        App::new()
            .wrap(middleware::Logger::default())
            .configure(|app| config(app))
    })
    .workers(num_workers)
    .bind(SOCKET)?;
    return server.run().await;
}

#[cfg(test)]
mod tests {
    use ntex::web::test;

    macro_rules! test_calc_inner {
        ($name:ident, $request_method:expr, $url:expr, $expression:expr, $expected:expr) => {
            #[ntex::test]
            async fn $name() -> Result<(), String> {
                let mut req = test::TestRequest::post()
                    .set_payload($expression.clone())
                    .uri($url.clone())
                    .to_request();
                if $request_method.eq("GET") {
                    req = test::TestRequest::get().uri($url.clone()).to_request();
                };
                let app =
                    test::init_service(crate::App::new().configure(|cfg| crate::config(cfg))).await;
                let resp = app.call(req).await.unwrap();
                let body_bytes = test::read_body(resp).await;
                let actual = std::str::from_utf8(&body_bytes).unwrap();
                if $expected.clone() != actual {
                    return Err(format!(
                        "{} with {}; expected {}, actual {}",
                        $url.clone(),
                        $expression.clone(),
                        $expected.clone(),
                        actual
                    ));
                }
                Ok(())
            }
        };
    }

    test_calc_inner!(welcome, "GET", "/", "", crate::handler::WELCOME);

    test_calc_inner!(
        meval_simple,
        "POST",
        "/",
        calc_ntex::SIMPLE_EXPRESSION,
        calc_ntex::SIMPLE_EXPRESSION_RESULT
    );

    test_calc_inner!(
        meval_complex,
        "POST",
        "/",
        calc_ntex::COMPLEX_EXPRESSION,
        calc_ntex::COMPLEX_EXPRESSION_RESULT
    );

    test_calc_inner!(meval_invalid, "POST", "/", calc_ntex::NAN, calc_ntex::NAN);

    test_calc_inner!(
        exprtk_simple,
        "POST",
        format!("/{}", calc_ntex::EXPRTK).as_str(),
        calc_ntex::SIMPLE_EXPRESSION,
        calc_ntex::SIMPLE_EXPRESSION_RESULT
    );

    test_calc_inner!(
        exprtk_complex,
        "POST",
        format!("/{}", calc_ntex::EXPRTK).as_str(),
        calc_ntex::COMPLEX_EXPRESSION,
        calc_ntex::COMPLEX_EXPRESSION_RESULT
    );

    test_calc_inner!(
        exprtk_invalid,
        "POST",
        format!("/{}", calc_ntex::EXPRTK).as_str(),
        calc_ntex::NAN,
        calc_ntex::NAN
    );
}
