pub const WELCOME: &'static str = "Welcome to calc service\nHTTP POST your expression /";
const MAX_BODY_LENGTH: usize = 10240;

fn body_to_string(request: &mut tiny_http::Request) -> String {
    let mut body = String::new();
    let read_result = request.as_reader().read_to_string(&mut body);
    match read_result {
        Ok(_ok) => (),
        Err(_e) => return calc_tiny_http::NAN.to_owned(),
    }
    match body.parse() {
        Ok(b) => b,
        Err(_e) => calc_tiny_http::NAN.to_owned(),
    }
}

fn str_response(request: tiny_http::Request, response_str: &str) -> Result<(), std::io::Error> {
    request.respond(tiny_http::Response::from_string(response_str))
}

pub fn handler(server: std::sync::Arc<tiny_http::Server>) {
    for mut request in server.incoming_requests() {
        if let &tiny_http::Method::Post = request.method() {
            let mut response_text = calc_tiny_http::NAN.to_owned();
            if let Some(body_length) = request.body_length() {
                if body_length <= MAX_BODY_LENGTH {
                    let body = body_to_string(&mut request);
                    if body != calc_tiny_http::NAN {
                        if request.url().contains(calc_tiny_http::EXPRTK) {
                            response_text = calc_tiny_http::via_exprtk(body);
                        } else {
                            response_text = calc_tiny_http::via_meval(body);
                        }
                    }
                }
            }
            str_response(request, &response_text).ok();
        } else {
            str_response(request, WELCOME).ok();
        }
    }
}
