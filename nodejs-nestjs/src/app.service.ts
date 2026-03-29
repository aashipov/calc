import { Injectable } from '@nestjs/common';
import { evaluate, ResultSet } from 'mathjs';
import { load } from 'koffi';

export const WELCOME =
  'Welcome to calc service\nHTTP POST your expression / (via mathjs) or /exprtk (via exprtk)';

export const NAN = 'NaN';

const viaMathJs = (expr: string): string => {
  const result: unknown = evaluate(expr);
  if (result === undefined || result === null) {
    return NAN;
  }
  if ((result as ResultSet).entries !== undefined) {
    const entries = (result as ResultSet).entries;
    return entries.length === 0 ? NAN : String(entries[0]);
  }
  return String(result);
};

const cExprtkAdapter = load('libc-exprtk-adapter.so');
const viaExprtk = cExprtkAdapter.func('double calculate(str)');

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
