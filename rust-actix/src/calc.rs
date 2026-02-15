mod api_docs;
mod handler;
use std::thread;

use crate::api_docs::ApiDoc;
use actix_web::{App, HttpServer, middleware, web};
use utoipa::OpenApi;
use utoipa_swagger_ui::SwaggerUi;

const SOCKET: &'static str = "0.0.0.0:8080";

fn config(app_under_configuration: &mut web::ServiceConfig) {
    app_under_configuration
        .service(
            SwaggerUi::new("/openapi-ui/{_:.*}").url("/api-docs/openapi.json", ApiDoc::openapi()),
        )
        .service(handler::index)
        .service(handler::via_exprkt)
        .service(handler::via_meval);
}

#[actix_web::main]
async fn main() -> std::io::Result<()> {
    let num_workers = std::cmp::max(1, thread::available_parallelism()?.get()) * 8;
    let server = HttpServer::new(|| {
        App::new()
            .wrap(middleware::Logger::default())
            .configure(|app_under_configuration| config(app_under_configuration))
    })
    .workers(num_workers)
    .bind(SOCKET)?;
    return server.run().await;
}
