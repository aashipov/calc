import { evaluate, ResultSet } from "mathjs";

export const EXPRTK: string = "exprtk";
export const MEVAL: string = "meval";

const C_EXPRTK_ADAPTER_NAME: string = "libc-exprtk-adapter.so";
const C_MEVAL_ADAPTER_NAME: string = "libc-meval-adapter.so";
const TEXT_ENCODER: TextEncoder = new TextEncoder();

/**
 * Calculate expression via mathjs.
 * @param expr expression
 * @returns result
 */
export const viaMathJs = (expression: string): number => {
  try {
    let result = evaluate(expression);
    if (result != undefined && result != null) {
      if (result.entries) {
        const entries = (result as ResultSet).entries;
        if (entries.length > 0 && entries[0] instanceof Number) {
          return entries[0] as number;
        }
      } else {
        return result as number;
      }
    }
  } catch {}
  return Number.NaN;
};

const C_EXPRTK_ADAPTER = Deno.dlopen(C_EXPRTK_ADAPTER_NAME, {
  calculate_via_exprtk: { parameters: ["buffer"], result: "f64" },
});

/**
 * Calculate expression via exprtk.
 * @param expr expression
 * @returns result
 */
export const viaExprtk = (expr: string): number => {
  try {
    const c_string_buf = TEXT_ENCODER.encode(expr + "\0");
    return C_EXPRTK_ADAPTER.symbols.calculate_via_exprtk(c_string_buf);
  } catch {}
  return Number.NaN;
};

const C_MEVAL_ADAPTER = Deno.dlopen(C_MEVAL_ADAPTER_NAME, {
  calculate_via_meval: { parameters: ["buffer"], result: "f64" },
});

/**
 * Calculate expression via meval.
 * @param expr expression
 * @returns result
 */
export const viaMeval = (expr: string): number => {
  try {
    const c_string_buf = TEXT_ENCODER.encode(expr + "\0");
    return C_MEVAL_ADAPTER.symbols.calculate_via_meval(c_string_buf);
  } catch {}
  return Number.NaN;
};
