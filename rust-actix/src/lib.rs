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

    #[derive(Clone)]
    struct LibTestConfig {
        name: String,
        f: fn(String) -> String,
        expression: String,
        expected: String,
    }

    fn test_lib_inner(tt: LibTestConfig) -> Result<(), String> {
        let actual = (tt.f)(tt.expression.clone());
        if tt.expected.clone() != actual.clone() {
            Err(format!(
                "{} with {}; expected {}, actual {}",
                tt.name.clone(),
                tt.expression.clone(),
                tt.expected.clone(),
                actual.clone()
            ))
        } else {
            Ok(())
        }
    }

    #[test]
    fn test_lib() -> Result<(), String> {
        let test_cfgs = [
            LibTestConfig {
                name: String::from("mevalSimpleExpression"),
                f: crate::via_meval,
                expression: String::from(crate::SIMPLE_EXPRESSION),
                expected: String::from(crate::SIMPLE_EXPRESSION_RESULT),
            },
            LibTestConfig {
                name: String::from("mevalComplexExpression"),
                f: crate::via_meval,
                expression: String::from(crate::COMPLEX_EXPRESSION),
                expected: String::from(crate::COMPLEX_EXPRESSION_RESULT),
            },
            LibTestConfig {
                name: String::from("mevalInvalidExpression"),
                f: crate::via_meval,
                expression: String::from(crate::NAN),
                expected: String::from(crate::NAN),
            },
            LibTestConfig {
                name: String::from("exprtkSimpleExpression"),
                f: crate::via_exprtk,
                expression: String::from(crate::SIMPLE_EXPRESSION),
                expected: String::from(crate::SIMPLE_EXPRESSION_RESULT),
            },
            LibTestConfig {
                name: String::from("exprtkComplexExpression"),
                f: crate::via_exprtk,
                expression: String::from(crate::COMPLEX_EXPRESSION),
                expected: String::from(crate::COMPLEX_EXPRESSION_RESULT),
            },
            LibTestConfig {
                name: String::from("exprtkInvalidExpression"),
                f: crate::via_exprtk,
                expression: String::from(crate::NAN),
                expected: String::from(crate::NAN),
            },
        ];
        test_cfgs
            .iter()
            .try_for_each(|tt| test_lib_inner(tt.clone()))?;
        Ok(())
    }
}
