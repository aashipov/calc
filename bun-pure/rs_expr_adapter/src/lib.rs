#![allow(non_upper_case_globals)]
#![allow(non_camel_case_types)]
#![allow(non_snake_case)]
include!(concat!(env!("OUT_DIR"), "/bindings.rs"));

use std::ffi::CStr;

#[unsafe(no_mangle)]
pub extern "C" fn via_meval(expr: *const std::os::raw::c_char) -> std::os::raw::c_double {
    let expr_string: String = unsafe { CStr::from_ptr(expr).to_string_lossy().into_owned() };
    let eval = meval::eval_str(expr_string);
    match eval {
        Ok(result) => return result,
        Err(_err) => f64::NAN,
    }
}

#[unsafe(no_mangle)]
pub extern "C" fn via_exprtk(expr: *const std::os::raw::c_char) -> std::os::raw::c_double {
    unsafe { calculate(expr) }
}

#[cfg(test)]
mod tests {
    use std::ffi::CString;

    use crate::{via_exprtk, via_meval};

    const EXPRESSION: &'static str = "(-abs(pi*2-e-(32-4)/(23+4/5)-(2-4)*(4+6-98.2)+4))+1.9e2";
    const EXPRESSION_RESULT_MEVAL: f64 = 19.988432890485228;
    const NOT_AN_EXPRESSION: &'static str = "NaN";

    #[test]
    fn test_via_meval_expr() {
        let expr = CString::new(EXPRESSION.to_string()).expect("CString::new failed");
        let ptr = expr.as_ptr();
        let result = via_meval(ptr);
        assert_eq!(EXPRESSION_RESULT_MEVAL.to_owned(), result);
    }

    #[test]
    fn test_via_meval_not_an_expr() {
        let expr = CString::new(NOT_AN_EXPRESSION.to_string()).expect("CString::new failed");
        let ptr = expr.as_ptr();
        let result = via_meval(ptr);
        assert!(result.is_nan());
    }

    #[test]
    fn test_via_exprtk_expr() {
        let expr = CString::new(EXPRESSION.to_string()).expect("CString::new failed");
        let ptr = expr.as_ptr();
        let result = via_exprtk(ptr);
        assert_eq!(EXPRESSION_RESULT_MEVAL, result);
    }

    #[test]
    fn test_via_exprtk_not_an_expr() {
        let expr = CString::new(NOT_AN_EXPRESSION.to_string()).expect("CString::new failed");
        let ptr = expr.as_ptr();
        let result = via_exprtk(ptr);
        assert!(result.is_nan());
    }
}
