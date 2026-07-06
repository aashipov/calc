import { describe, it, expect, beforeAll, afterAll } from "vitest";
import {
  NAN_STR,
  viaExprtk,
  viaMathJs,
} from "../src/app.service";
import { COMPLEX_EXPRESSION, COMPLEX_EXPRESSION_RESULT, SIMPLE_EXPRESSION, SIMPLE_EXPRESSION_RESULT } from "./constants";

describe("Library", () => {
  it("should evaluate simple expression via mathjs", () => {
    let actual: string = "" + viaMathJs(SIMPLE_EXPRESSION);
    expect(actual).toContain(SIMPLE_EXPRESSION_RESULT);
  });

  it("should evaluate complex expression via mathjs", () => {
    let actual: string = "" + viaMathJs(COMPLEX_EXPRESSION);
    expect(actual).toContain(COMPLEX_EXPRESSION_RESULT);
  });

  it("should evaluate invalid expression via mathjs", () => {
    let actual: string = "" + viaMathJs(NAN_STR);
    expect(actual).toContain(NAN_STR);
  });

  it("should evaluate simple expression via exprtk", () => {
    let actual: string = "" + viaExprtk(SIMPLE_EXPRESSION);
    expect(actual).toContain(SIMPLE_EXPRESSION_RESULT);
  });

  it("should evaluate complex expression via exprtk", () => {
    let actual: string = "" + viaExprtk(COMPLEX_EXPRESSION);
    expect(actual).toContain(COMPLEX_EXPRESSION_RESULT);
  });

  it("should evaluate invalid expression via exprtk", () => {
    let actual: string = "" + viaExprtk(NAN_STR);
    expect(actual).toContain(NAN_STR);
  });
});
