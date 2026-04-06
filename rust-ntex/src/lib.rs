#![allow(non_upper_case_globals)]
#![allow(non_camel_case_types)]
#![allow(non_snake_case)]
include!(concat!(env!("OUT_DIR"), "/bindings.rs"));

pub const EXPRTK: &'static str = "exprtk";
pub const NAN: &'static str = "NaN";
pub const SIMPLE_EXPRESSION: &'static str = "2+2";
pub const SIMPLE_EXPRESSION_RESULT: &'static str = "4";
pub const COMPLEX_EXPRESSION: &'static str =
    "(-abs(pi*2-e-(32-4)/(23+4/5)-(2-4)*(4+6-98.2)+4))+1.9e2";
pub const COMPLEX_EXPRESSION_RESULT: &'static str = "19.988432890485228";

pub fn via_meval(expr: String) -> String {
    let eval = meval::eval_str(expr);
    match eval {
        Ok(result) => return result.to_string(),
        Err(_) => return NAN.to_string(),
    }
}

pub fn via_exprtk(expr: String) -> String {
    let expr_c_string = std::ffi::CString::new(expr).expect("CString::new failed");
    let expr_c_ptr: *const std::ffi::c_char = expr_c_string.as_ptr();
    unsafe {
        let result = calculate(expr_c_ptr);
        return result.to_string();
    }
}

#[cfg(test)]
mod tests {

    macro_rules! test_lib_inner {
        ($name:ident, $f:expr, $expr:expr, $expected:expr) => {
            #[test]
            fn $name() {
                let actual = ($f)($expr.to_string());
                assert_eq!(actual, $expected);
            }
        };
    }

    test_lib_inner!(
        meval_simple,
        crate::via_meval,
        crate::SIMPLE_EXPRESSION,
        crate::SIMPLE_EXPRESSION_RESULT
    );

    test_lib_inner!(
        meval_complex,
        crate::via_meval,
        crate::COMPLEX_EXPRESSION,
        crate::COMPLEX_EXPRESSION_RESULT
    );

    test_lib_inner!(meval_invalid, crate::via_meval, crate::NAN, crate::NAN);

    test_lib_inner!(
        exprtk_simple,
        crate::via_exprtk,
        crate::SIMPLE_EXPRESSION,
        crate::SIMPLE_EXPRESSION_RESULT
    );

    test_lib_inner!(
        exprtk_complex,
        crate::via_exprtk,
        crate::COMPLEX_EXPRESSION,
        crate::COMPLEX_EXPRESSION_RESULT
    );

    test_lib_inner!(exprtk_invalid, crate::via_exprtk, crate::NAN, crate::NAN);
}
