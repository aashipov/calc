mod handler;
mod api_docs;
use axum::{
    Router,
    routing::{get, post},
};
use utoipa::OpenApi;
use utoipa_swagger_ui::SwaggerUi;

#[tokio::main]
async fn main() {
    let app = Router::new()
        .merge(SwaggerUi::new("/openapi-ui").url("/api-docs/openapi.json", api_docs::ApiDoc::openapi()))
        .route("/", get(handler::welcome))
        .route("/{*any}", get(handler::welcome))
        .route("/", post(|expr| handler::via_meval(expr)))
        .route("/{*any}", post(|expr| handler::via_meval(expr)))
        .route("/exprtk", post(|expr| handler::via_exprkt(expr)));
    let listener = tokio::net::TcpListener::bind("0.0.0.0:8080").await.unwrap();
    println!("listening on {}", listener.local_addr().unwrap());
    axum::serve(listener, app).await.unwrap();
}
