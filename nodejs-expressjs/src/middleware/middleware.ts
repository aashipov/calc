import { RequestHandler } from "express";
import {
  EXPRTK,
  MEVAL,
  viaExprtk,
  viaMathJs,
  viaMeval,
} from "../lib/expr-adapter";

export const WELCOME: string =
  "Welcome to calc service\nHTTP POST your expression / (via mathjs) or /exprtk (via exprtk)";
const RES_PLACEHOLDER = String(NaN);

export const getMiddleware: RequestHandler = (req, res) => res.send(WELCOME);

export const postMiddleware: RequestHandler = (req, res) => {
  const expr = req.body as string;
  let result: string = RES_PLACEHOLDER;
  try {
    if (req.url?.includes(EXPRTK)) {
      result = "" + viaExprtk(expr);
    } else if (req.url?.includes(MEVAL)) {
      result = "" + viaMeval(expr);
    } else {
      result = "" + viaMathJs(expr);
    }
  } catch (exc: any) {
    result += " " + expr + ": " + exc.message;
  }
  res.send(result);
};
