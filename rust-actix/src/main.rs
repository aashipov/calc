use std::thread;

use actix_web::{get, post, App, HttpResponse, HttpServer, Responder};

const WELCOME: &'static str = "Welcome to calc service\nHTTP POST your expression / (via meval)";

#[get("{tails:.*}")]
async fn index() -> impl Responder {
    HttpResponse::Ok().body(WELCOME)
}

#[post("{tails:.*}")]
async fn eval(expr: String) -> impl Responder {
    let eval = meval::eval_str(expr);
    match eval {
        Ok(result) => HttpResponse::Ok().body((result).to_string()),
        Err(err) => HttpResponse::BadRequest().body(err.to_string()),
    }
}

#[actix_web::main]
async fn main() -> std::io::Result<()> {
    let ip = "0.0.0.0:8080";
    let num_workers = std::cmp::max(1, thread::available_parallelism()?.get()) * 8;
    let server = HttpServer::new(|| App::new().service(index).service(eval))
        .workers(num_workers)
        .bind(ip)?;
    return server.run().await;
}
