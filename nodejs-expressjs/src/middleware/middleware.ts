import { RequestHandler } from 'express';
import * as math from 'mathjs';

export const WELCOME =
  'Welcome to calc service\nHTTP POST your expression / (via mathjs)';

export const CAN_NOT_EVALUATE = 'Can not evaluate';

const evaluate = (expr: string) => {
  let result: string = CAN_NOT_EVALUATE;
  try {
    result = '' + math.evaluate(expr);
  } catch (exc) {
    result += ' ' + expr + ': ' + exc.message;
  }
  return result;
};

export const welcomeMiddleware: RequestHandler = (req, res) =>
  res.send(WELCOME);

export const evaluateMiddleware: RequestHandler = (req, res) => {
  const expr = req.body as string;
  const result = evaluate(expr);
  res.send(result);
};
