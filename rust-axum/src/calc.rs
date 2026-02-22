mod api_docs;
mod handler;
use axum::{
    Router,
    routing::{get, post},
};
use utoipa::OpenApi;
use utoipa_swagger_ui::SwaggerUi;

#[tokio::main]
async fn main() {
    let app = Router::new()
        .merge(
            SwaggerUi::new("/openapi-ui")
                .url("/api-docs/openapi.json", api_docs::ApiDoc::openapi()),
        )
        .route("/", get(handler::respond_welcome))
        .route("/{*any}", get(handler::respond_welcome))
        .route("/", post(|expr| handler::respond_via_meval(expr)))
        .route("/{*any}", post(|expr| handler::respond_via_meval(expr)))
        .route("/exprtk", post(|expr| handler::respond_via_exprkt(expr)));
    let listener = tokio::net::TcpListener::bind("0.0.0.0:8080").await.unwrap();
    println!("listening on {}", listener.local_addr().unwrap());
    axum::serve(listener, app).await.unwrap();
}
