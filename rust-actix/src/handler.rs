use std::convert::Infallible;

use actix_web::{HttpResponse, HttpResponseBuilder, get, http::StatusCode, post};
use calc_actix::{via_exprtk, via_meval};

pub const WELCOME: &'static str = "Welcome to calc service\nHTTP POST your expression /";

fn text_response(body: String) -> Result<HttpResponse, Infallible> {
    return Ok(HttpResponseBuilder::new(StatusCode::OK)
        .content_type("text/plain")
        .body(body));
}

#[utoipa::path(
    path = "/",
    responses(
        (status = 200, description = "OK")
    )
)]
#[get("{tails:.*}")]
async fn respond_welcome() -> Result<HttpResponse, Infallible> {
    return text_response(WELCOME.to_owned());
}

#[utoipa::path(
    path = "/",
    request_body = String,
    responses(
        (status = 200, description = "OK")
    )
)]
#[post("{tails:.*}")]
async fn respond_via_meval(expr: String) -> Result<HttpResponse, Infallible> {
    let result = via_meval(expr);
    return text_response(result);
}

#[utoipa::path(
    path = "/exprtk",
    request_body = String,
    responses(
        (status = 200, description = "OK")
    )
)]
#[post("/exprtk")]
async fn respond_via_exprkt(expr: String) -> Result<HttpResponse, Infallible> {
    let result = via_exprtk(expr);
    return text_response(result);
}
