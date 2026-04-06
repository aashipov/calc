mod api_docs;
mod handler;
use std::thread;

use axum::{
    Router,
    routing::{get, post},
};
use utoipa::OpenApi;
use utoipa_swagger_ui::SwaggerUi;

fn app() -> Router {
    return Router::new()
        .merge(
            SwaggerUi::new("/openapi-ui")
                .url("/api-docs/openapi.json", api_docs::ApiDoc::openapi()),
        )
        .route("/", get(handler::respond_welcome))
        .route("/{*any}", get(handler::respond_welcome))
        .route("/", post(|expr| handler::respond_via_meval(expr)))
        .route("/{*any}", post(|expr| handler::respond_via_meval(expr)))
        .route("/exprtk", post(|expr| handler::respond_via_exprkt(expr)));
}

fn main() -> std::io::Result<()> {
    let num_workers = std::cmp::max(2, thread::available_parallelism()?.get());
    let rt = tokio::runtime::Builder::new_multi_thread()
        .worker_threads(num_workers)
        .enable_all()
        .build()?;
    rt.block_on(async {
        let app = app();
        let listener = tokio::net::TcpListener::bind("0.0.0.0:8080").await.unwrap();
        println!("listening on {}", listener.local_addr().unwrap());
        axum::serve(listener, app).await.unwrap();
    });
    return Ok(());
}

#[cfg(test)]
mod tests {

    fn build_server() -> axum_test::TestServer {
        axum_test::TestServer::new(crate::app())
    }

    macro_rules! test_calc_inner {
        ($name:ident, $request_method:expr, $url:expr, $expression:expr, $expected:expr) => {
            #[tokio::test]
            async fn $name() -> Result<(), String> {
                let server = build_server();
                let mut response = server.post($url).text($expression).await;
                if $request_method.eq("GET") {
                    response = server.get($url).await;
                };
                let actual = response.text();
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
        calc_axum::SIMPLE_EXPRESSION,
        calc_axum::SIMPLE_EXPRESSION_RESULT
    );

    test_calc_inner!(
        meval_complex,
        "POST",
        "/",
        calc_axum::COMPLEX_EXPRESSION,
        calc_axum::COMPLEX_EXPRESSION_RESULT
    );

    test_calc_inner!(meval_invalid, "POST", "/", calc_axum::NAN, calc_axum::NAN);

    test_calc_inner!(
        exprtk_simple,
        "POST",
        format!("/{}", calc_axum::EXPRTK).as_str(),
        calc_axum::SIMPLE_EXPRESSION,
        calc_axum::SIMPLE_EXPRESSION_RESULT
    );

    test_calc_inner!(
        exprtk_complex,
        "POST",
        format!("/{}", calc_axum::EXPRTK).as_str(),
        calc_axum::COMPLEX_EXPRESSION,
        calc_axum::COMPLEX_EXPRESSION_RESULT
    );

    test_calc_inner!(
        exprtk_invalid,
        "POST",
        format!("/{}", calc_axum::EXPRTK).as_str(),
        calc_axum::NAN,
        calc_axum::NAN
    );
}
