use wasm_bindgen::prelude::*;

#[wasm_bindgen]
pub fn via_meval(expr: String) -> String {
  let eval = meval::eval_str(expr);
  match eval {
    Ok(result) => return result.to_string(),
    Err(err) => return err.to_string(),
  }
}

#[cfg(test)]
mod tests {
  use crate::via_meval;

  const EXPRESSION: &'static str =
    "(-abs(pi*2-e-(32-4)/(23+4/5)-(2-4)*(4+6-98.2)+4))+1.9e2";
  const EXPRESSION_RESULT_MEVAL: &'static str = "19.988432890485228";
  const NOT_AN_EXPRESSION: &'static str = "NaN";

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
}
