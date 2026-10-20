import { evaluate, ResultSet } from "mathjs";
import koffi from "koffi";

export const EXPRTK: string = "exprtk";
export const MEVAL: string = "meval";

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

const C_EXPRTK_ADAPTER: koffi.IKoffiLib = koffi.load("libc-exprtk-adapter.so");
const KOFFI_C_EXPRTK_ADAPTER: koffi.KoffiFunction = C_EXPRTK_ADAPTER.func(
  "double calculate_via_exprtk(str)",
);

/**
 * Calculate expression via exprtk.
 * @param expr expression
 * @returns result
 */
export const viaExprtk = (expr: string): number => {
  try {
    let result: unknown = KOFFI_C_EXPRTK_ADAPTER(expr);
    if (result != undefined && result != null) {
      if (Array.isArray(result) && result.length > 0) {
        return result[0];
      } else {
        return result as number;
      }
    }
  } catch {}
  return Number.NaN;
};

const C_MEVAL_ADAPTER: koffi.IKoffiLib = koffi.load("libc-meval-adapter.so");
const KOFFI_C_MEVAL_ADAPTER: koffi.KoffiFunction = C_MEVAL_ADAPTER.func(
  "double calculate_via_meval(str)",
);

/**
 * Calculate expression via meval.
 * @param expr expression
 * @returns result
 */
export const viaMeval = (expr: string): number => {
  try {
    let result: unknown = KOFFI_C_MEVAL_ADAPTER(expr);
    if (result != undefined && result != null) {
      if (Array.isArray(result) && result.length > 0) {
        return result[0];
      } else {
        return result as number;
      }
    }
  } catch {}
  return Number.NaN;
};
