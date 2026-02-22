extern crate tiny_http;
use calc_tiny_http::{WELCOME, via_exprtk, via_meval};
use tiny_http::{Method, Request, Response, Server};

const EXPRTK: &'static str = "exprtk";

fn pass_body_to_string(request: &mut Request, body: &mut String) -> std::io::Result<usize> {
    request.as_reader().read_to_string(body)
}

fn read_body_as_string(request: &mut Request) -> Result<String, std::io::Error> {
    let mut body: String = String::new();
    pass_body_to_string(request, &mut body)
        .map_err(|e| std::io::Error::from(e))
        .unwrap();
    let expr = body.parse();
    match expr {
        Ok(f) => Ok(f),
        Err(e) => Err(std::io::Error::new(std::io::ErrorKind::Other, e)),
    }
}

fn str_response(request: Request, response_str: &str) -> Result<(), std::io::Error> {
    request.respond(Response::from_string(response_str))
}

pub fn handler(server: std::sync::Arc<Server>) {
    for mut request in server.incoming_requests() {
        if let &Method::Post = request.method() {
            request.url();
            let body = read_body_as_string(&mut request);
            let response_text: String;
            match body {
                Ok(body_string) => {
                    if request.url().contains(EXPRTK) {
                        response_text = via_exprtk(body_string);
                    } else {
                        response_text = via_meval(body_string);
                    }
                }
                Err(body_error) => response_text = body_error.to_string(),
            }
            str_response(request, &response_text).ok();
        } else {
            str_response(request, WELCOME).ok();
        }
    }
}
