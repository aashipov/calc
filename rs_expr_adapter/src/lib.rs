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

    const NAN: &'static str = "NaN";
    const SIMPLE_EXPRESSION: &'static str = "2+2";
    const SIMPLE_EXPRESSION_RESULT: &'static str = "4";
    const COMPLEX_EXPRESSION: &'static str =
        "(-abs(pi*2-e-(32-4)/(23+4/5)-(2-4)*(4+6-98.2)+4))+1.9e2";
    const COMPLEX_EXPRESSION_RESULT: &'static str = "19.988432890485228";

    macro_rules! test_lib_inner {
        ($name:ident, $f:expr, $expr:expr, $expected:expr) => {
            #[test]
            fn $name() {
                let expr_cstring = CString::new($expr.to_string()).expect("CString::new failed");
                let ptr = expr_cstring.as_ptr();
                let actual = ($f)(ptr);
                assert_eq!(actual.to_string(), $expected);
            }
        };
    }

    test_lib_inner!(
        via_meval_simple,
        crate::via_meval,
        SIMPLE_EXPRESSION,
        SIMPLE_EXPRESSION_RESULT
    );

    test_lib_inner!(
        via_meval_complex,
        crate::via_meval,
        COMPLEX_EXPRESSION,
        COMPLEX_EXPRESSION_RESULT
    );

    test_lib_inner!(via_meval_invalid, crate::via_meval, NAN, NAN);

    test_lib_inner!(
        via_exprtk_simple,
        crate::via_exprtk,
        SIMPLE_EXPRESSION,
        SIMPLE_EXPRESSION_RESULT
    );

    test_lib_inner!(
        via_exprtk_complex,
        crate::via_exprtk,
        COMPLEX_EXPRESSION,
        COMPLEX_EXPRESSION_RESULT
    );

    test_lib_inner!(via_exprtk_invalid, crate::via_exprtk, NAN, NAN);
}
