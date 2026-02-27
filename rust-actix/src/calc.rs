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
    let num_workers = std::cmp::max(1, thread::available_parallelism()?.get()) * 8;
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
    use actix_web::{
        Error,
        dev::{Service, ServiceResponse},
        http, test,
    };

    use super::*;

    const EXPRESSION: &'static str = "(-abs(pi*2-e-(32-4)/(23+4/5)-(2-4)*(4+6-98.2)+4))+1.9e2";
    const EXPRESSION_RESULT_MEVAL: &'static str = "19.988432890485228";
    const NOT_AN_EXPRESSION: &'static str = "NaN";

    async fn check_response(resp: ServiceResponse, expected_body: String) -> Result<(), Error> {
        assert_eq!(resp.status(), http::StatusCode::OK);
        let body_bytes = actix_web::body::to_bytes(resp.into_body()).await.unwrap();
        let body_str = std::str::from_utf8(&body_bytes).unwrap();
        assert_eq!(body_str, expected_body);
        Ok(())
    }

    #[actix_web::test]
    async fn test_welcome() -> Result<(), Error> {
        let app = test::init_service(App::new().configure(|cfg| config(cfg))).await;

        let req = test::TestRequest::get().uri("/").to_request();
        let resp = app.call(req).await?;

        return check_response(resp, calc_actix::WELCOME.to_string()).await;
    }

    #[actix_web::test]
    async fn test_via_meval_expr() -> Result<(), Error> {
        let app = test::init_service(App::new().configure(|cfg| config(cfg))).await;

        let req = test::TestRequest::post()
            .set_payload(EXPRESSION)
            .uri("/")
            .to_request();
        let resp = app.call(req).await?;

        return check_response(resp, EXPRESSION_RESULT_MEVAL.to_string()).await;
    }

    #[actix_web::test]
    async fn test_via_meval_not_an_expr() -> Result<(), Error> {
        let app = test::init_service(App::new().configure(|cfg| config(cfg))).await;

        let req = test::TestRequest::post()
            .set_payload(NOT_AN_EXPRESSION)
            .uri("/")
            .to_request();
        let resp = app.call(req).await?;

        return check_response(
            resp,
            "Evaluation error: unknown variable `NaN`.".to_string(),
        )
        .await;
    }

    #[actix_web::test]
    async fn test_via_exprtk_expr() -> Result<(), Error> {
        let app = test::init_service(App::new().configure(|cfg| config(cfg))).await;

        let req = test::TestRequest::post()
            .set_payload(EXPRESSION)
            .uri("/exprtk")
            .to_request();
        let resp = app.call(req).await?;

        return check_response(resp, EXPRESSION_RESULT_MEVAL.to_string()).await;
    }

    #[actix_web::test]
    async fn test_via_exprtk_not_an_expr() -> Result<(), Error> {
        let app = test::init_service(App::new().configure(|cfg| config(cfg))).await;

        let req = test::TestRequest::post()
            .set_payload(NOT_AN_EXPRESSION)
            .uri("/exprtk")
            .to_request();
        let resp = app.call(req).await?;

        return check_response(resp, NOT_AN_EXPRESSION.to_string()).await;
    }
}
