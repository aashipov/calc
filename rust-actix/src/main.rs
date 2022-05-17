use actix_web::{middleware, web, App, HttpResponse, HttpServer};

async fn index() -> &'static str {
    "Welcome to calc service\nHTTP POST your expression / (via meval)"
}

fn string_response(txt: String) -> HttpResponse {
    HttpResponse::Ok().content_type("text/plain; charset=utf-8").body(txt)
}

async fn eval(expr: String) -> HttpResponse {
    let eval = meval::eval_str(expr);
    match eval {
        Ok(result) => string_response((result).to_string()),
        Err(err) => string_response(err.to_string()),
    }
}

#[actix_web::main]
async fn main() -> std::io::Result<()> {
    let ip = "0.0.0.0:8080";
    HttpServer::new(|| {
        App::new()
            .wrap(middleware::Logger::default())
            .default_service(
                web::resource("")
                    .route(web::get().to(index))
                    .route(web::post().to(eval)),
            )
    })
    .bind(ip)?
    .run()
    .await
}
