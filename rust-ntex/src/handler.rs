#![allow(non_upper_case_globals)]
#![allow(non_camel_case_types)]
#![allow(non_snake_case)]
include!(concat!(env!("OUT_DIR"), "/bindings.rs"));

use std::ffi::{CString, c_char};

use ntex::web::{HttpResponse, Responder, get, post};

const WELCOME: &'static str = "Welcome to calc service\nHTTP POST your expression /";

#[get("{tails:.*}")]
async fn index() -> impl Responder {
    HttpResponse::Ok().body(WELCOME)
}

#[post("/exprtk")]
async fn via_exprkt(expr: String) -> impl Responder {
    let expr_c_string = CString::new(expr).expect("CString::new failed");
    let expr_c_ptr: *const c_char = expr_c_string.as_ptr();
    unsafe {
        let result = calculate(expr_c_ptr);
        return HttpResponse::Ok().body((result).to_string());
    }
}

#[post("{tails:.*}")]
async fn via_meval(expr: String) -> impl Responder {
    let eval = meval::eval_str(expr);
    match eval {
        Ok(result) => HttpResponse::Ok().body((result).to_string()),
        Err(err) => HttpResponse::BadRequest().body(err.to_string()),
    }
}
