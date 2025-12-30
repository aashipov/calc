extern crate tiny_http;

use std::thread;

use tiny_http::{Method, Request, Response, Server};

const WELCOME: &'static str = "Welcome to calc service\nHTTP POST your expression / (via meval)";

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

fn handler(server: std::sync::Arc<Server>) {
    for mut request in server.incoming_requests() {
        if let &Method::Post = request.method() {
            let body = read_body_as_string(&mut request);
            let response_text: String;
            match body {
                Ok(body_string) => {
                    let eval = meval::eval_str(body_string);
                    match eval {
                        Ok(eval_result) => response_text = (eval_result).to_string(),
                        Err(eval_error) => response_text = eval_error.to_string(),
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

fn main() {
    let server = std::sync::Arc::new(Server::http("0.0.0.0:8080").unwrap());
    let num_workers = std::cmp::max(1, thread::available_parallelism().unwrap().get()) * 8;
    let mut handles = Vec::with_capacity(num_workers);
    for _ in 0..num_workers {
        let server = server.clone();
        handles.push(std::thread::spawn(move || handler(server)));
    }
    for h in handles {
        h.join().unwrap();
    }
}
