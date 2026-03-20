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
    use super::*;
    use ntex::{
        http::StatusCode,
        web::{WebResponse, test},
    };

    const EXPRESSION: &'static str = "(-abs(pi*2-e-(32-4)/(23+4/5)-(2-4)*(4+6-98.2)+4))+1.9e2";
    const EXPRESSION_RESULT_MEVAL: &'static str = "19.988432890485228";
    const NOT_AN_EXPRESSION: &'static str = "NaN";

    async fn check_response(resp: WebResponse, expected_body: String) {
        assert_eq!(resp.status(), StatusCode::OK);
        let body_bytes = test::read_body(resp).await;
        let body_str = std::str::from_utf8(&body_bytes).unwrap();
        assert_eq!(body_str, expected_body);
    }

    #[ntex::test]
    async fn test_welcome() {
        let mut app = test::init_service(App::new().configure(|cfg| config(cfg))).await;

        let req = test::TestRequest::get().uri("/").to_request();
        let resp = test::call_service(&mut app, req).await;

        check_response(resp, crate::handler::WELCOME.to_string()).await;
    }

    #[ntex::test]
    async fn test_via_meval_expr() {
        let mut app = test::init_service(App::new().configure(|cfg| config(cfg))).await;

        let req = test::TestRequest::post()
            .set_payload(EXPRESSION)
            .uri("/")
            .to_request();
        let resp = test::call_service(&mut app, req).await;

        return check_response(resp, EXPRESSION_RESULT_MEVAL.to_string()).await;
    }

    #[ntex::test]
    async fn test_via_meval_not_an_expr() {
        let mut app = test::init_service(App::new().configure(|cfg| config(cfg))).await;

        let req = test::TestRequest::post()
            .set_payload(NOT_AN_EXPRESSION)
            .uri("/")
            .to_request();
        let resp = test::call_service(&mut app, req).await;

        return check_response(
            resp,
            "Evaluation error: unknown variable `NaN`.".to_string(),
        )
        .await;
    }

    #[ntex::test]
    async fn test_via_exprtk_expr() {
        let mut app = test::init_service(App::new().configure(|cfg| config(cfg))).await;

        let req = test::TestRequest::post()
            .set_payload(EXPRESSION)
            .uri("/exprtk")
            .to_request();
        let resp = test::call_service(&mut app, req).await;

        return check_response(resp, EXPRESSION_RESULT_MEVAL.to_string()).await;
    }

    #[ntex::test]
    async fn test_via_exprtk_not_an_expr() {
        let mut app = test::init_service(App::new().configure(|cfg| config(cfg))).await;

        let req = test::TestRequest::post()
            .set_payload(NOT_AN_EXPRESSION)
            .uri("/exprtk")
            .to_request();
        let resp = test::call_service(&mut app, req).await;

        return check_response(
            resp,
            "Evaluation error: unknown variable `NaN`.".to_string(),
        )
        .await;
    }
}
