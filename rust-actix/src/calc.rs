mod api_docs;
mod handler;
use std::thread;

use crate::api_docs::ApiDoc;
use actix_web::{App, HttpServer, middleware, web};
use utoipa::OpenApi;
use utoipa_swagger_ui::SwaggerUi;

const SOCKET: &'static str = "0.0.0.0:8080";

fn config(app: &mut web::ServiceConfig) {
    app.service(
        SwaggerUi::new("/openapi-ui/{_:.*}").url("/api-docs/openapi.json", ApiDoc::openapi()),
    )
    .service(handler::respond_welcome)
    .service(handler::respond_via_exprkt)
    .service(handler::respond_via_meval);
}

#[actix_web::main]
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
    use actix_web::{dev::Service, test};

    macro_rules! test_calc_inner {
        ($name:ident, $request_method:expr, $url:expr, $expression:expr, $expected:expr) => {
            #[actix_web::test]
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
                let body_bytes = actix_web::body::to_bytes(resp.into_body()).await.unwrap();
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
        calc_actix::SIMPLE_EXPRESSION,
        calc_actix::SIMPLE_EXPRESSION_RESULT
    );

    test_calc_inner!(
        meval_complex,
        "POST",
        "/",
        calc_actix::COMPLEX_EXPRESSION,
        calc_actix::COMPLEX_EXPRESSION_RESULT
    );

    test_calc_inner!(meval_invalid, "POST", "/", calc_actix::NAN, calc_actix::NAN);

    test_calc_inner!(
        exprtk_simple,
        "POST",
        format!("/{}", calc_actix::EXPRTK).as_str(),
        calc_actix::SIMPLE_EXPRESSION,
        calc_actix::SIMPLE_EXPRESSION_RESULT
    );

    test_calc_inner!(
        exprtk_complex,
        "POST",
        format!("/{}", calc_actix::EXPRTK).as_str(),
        calc_actix::COMPLEX_EXPRESSION,
        calc_actix::COMPLEX_EXPRESSION_RESULT
    );

    test_calc_inner!(
        exprtk_invalid,
        "POST",
        format!("/{}", calc_actix::EXPRTK).as_str(),
        calc_actix::NAN,
        calc_actix::NAN
    );
}
