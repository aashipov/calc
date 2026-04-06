use std::thread;
use tiny_http::Server;
mod handler;

const SOCKET: &'static str = "0.0.0.0:8080";

fn main() -> std::io::Result<()> {
    let server = std::sync::Arc::new(Server::http(SOCKET).unwrap());
    let num_workers = std::cmp::max(2, thread::available_parallelism()?.get());
    let mut handles = Vec::with_capacity(num_workers);
    for _ in 0..num_workers {
        let server_copy = server.clone();
        handles.push(std::thread::spawn(move || handler::handler(server_copy)));
    }
    for h in handles {
        h.join().unwrap();
    }
    return Ok(());
}

#[cfg(test)]
mod tests {
    use crate::SOCKET;
    use calc_tiny_http::EXPRTK;

    static TEST_SERVER: std::sync::Once = std::sync::Once::new();

    fn set_up() {
        TEST_SERVER.call_once(|| {
            std::thread::spawn(move || {
                crate::handler::handler(std::sync::Arc::new(
                    tiny_http::Server::http(SOCKET).unwrap(),
                ));
            });
            std::thread::sleep(std::time::Duration::from_millis(100));
        });
    }

    macro_rules! test_calc_inner {
        ($name:ident, $request_method:expr, $url:expr, $expression:expr, $expected:expr) => {
            #[test]
            fn $name() -> Result<(), String> {
                set_up();
                let mut actual = ureq::post($url.clone())
                    .send($expression.clone())
                    .unwrap()
                    .body_mut()
                    .read_to_string()
                    .unwrap();
                if $request_method.eq("GET") {
                    actual = ureq::get($url.clone())
                        .call()
                        .unwrap()
                        .body_mut()
                        .read_to_string()
                        .unwrap();
                };
                if $expected == actual {
                    Ok(())
                } else {
                    Err(format!(
                        "{} {} with {}; expected {}, actual {}",
                        $request_method,
                        $url.clone(),
                        $expression,
                        $expected,
                        actual
                    ))
                }
            }
        };
    }

    test_calc_inner!(
        welcome,
        "GET",
        format!("http://{SOCKET}"),
        "",
        crate::handler::WELCOME
    );

    test_calc_inner!(
        meval_simple,
        "POST",
        format!("http://{SOCKET}"),
        calc_tiny_http::SIMPLE_EXPRESSION,
        calc_tiny_http::SIMPLE_EXPRESSION_RESULT
    );

    test_calc_inner!(
        meval_complex,
        "POST",
        format!("http://{SOCKET}"),
        calc_tiny_http::COMPLEX_EXPRESSION,
        calc_tiny_http::COMPLEX_EXPRESSION_RESULT
    );

    test_calc_inner!(
        meval_invalid,
        "POST",
        format!("http://{SOCKET}"),
        calc_tiny_http::NAN,
        calc_tiny_http::NAN
    );

    test_calc_inner!(
        exprtk_simple,
        "POST",
        format!("http://{SOCKET}/{EXPRTK}"),
        calc_tiny_http::SIMPLE_EXPRESSION,
        calc_tiny_http::SIMPLE_EXPRESSION_RESULT
    );

    test_calc_inner!(
        exprtk_complex,
        "POST",
        format!("http://{SOCKET}/{EXPRTK}"),
        calc_tiny_http::COMPLEX_EXPRESSION,
        calc_tiny_http::COMPLEX_EXPRESSION_RESULT
    );

    test_calc_inner!(
        exprtk_invalid,
        "POST",
        format!("http://{SOCKET}/{EXPRTK}"),
        calc_tiny_http::NAN,
        calc_tiny_http::NAN
    );
}
