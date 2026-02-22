#![allow(non_upper_case_globals)]
#![allow(non_camel_case_types)]
#![allow(non_snake_case)]
include!(concat!(env!("OUT_DIR"), "/bindings.rs"));

use std::ffi::{CString, c_char};

use ntex::web::{HttpResponse, get, post};

const WELCOME: &'static str = "Welcome to calc service\nHTTP POST your expression /";

fn text_response(body: String) -> HttpResponse {
    return HttpResponse::Ok().content_type("text/plain").body(body);
}

#[get("{tails:.*}")]
async fn index() -> HttpResponse {
    return text_response(WELCOME.to_owned());
}

#[post("{tails:.*}")]
async fn via_meval(expr: String) -> HttpResponse {
    let eval = meval::eval_str(expr);
    match eval {
        Ok(result) => text_response((result).to_string()),
        Err(err) => text_response(err.to_string()),
    }
}

#[post("/exprtk")]
async fn via_exprkt(expr: String) -> HttpResponse {
    let expr_c_string = CString::new(expr).expect("CString::new failed");
    let expr_c_ptr: *const c_char = expr_c_string.as_ptr();
    unsafe {
        let result = calculate(expr_c_ptr);
        return text_response((result).to_string());
    }
}
