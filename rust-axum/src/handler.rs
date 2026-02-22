#![allow(non_upper_case_globals)]
#![allow(non_camel_case_types)]
#![allow(non_snake_case)]
include!(concat!(env!("OUT_DIR"), "/bindings.rs"));

use std::ffi::{CString, c_char};

use axum::{
    body::Body,
    http::{StatusCode, header::CONTENT_TYPE},
    response::Response,
};

const WELCOME: &'static str = "Welcome to calc service\nHTTP POST your expression /";

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
pub async fn welcome() -> Response {
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
pub async fn via_meval(expr: String) -> Response {
    let eval = meval::eval_str(expr);
    match eval {
        Ok(result) => text_response(result.to_string()),
        Err(err) => text_response(err.to_string()),
    }
}

#[utoipa::path(
    post,
    path = "/exprtk",
    request_body = String,
    responses(
        (status = 200, description = "OK")
    )
)]
pub async fn via_exprkt(expr: String) -> Response {
    let expr_c_string = CString::new(expr).expect("CString::new failed");
    let expr_c_ptr: *const c_char = expr_c_string.as_ptr();
    unsafe {
        let result = calculate(expr_c_ptr);
        return text_response(result.to_string());
    }
}
