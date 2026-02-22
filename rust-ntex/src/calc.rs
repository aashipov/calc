mod handler;
use ntex::web::{App, HttpServer, ServiceConfig, middleware};
use std::thread;

const SOCKET: &'static str = "0.0.0.0:8080";

fn config(app_under_configuration: &mut ServiceConfig) {
    app_under_configuration
        .service(handler::respond_welcome)
        .service(handler::respond_via_exprkt)
        .service(handler::respond_via_meval);
}

#[ntex::main]
async fn main() -> std::io::Result<()> {
    let num_workers = std::cmp::max(1, thread::available_parallelism()?.get()) * 8;
    let server = HttpServer::new(|| {
        App::new()
            .wrap(middleware::Logger::default())
            .configure(|app_under_configuration| config(app_under_configuration))
    })
    .workers(num_workers)
    .bind(SOCKET)?;
    return server.run().await;
}
