extern crate tiny_http;
use std::thread;
use tiny_http::Server;
mod handler;

fn main() {
    let server = std::sync::Arc::new(Server::http("0.0.0.0:8080").unwrap());
    let num_workers = std::cmp::max(1, thread::available_parallelism().unwrap().get()) * 8;
    let mut handles = Vec::with_capacity(num_workers);
    for _ in 0..num_workers {
        let server = server.clone();
        handles.push(std::thread::spawn(move || handler::handler(server)));
    }
    for h in handles {
        h.join().unwrap();
    }
}
