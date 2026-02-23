#![allow(non_upper_case_globals)]
#![allow(non_camel_case_types)]
#![allow(non_snake_case)]
include!(concat!(env!("OUT_DIR"), "/bindings.rs"));

use std::ffi::{CString, c_char};

pub const WELCOME: &'static str = "Welcome to calc service\nHTTP POST your expression /";

pub fn welcome() -> String {
    WELCOME.to_owned()
}

pub fn via_meval(expr: String) -> String {
    let eval = meval::eval_str(expr);
    match eval {
        Ok(result) => return result.to_string(),
        Err(err) => return err.to_string(),
    }
}

pub fn via_exprtk(expr: String) -> String {
    let expr_c_string = CString::new(expr).expect("CString::new failed");
    let expr_c_ptr: *const c_char = expr_c_string.as_ptr();
    unsafe {
        let result = calculate(expr_c_ptr);
        return result.to_string();
    }
}

#[cfg(test)]
mod tests {
    use crate::{WELCOME, via_exprtk, via_meval, welcome};

    const EXPRESSION: &'static str = "(-abs(pi*2-e-(32-4)/(23+4/5)-(2-4)*(4+6-98.2)+4))+1.9e2";
    const EXPRESSION_RESULT_MEVAL: &'static str = "19.988432890485228";
    const NOT_AN_EXPRESSION: &'static str = "NaN";

    #[test]
    fn test_welcome() {
        assert_eq!(WELCOME.to_owned(), welcome());
    }

    #[test]
    fn test_via_meval_expr() {
        let result = via_meval(EXPRESSION.to_owned());
        assert_eq!(EXPRESSION_RESULT_MEVAL.to_owned(), result);
    }

    #[test]
    fn test_via_meval_not_an_expr() {
        let result = via_meval(NOT_AN_EXPRESSION.to_owned());
        assert!(result.to_owned().contains(NOT_AN_EXPRESSION));
    }

    #[test]
    fn test_via_exprtk_expr() {
        let result = via_exprtk(EXPRESSION.to_owned());
        assert_eq!(EXPRESSION_RESULT_MEVAL.to_owned(), result);
    }

    #[test]
    fn test_via_exprtk_not_an_expr() {
        let result = via_exprtk(NOT_AN_EXPRESSION.to_owned());
        assert_eq!(NOT_AN_EXPRESSION.to_owned(), result);
    }
}
