use std::env;
use std::path::PathBuf;

fn main() {
    println!("cargo:rustc-link-lib=c-exprtk-adapter");
    println!("cargo:rustc-link-lib=c-meval-adapter");
    let bindings = bindgen::Builder::default()
        .header("c-exprtk-adapter.h")
        .header("c-meval-adapter.h")
        .parse_callbacks(Box::new(bindgen::CargoCallbacks::new()))
        .generate()
        .expect("Unable to generate bindings");
    let out_path = PathBuf::from(env::var("OUT_DIR").unwrap());
    bindings
        .write_to_file(out_path.join("bindings.rs"))
        .expect("Couldn't write bindings!");
}
