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

    #[derive(Clone)]
    struct CalcTestConfig {
        name: String,
        request_method: String,
        url: String,
        expression: String,
        expected: String,
    }

    // https://stackoverflow.com/a/71993139
    fn test_calc_inner(tt: CalcTestConfig) -> Result<(), String> {
        let mut actual = ureq::post(tt.url.clone())
            .send(tt.expression.clone())
            .unwrap()
            .body_mut()
            .read_to_string()
            .unwrap();
        if tt.request_method.eq("GET") {
            actual = ureq::get(tt.url.clone())
                .call()
                .unwrap()
                .body_mut()
                .read_to_string()
                .unwrap();
        };
        if tt.expected == actual {
            Ok(())
        } else {
            Err(format!(
                "{}: {} {} with {}; expected {}, actual {}",
                tt.name,
                tt.request_method,
                tt.url.clone(),
                tt.expression,
                tt.expected,
                actual
            ))
        }
    }

    #[test]
    fn test_calc() -> Result<(), String> {
        set_up();
        let test_cfgs = [
            CalcTestConfig {
                name: String::from("welcome"),
                request_method: String::from("GET"),
                url: format!("http://{SOCKET}"),
                expression: String::from(""),
                expected: String::from(crate::handler::WELCOME),
            },
            CalcTestConfig {
                name: String::from("mevalSimpleExpression"),
                request_method: String::from("POST"),
                url: format!("http://{SOCKET}"),
                expression: String::from(calc_tiny_http::SIMPLE_EXPRESSION),
                expected: String::from(calc_tiny_http::SIMPLE_EXPRESSION_RESULT),
            },
            CalcTestConfig {
                name: String::from("mevalComplexExpression"),
                request_method: String::from("POST"),
                url: format!("http://{SOCKET}"),
                expression: String::from(calc_tiny_http::COMPLEX_EXPRESSION),
                expected: String::from(calc_tiny_http::COMPLEX_EXPRESSION_RESULT),
            },
            CalcTestConfig {
                name: String::from("mevalInvalidExpression"),
                request_method: String::from("POST"),
                url: format!("http://{SOCKET}"),
                expression: String::from(calc_tiny_http::NAN),
                expected: String::from(calc_tiny_http::NAN),
            },
            CalcTestConfig {
                name: String::from("exprtkSimpleExpression"),
                request_method: String::from("POST"),
                url: format!("http://{SOCKET}/{EXPRTK}"),
                expression: String::from(calc_tiny_http::SIMPLE_EXPRESSION),
                expected: String::from(calc_tiny_http::SIMPLE_EXPRESSION_RESULT),
            },
            CalcTestConfig {
                name: String::from("exprtkComplexExpression"),
                request_method: String::from("POST"),
                url: format!("http://{SOCKET}/{EXPRTK}"),
                expression: String::from(calc_tiny_http::COMPLEX_EXPRESSION),
                expected: String::from(calc_tiny_http::COMPLEX_EXPRESSION_RESULT),
            },
            CalcTestConfig {
                name: String::from("exprtkInvalidExpression"),
                request_method: String::from("POST"),
                url: format!("http://{SOCKET}/{EXPRTK}"),
                expression: String::from(calc_tiny_http::NAN),
                expected: String::from(calc_tiny_http::NAN),
            },
        ];
        test_cfgs
            .iter()
            .try_for_each(|tt| test_calc_inner(tt.clone()))?;
        Ok(())
    }
}
