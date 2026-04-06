use calc_axum::{via_exprtk, via_meval};

use axum::{
    body::Body,
    http::{StatusCode, header::CONTENT_TYPE},
    response::Response,
};

pub const WELCOME: &'static str = "Welcome to calc service\nHTTP POST your expression /";

fn text_response(body: String) -> Response {
    return Response::builder()
        .status(StatusCode::OK)
        .header(CONTENT_TYPE, "text/plain")
        .body(Body::from(body))
        .unwrap();
}

#[utoipa::path(
    get,
    path = "/",
    responses(
        (status = 200, description = "OK")
    )
)]
pub async fn respond_welcome() -> Response {
    return text_response(WELCOME.to_owned());
}

#[utoipa::path(
    post,
    path = "/",
    request_body = String,
    responses(
        (status = 200, description = "OK")
    )
)]
pub async fn respond_via_meval(expr: String) -> Response {
    let result = via_meval(expr);
    return text_response(result);
}

#[utoipa::path(
    post,
    path = "/exprtk",
    request_body = String,
    responses(
        (status = 200, description = "OK")
    )
)]
pub async fn respond_via_exprkt(expr: String) -> Response {
    let result = via_exprtk(expr);
    return text_response(result);
}
