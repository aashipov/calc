#[macro_use]
extern crate rocket;
mod api_docs;
mod handler;

use std::thread;

use crate::api_docs::ApiDoc;
use rocket::{Config, figment::Figment};
use utoipa::OpenApi;
use utoipa_swagger_ui::SwaggerUi;

fn config() -> Figment {
    let num_workers = std::cmp::max(2, thread::available_parallelism().unwrap().get());
    return Config::figment()
        .merge(("address", "0.0.0.0"))
        .merge(("port", 8080))
        .merge(("log_level", "critical"))
        .merge(("workers", num_workers));
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

    macro_rules! test_calc_inner {
        ($name:ident, $request_method:expr, $url:expr, $expression:expr, $expected:expr) => {
            #[test]
            fn $name() -> Result<(), String> {
                let client = Client::tracked(crate::rocket()).unwrap();
                let mut response = client.post($url).body($expression).dispatch();
                if $request_method.eq("GET") {
                    response = client.get($url).dispatch();
                };
                let actual = response.into_string().unwrap();
                if $expected == actual {
                    Ok(())
                } else {
                    Err(format!(
                        "{} {} with {}; expected {}, actual {}",
                        $request_method,
                        $url.clone(),
                        $expression,
                        $expected,
                        actual
                    ))
                }
            }
        };
    }

    test_calc_inner!(welcome, "GET", "/", "", crate::handler::WELCOME);

    test_calc_inner!(
        meval_simple,
        "POST",
        "/",
        calc_rocket::SIMPLE_EXPRESSION,
        calc_rocket::SIMPLE_EXPRESSION_RESULT
    );

    test_calc_inner!(
        meval_complex,
        "POST",
        "/",
        calc_rocket::COMPLEX_EXPRESSION,
        calc_rocket::COMPLEX_EXPRESSION_RESULT
    );

    test_calc_inner!(
        meval_invalid,
        "POST",
        "/",
        calc_rocket::NAN,
        calc_rocket::NAN
    );

    test_calc_inner!(
        exprtk_simple,
        "POST",
        format!("/{}", calc_rocket::EXPRTK),
        calc_rocket::SIMPLE_EXPRESSION,
        calc_rocket::SIMPLE_EXPRESSION_RESULT
    );

    test_calc_inner!(
        exprtk_complex,
        "POST",
        format!("/{}", calc_rocket::EXPRTK),
        calc_rocket::COMPLEX_EXPRESSION,
        calc_rocket::COMPLEX_EXPRESSION_RESULT
    );

    test_calc_inner!(
        exprtk_invalid,
        "POST",
        format!("/{}", calc_rocket::EXPRTK),
        calc_rocket::NAN,
        calc_rocket::NAN
    );
}
