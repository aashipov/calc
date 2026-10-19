#![allow(non_upper_case_globals)]
#![allow(non_camel_case_types)]
#![allow(non_snake_case)]

include!(concat!(env!("OUT_DIR"), "/bindings.rs"));

pub const EXPRTK: &'static str = "exprtk";
pub const MEVAL: &'static str = "meval";
pub const NAN: &'static str = "NaN";
pub const SIMPLE_EXPRESSION: &'static str = "2+2";
pub const SIMPLE_EXPRESSION_RESULT: &'static str = "4";
pub const COMPLEX_EXPRESSION: &'static str =
    "(-abs(pi*2-e-(32-4)/(23+4/5)-(2-4)*(4+6-98.2)+4))+1.9e2";
pub const COMPLEX_EXPRESSION_RESULT: &'static str = "19.988432890485228";

pub fn via_meval(expr: String) -> String {
    let trimmed = expr.trim();
    if trimmed.is_empty() {
        return NAN.to_owned();
    }
    let Ok(expr_cstring) = std::ffi::CString::new(trimmed) else {
        return NAN.to_owned();
    };
    let ptr = expr_cstring.as_ptr();
    let result = unsafe { calculate_via_meval(ptr) };
    result.to_string()
}

pub fn via_exprtk(expr: String) -> String {
    let trimmed = expr.trim();
    if trimmed.is_empty() {
        return NAN.to_owned();
    }
    let Ok(expr_cstring) = std::ffi::CString::new(trimmed) else {
        return NAN.to_owned();
    };
    let ptr = expr_cstring.as_ptr();
    let result = unsafe { calculate_via_exprtk(ptr) };
    result.to_string()
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
        lib_simple_expression_meval,
        crate::via_meval,
        crate::SIMPLE_EXPRESSION,
        crate::SIMPLE_EXPRESSION_RESULT
    );

    test_lib_inner!(
        lib_complex_expression_meval,
        crate::via_meval,
        crate::COMPLEX_EXPRESSION,
        crate::COMPLEX_EXPRESSION_RESULT
    );

    test_lib_inner!(lib_blank_meval, crate::via_meval, "", crate::NAN);

    test_lib_inner!(
        lib_whitespace_meval,
        crate::via_meval,
        "   \\n\\r\\l\\t       \\n",
        crate::NAN
    );

    test_lib_inner!(
        lib_simple_expression_exprtk,
        crate::via_exprtk,
        crate::SIMPLE_EXPRESSION,
        crate::SIMPLE_EXPRESSION_RESULT
    );

    test_lib_inner!(
        lib_complex_expression_exprtk,
        crate::via_exprtk,
        crate::COMPLEX_EXPRESSION,
        crate::COMPLEX_EXPRESSION_RESULT
    );

    test_lib_inner!(lib_blank_exprtk, crate::via_exprtk, crate::NAN, crate::NAN);

    test_lib_inner!(
        lib_whitespace_exprtk,
        crate::via_exprtk,
        "   \\n\\r\\l\\t       \\n",
        crate::NAN
    );
}
