import { Injectable } from '@nestjs/common';
import { evaluate } from 'mathjs';

export const WELCOME =
  'Welcome to calc service\nHTTP POST your expression / (via mathjs)';
export const CAN_NOT_EVALUATE = 'Can not evaluate';

const evaluateInner = (expr: string): string => {
  let result: string = CAN_NOT_EVALUATE;
  try {
    // eslint-disable-next-line @typescript-eslint/no-unsafe-assignment
    const evaluateResult = evaluate(expr);
    if (Array.isArray(evaluateResult)) {
      result = '' + evaluateResult.entries[0];
    } else {
      result = '' + evaluateResult;
    }
  } catch (exc) {
    result += '' + expr + ': ' + exc.message;
  }
  return result;
};

@Injectable()
export class AppService {
  welcome(): string {
    return WELCOME;
  }
  doEval(expr: string): string {
    return evaluateInner(expr);
  }
}
