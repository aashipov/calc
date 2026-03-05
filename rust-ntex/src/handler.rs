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
    return text_response(via_meval(expr));
}

#[post("/exprtk")]
async fn respond_via_exprkt(expr: String) -> HttpResponse {
    return text_response(via_exprtk(expr));
}
