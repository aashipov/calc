import { Injectable } from "@nestjs/common";
import { viaExprtk, viaMathJs, viaMeval } from './expr-adapter';

export const WELCOME: string =
  "Welcome to calc service\nHTTP POST your expression / (via mathjs) or /exprtk (via exprtk)";
export const NAN_STR = "NaN";

@Injectable()
export class AppService {
  welcome(): string {
    return WELCOME;
  }
  doViaExprtk(expr: string): string {
    return "" + viaExprtk(expr);
  }
  doViaMeval(expr: string): string {
    return "" + viaMeval(expr);
  }
  doViaMathJs(expr: string): string {
    return "" + viaMathJs(expr);
  }
}
