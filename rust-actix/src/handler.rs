use actix_web::{HttpResponse, get, post};
use calc_actix::{via_exprtk, via_meval, welcome};

fn text_response(body: String) -> HttpResponse {
    return HttpResponse::Ok().content_type("text/plain").body(body);
}

#[utoipa::path(
    path = "/",
    responses(
        (status = 200, description = "OK")
    )
)]
#[get("{tails:.*}")]
async fn respond_welcome() -> HttpResponse {
    return text_response(welcome());
}

#[utoipa::path(
    path = "/",
    request_body = String,
    responses(
        (status = 200, description = "OK")
    )
)]
#[post("{tails:.*}")]
async fn respond_via_meval(expr: String) -> HttpResponse {
    return text_response(via_meval(expr));
}

#[utoipa::path(
    path = "/exprtk",
    request_body = String,
    responses(
        (status = 200, description = "OK")
    )
)]
#[post("/exprtk")]
async fn respond_via_exprkt(expr: String) -> HttpResponse {
    return text_response(via_exprtk(expr));
}
