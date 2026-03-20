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
    use axum_test::TestServer;
    const EXPRESSION: &'static str = "(-abs(pi*2-e-(32-4)/(23+4/5)-(2-4)*(4+6-98.2)+4))+1.9e2";
    const EXPRESSION_RESULT_MEVAL: &'static str = "19.988432890485228";
    const NOT_AN_EXPRESSION: &'static str = "NaN";

    #[tokio::test]
    async fn test_welcome() {
        let app = crate::app();
        let server = TestServer::new(app);
        let response = server.get("/").await;
        response
            .assert_status_ok()
            .assert_text(crate::handler::WELCOME);
    }

    #[tokio::test]
    async fn test_via_meval_expr() {
        let app = crate::app();
        let server = TestServer::new(app);
        let response = server.post("/").text(EXPRESSION).await;
        response
            .assert_status_ok()
            .assert_text(EXPRESSION_RESULT_MEVAL);
    }

    #[tokio::test]
    async fn test_via_meval_not_an_expr() {
        let app = crate::app();
        let server = TestServer::new(app);
        let response = server.post("/").text(NOT_AN_EXPRESSION).await;
        response
            .assert_status_ok()
            .assert_text("Evaluation error: unknown variable `NaN`.");
    }

    #[tokio::test]
    async fn test_via_exprtk_expr() {
        let app = crate::app();
        let server = TestServer::new(app);
        let response = server.post("/exprtk").text(EXPRESSION).await;
        response
            .assert_status_ok()
            .assert_text(EXPRESSION_RESULT_MEVAL);
    }

    #[tokio::test]
    async fn test_via_exprtk_not_an_expr() {
        let app = crate::app();
        let server = TestServer::new(app);
        let response = server.post("/exprtk").text(NOT_AN_EXPRESSION).await;
        response.assert_status_ok().assert_text(NOT_AN_EXPRESSION);
    }
}
