use actix_web::{HttpResponse, Responder, get, post};

const WELCOME: &'static str = "Welcome to calc service\nHTTP POST your expression / (via meval)";

static SYMBOL_TABLE: std::sync::OnceLock<exprtk_rs::SymbolTable> = std::sync::OnceLock::new();

#[utoipa::path(
    path = "/",
    responses(
        (status = 200, description = "OK")
    )
)]
#[get("{tails:.*}")]
async fn index() -> impl Responder {
    HttpResponse::Ok().body(WELCOME)
}

#[utoipa::path(
    path = "/mxparser",
    request_body = String,
    responses(
        (status = 200, description = "OK")
    )
)]
#[post("/mxparser")]
async fn via_exprkt_rs(expr: String) -> impl Responder {
    let expression = exprtk_rs::Expression::new(&expr, build_symbol_table());
    match expression {
        Ok(mut result) => HttpResponse::Ok().body((result.value()).to_string()),
        Err(err) => HttpResponse::BadRequest().body(err.to_string()),
    }
}

#[utoipa::path(
    path = "/",
    request_body = String,
    responses(
        (status = 200, description = "OK")
    )
)]
#[post("{tails:.*}")]
async fn via_meval(expr: String) -> impl Responder {
    let eval = meval::eval_str(expr);
    match eval {
        Ok(result) => HttpResponse::Ok().body((result).to_string()),
        Err(err) => HttpResponse::BadRequest().body(err.to_string()),
    }
}

fn build_symbol_table() -> exprtk_rs::SymbolTable {
    match SYMBOL_TABLE.get() {
        None => {
            let mut st = exprtk_rs::SymbolTable::new();
            let _ = st.add_constant("pi", std::f64::consts::PI);
            let _ = st.add_constant("e", std::f64::consts::E);
            return SYMBOL_TABLE.get_or_init(|| st).clone();
        }
        Some(st) => return st.clone(),
    }
}
