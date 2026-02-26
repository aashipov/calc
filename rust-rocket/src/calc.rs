#[macro_use]
extern crate rocket;
mod api_docs;
mod handler;

use crate::api_docs::ApiDoc;
use rocket::{Config, figment::Figment};
use utoipa::OpenApi;
use utoipa_swagger_ui::SwaggerUi;

fn config() -> Figment {
    return Config::figment()
        .merge(("address", "0.0.0.0"))
        .merge(("port", 8080))
        .merge(("log_level", "critical"));
}

#[rocket::main]
async fn main() -> Result<(), rocket::Error> {
    let _rocket = rocket::build()
        .configure(config())
        .mount(
            "/",
            SwaggerUi::new("/openapi-ui/<_..>").url("/api-docs/openapi.json", ApiDoc::openapi()),
        )
        .mount(
            "/",
            routes![
                handler::respond_welcome,
                handler::respond_via_meval,
                handler::respond_via_exprkt
            ],
        )
        .launch()
        .await?;
    Ok(())
}
