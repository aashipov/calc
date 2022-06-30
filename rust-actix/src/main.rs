use actix_web::{get, post, App, HttpResponse, HttpServer, Responder};

#[get("{tails:.*}")]
async fn index() -> impl Responder {
    HttpResponse::Ok().body("Welcome to calc service\nHTTP POST your expression / (via meval)")
}

#[post("{tails:.*}")]
async fn eval(expr: String) -> impl Responder {
    let eval = meval::eval_str(expr);
    match eval {
        Ok(result) => HttpResponse::Ok().body((result).to_string()),
        Err(err) => HttpResponse::Ok().body(err.to_string()),
    }
}

#[actix_web::main]
async fn main() -> std::io::Result<()> {
    let ip = "0.0.0.0:8080";
    HttpServer::new(|| App::new().service(index).service(eval))
        .bind(ip)?
        .run()
        .await
}
