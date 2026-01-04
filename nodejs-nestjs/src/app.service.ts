import { Injectable } from '@nestjs/common';
import * as math from 'mathjs';

export const WELCOME =
  'Welcome to calc service\nHTTP POST your expression / (via mathjs)';
export const CAN_NOT_EVALUATE = 'Can not evaluate';

const evaluate = (expr: string): string => {
  let result: string = CAN_NOT_EVALUATE;
  try {
    result = '' + math.evaluate(expr);
  } catch (exc) {
    result += ' ' + expr + ': ' + exc.message;
  }
  return result;
};

@Injectable()
export class AppService {
  welcome(): string {
    return WELCOME;
  }
  doEval(expr: string): string {
    return evaluate(expr);
  }
}
