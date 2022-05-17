use std::io::Read;
use rouille::{Request, Response};

const POST: &'static str = "POST";
const WELCOME: &'static str = "Welcome to calc service\nHTTP POST your expression / (via meval)";
const NO_REQUEST_BODY: &'static str = "No request body";
const CANNOT_READ_BODY_TO_STRING: &'static str = "Can not read body to string";

fn str_response(text: &str) -> Response {
    Response::text(text)
}

fn string_response(text: String) -> Response {
    Response::text(text)
}

fn evaluate(request: &Request) -> Response {
    match request.data() {
        None => str_response(NO_REQUEST_BODY),
        Some(mut request_body) => {
            let mut body_string = String::new();
            match request_body.read_to_string(&mut body_string) {
                Ok(_) => {
                    let eval = meval::eval_str(body_string);
                    match eval {
                        Ok(eval_result) => string_response((eval_result).to_string()),
                        Err(eval_error) => string_response((eval_error).to_string())
                    }
                }
                Err(_) => str_response(CANNOT_READ_BODY_TO_STRING)
            }
        }
    }
}

fn main() {
    rouille::start_server("0.0.0.0:8080", move |request: &Request| {
        match request.method() {
            POST => evaluate(request),
            _ => str_response(WELCOME)
        }
    });
}
