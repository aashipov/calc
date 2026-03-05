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

#[launch]
fn rocket() -> _ {
    rocket::build()
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
}

#[cfg(test)]
mod tests {
    use rocket::local::blocking::Client;
    use crate::handler::WELCOME;

    const EXPRESSION: &'static str = "(-abs(pi*2-e-(32-4)/(23+4/5)-(2-4)*(4+6-98.2)+4))+1.9e2";
    const EXPRESSION_RESULT_MEVAL: &'static str = "19.988432890485228";
    const NOT_AN_EXPRESSION: &'static str = "NaN";

    #[test]
    fn test_welcome() {
        let client = Client::tracked(crate::rocket()).unwrap();
        let response = client.get("/").dispatch();
        assert_eq!(response.into_string().unwrap(), WELCOME);
    }

    #[test]
    fn test_via_meval_expr() {
        let client = Client::tracked(crate::rocket()).unwrap();
        let response = client.post("/").body(EXPRESSION).dispatch();
        assert_eq!(response.into_string().unwrap(), EXPRESSION_RESULT_MEVAL);
    }

    #[test]
    fn test_via_meval_not_an_expr() {
        let client = Client::tracked(crate::rocket()).unwrap();
        let response = client.post("/").body(NOT_AN_EXPRESSION).dispatch();
        assert_eq!(
            response.into_string().unwrap(),
            "Evaluation error: unknown variable `NaN`."
        );
    }

    #[test]
    fn test_via_exprtk_expr() {
        let client = Client::tracked(crate::rocket()).unwrap();
        let response = client.post("/exprtk").body(EXPRESSION).dispatch();
        assert_eq!(response.into_string().unwrap(), EXPRESSION_RESULT_MEVAL);
    }

    #[test]
    fn test_via_exprtk_not_an_expr() {
        let client = Client::tracked(crate::rocket()).unwrap();
        let response = client.post("/exprtk").body(NOT_AN_EXPRESSION).dispatch();
        assert_eq!(response.into_string().unwrap(), NOT_AN_EXPRESSION);
    }
}
