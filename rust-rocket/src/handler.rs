use rocket::response::content::RawText;
use calc_rocket::{via_exprtk, via_meval};

pub const WELCOME: &'static str = "Welcome to calc service\nHTTP POST your expression /";

fn text_response(body: String) -> RawText<String> {
    return RawText(body);
}

#[utoipa::path(
    path = "/",
    responses(
        (status = 200, description = "OK")
    )
)]
#[get("/<_..>")]
pub fn respond_welcome() -> RawText<String> {
    return text_response(WELCOME.to_owned());
}

#[utoipa::path(
    path = "/",
    request_body = String,
    responses(
        (status = 200, description = "OK")
    )
)]
#[post("/<_..>", data = "<expr>")]
pub fn respond_via_meval(expr: String) -> RawText<String> {
    return text_response(via_meval(expr));
}

#[utoipa::path(
    path = "/exprtk",
    request_body = String,
    responses(
        (status = 200, description = "OK")
    )
)]
#[post("/exprtk", data = "<expr>")]
pub fn respond_via_exprkt(expr: String) -> RawText<String> {
    return text_response(via_exprtk(expr));
}
