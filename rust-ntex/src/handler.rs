use calc_ntex::{via_exprtk, via_meval};

use ntex::web::{HttpResponse, get, post};

pub const WELCOME: &'static str = "Welcome to calc service\nHTTP POST your expression /";

fn text_response(body: String) -> HttpResponse {
    return HttpResponse::Ok().content_type("text/plain").body(body);
}

#[get("{tails:.*}")]
async fn respond_welcome() -> HttpResponse {
    return text_response(WELCOME.to_owned());
}

#[post("{tails:.*}")]
async fn respond_via_meval(expr: String) -> HttpResponse {
    let result = via_meval(expr);
    return text_response(result);
}

#[post("/exprtk")]
async fn respond_via_exprkt(expr: String) -> HttpResponse {
    let result = via_exprtk(expr);
    return text_response(result);
}
