import { Injectable } from "@nestjs/common";
import { evaluate, ResultSet } from "mathjs";
import koffi from "koffi";

export const WELCOME: string =
  "Welcome to calc service\nHTTP POST your expression / (via mathjs) or /exprtk (via exprtk)";
export const NAN = "NaN";
export const EXPRTK: string = "exprtk";

/**
 * Calculate expression via mathjs.
 * @param expr expression
 * @returns result
 */
export const viaMathJs = (expr: string): string => {
  let result: unknown = evaluate(expr);
  if (result === undefined || result === null) {
    return NAN;
  }
  if ((result as ResultSet).entries !== undefined) {
    const entries = (result as ResultSet).entries;
    result = entries.length === 0 ? NAN : entries[0];
  }
  return String(result);
};

const cExprtkAdapter: koffi.IKoffiLib = koffi.load("libc-exprtk-adapter.so");
const koffiFunctionExprtk: koffi.KoffiFunction = cExprtkAdapter.func(
  "double calculate(str)",
);
/**
 * Calculate expression via exprtk.
 * @param expr expression
 * @returns result
 */
export const viaExprtk = (expr: string): string => {
  let result: unknown = koffiFunctionExprtk(expr);
  if (result === undefined || result === null) {
    return NAN;
  }
  if (Array.isArray(result)) {
    result = result.length === 0 ? NAN : result[0];
  }
  return String(result);
};

@Injectable()
export class AppService {
  welcome(): string {
    return WELCOME;
  }
  doViaMathJs(expr: string): string {
    return viaMathJs(expr);
  }
  doViaExprtk(expr: string): string {
    return viaExprtk(expr) as string;
  }
}
