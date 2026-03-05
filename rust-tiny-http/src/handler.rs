extern crate tiny_http;
use calc_tiny_http::{via_exprtk, via_meval};
use tiny_http::{Method, Request, Response, Server};

pub const EXPRTK: &'static str = "exprtk";
pub const NAN: &'static str = "NaN";
pub const WELCOME: &'static str = "Welcome to calc service\nHTTP POST your expression /";

fn body_to_string(request: &mut Request) -> String {
    let mut body = String::new();
    let read_result = request.as_reader().read_to_string(&mut body);
    match read_result {
        Ok(_ok) => (),
        Err(_e) => return NAN.to_owned(),
    }
    match body.parse() {
        Ok(b) => b,
        Err(_e) => NAN.to_owned(),
    }
}

fn str_response(request: Request, response_str: &str) -> Result<(), std::io::Error> {
    request.respond(Response::from_string(response_str))
}

pub fn handler(server: std::sync::Arc<Server>) {
    for mut request in server.incoming_requests() {
        if let &Method::Post = request.method() {
            request.url();
            let body = body_to_string(&mut request);
            let mut response_text = body.to_owned();
            if body != NAN {
                if request.url().contains(EXPRTK) {
                    response_text = via_exprtk(body);
                } else {
                    response_text = via_meval(body);
                }
            }
            str_response(request, &response_text).ok();
        } else {
            str_response(request, WELCOME).ok();
        }
    }
}
