import { RequestHandler } from 'express';
import { evaluate, ResultSet } from 'mathjs';
import { load } from 'koffi';

export const WELCOME =
  'Welcome to calc service\nHTTP POST your expression / (via mathjs) or /exprtk (via exprtk)';

export const NAN = 'NaN';

const viaMathJs = (expr: string): string => {
  const result: ResultSet = evaluate(expr);
  return result.entries.length === 0 ? NAN : (result.entries[0] as string);
};

const exprtk = 'exprtk';
const cExprtkAdapter = load('libc-exprtk-adapter.so');
const viaExprtk = cExprtkAdapter.func('double calculate(str)');

export const getMiddleware: RequestHandler = (req, res) => res.send(WELCOME);

export const postMiddleware: RequestHandler = (req, res) => {
  const expr = req.body as string;
  let result: string = NAN;
  try {
    if (req.url.includes(exprtk)) {
      result = '' + viaExprtk(expr);
    } else {
      result = viaMathJs(expr);
    }
  } catch (exc) {
    result += ' ' + expr + ': ' + exc.message;
  }
  res.send(result);
};
