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
    use ntex::web::test;

    #[derive(Clone)]
    struct CalcTestConfig {
        name: String,
        request_method: String,
        url: String,
        expression: String,
        expected: String,
    }

    async fn test_calc_inner(tt: &CalcTestConfig) -> Result<(), String> {
        let mut req = test::TestRequest::post()
            .set_payload(tt.expression.clone())
            .uri(tt.url.clone().as_str())
            .to_request();
        if tt.request_method.eq("GET") {
            req = test::TestRequest::get()
                .uri(tt.url.clone().as_str())
                .to_request();
        };
        let app = test::init_service(App::new().configure(|cfg| config(cfg))).await;
        let resp = app.call(req).await.unwrap();
        let body_bytes = test::read_body(resp).await;
        let actual = std::str::from_utf8(&body_bytes).unwrap();
        if tt.expected.clone() != actual {
            return Err(format!(
                "{} with {}; expected {}, actual {}",
                tt.name.clone(),
                tt.expression.clone(),
                tt.expected.clone(),
                actual
            ));
        }
        Ok(())
    }

    #[ntex::test]
    async fn test_calc() -> Result<(), String> {
        let test_cfgs = [
            CalcTestConfig {
                name: String::from("welcome"),
                request_method: String::from("GET"),
                url: String::from("/"),
                expression: String::from(""),
                expected: String::from(crate::handler::WELCOME),
            },
            CalcTestConfig {
                name: String::from("mevalSimpleExpression"),
                request_method: String::from("POST"),
                url: String::from("/"),
                expression: String::from(calc_ntex::SIMPLE_EXPRESSION),
                expected: String::from(calc_ntex::SIMPLE_EXPRESSION_RESULT),
            },
            CalcTestConfig {
                name: String::from("mevalComplexExpression"),
                request_method: String::from("POST"),
                url: String::from("/"),
                expression: String::from(calc_ntex::COMPLEX_EXPRESSION),
                expected: String::from(calc_ntex::COMPLEX_EXPRESSION_RESULT),
            },
            CalcTestConfig {
                name: String::from("mevalInvalidExpression"),
                request_method: String::from("POST"),
                url: String::from("/"),
                expression: String::from(calc_ntex::NAN),
                expected: String::from(calc_ntex::NAN),
            },
            CalcTestConfig {
                name: String::from("exprtkSimpleExpression"),
                request_method: String::from("POST"),
                url: String::from(format!("/{}", calc_ntex::EXPRTK)),
                expression: String::from(calc_ntex::SIMPLE_EXPRESSION),
                expected: String::from(calc_ntex::SIMPLE_EXPRESSION_RESULT),
            },
            CalcTestConfig {
                name: String::from("exprtkComplexExpression"),
                request_method: String::from("POST"),
                url: String::from(format!("/{}", calc_ntex::EXPRTK)),
                expression: String::from(calc_ntex::COMPLEX_EXPRESSION),
                expected: String::from(calc_ntex::COMPLEX_EXPRESSION_RESULT),
            },
            CalcTestConfig {
                name: String::from("exprtkInvalidExpression"),
                request_method: String::from("POST"),
                url: String::from(format!("/{}", calc_ntex::EXPRTK)),
                expression: String::from(calc_ntex::NAN),
                expected: String::from(calc_ntex::NAN),
            },
        ];
        for tt in test_cfgs.iter() {
            test_calc_inner(tt).await?;
        }
        Ok(())
    }
}
