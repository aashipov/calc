import { RequestHandler } from "express";
import { evaluate, ResultSet } from "mathjs";
import koffi from "koffi";

export const WELCOME: string =
  "Welcome to calc service\nHTTP POST your expression / (via mathjs) or /exprtk (via exprtk)";
export const NAN_STR = "NaN";
export const EXPRTK: string = "exprtk";
const C_EXPRTK_ADAPTER: koffi.IKoffiLib = koffi.load("libc-exprtk-adapter.so");
const KOFFI_C_EXPRTK_ADAPTER: koffi.KoffiFunction = C_EXPRTK_ADAPTER.func(
  "double calculate(str)",
);

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

export const getMiddleware: RequestHandler = (req, res) => res.send(WELCOME);

export const postMiddleware: RequestHandler = (req, res) => {
  const expr = req.body as string;
  let result: string = NAN_STR;
  try {
    if (req.url.includes(EXPRTK)) {
      result = "" + viaExprtk(expr);
    } else {
      result = "" + viaMathJs(expr);
    }
  } catch (exc: any) {
    result += " " + expr + ": " + exc.message;
  }
  res.send(result);
};
