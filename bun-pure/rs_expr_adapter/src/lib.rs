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
