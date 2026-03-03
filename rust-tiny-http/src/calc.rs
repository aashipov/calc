extern crate tiny_http;
use std::thread;
use tiny_http::Server;
mod handler;

const SOCKET: &'static str = "0.0.0.0:8080";

fn main() {
    let server = std::sync::Arc::new(Server::http(SOCKET).unwrap());
    let num_workers = std::cmp::max(1, thread::available_parallelism().unwrap().get()) * 8;
    let mut handles = Vec::with_capacity(num_workers);
    for _ in 0..num_workers {
        let server_copy = server.clone();
        handles.push(std::thread::spawn(move || handler::handler(server_copy)));
    }
    for h in handles {
        h.join().unwrap();
    }
}

#[cfg(test)]
mod tests {
    use crate::{
        SOCKET,
        handler::{self, EXPRTK, NAN},
    };
    use calc_tiny_http::WELCOME;
    use std::sync::Once;
    use tiny_http::Server;

    static TEST_SERVER: Once = Once::new();
    const EXPRESSION: &'static str = "(-abs(pi*2-e-(32-4)/(23+4/5)-(2-4)*(4+6-98.2)+4))+1.9e2";
    const EXPRESSION_RESULT_MEVAL: &'static str = "19.988432890485228";

    fn set_up() {
        TEST_SERVER.call_once(|| {
            std::thread::spawn(move || {
                handler::handler(std::sync::Arc::new(Server::http(SOCKET).unwrap()));
            });
            std::thread::sleep(std::time::Duration::from_millis(100));
        });
    }

    fn do_post(url: String, expression: &'static str, expected: &'static str) {
        let response_body = ureq::post(url)
            .send(expression)
            .unwrap()
            .body_mut()
            .read_to_string()
            .unwrap();
        assert_eq!(expected, response_body);
    }

    #[test]
    fn test_welcome() {
        set_up();
        let url = format!("http://{SOCKET}");
        let response_body = ureq::get(url)
            .call()
            .unwrap()
            .body_mut()
            .read_to_string()
            .unwrap();
        assert_eq!(WELCOME, response_body);
    }

    #[test]
    fn test_via_meval_expr() {
        set_up();
        let url = format!("http://{SOCKET}");
        do_post(url, EXPRESSION, EXPRESSION_RESULT_MEVAL);
    }

    #[test]
    fn test_via_meval_not_an_expr() {
        set_up();
        let url = format!("http://{SOCKET}");
        do_post(url, NAN, NAN);
    }

    #[test]
    fn test_via_exprtk_expr() {
        set_up();
        let url = format!("http://{SOCKET}/{EXPRTK}");
        do_post(url, EXPRESSION, EXPRESSION_RESULT_MEVAL);
    }

    #[test]
    fn test_via_exprtk_not_an_expr() {
        set_up();
        let url = format!("http://{SOCKET}/{EXPRTK}");
        do_post(url, NAN, NAN);
    }
}
