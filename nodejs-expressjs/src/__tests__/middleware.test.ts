import { describe, it, expect } from "vitest";
import { NAN, viaExprtk, viaMathJs } from "../middleware/middleware.js";
import {
  COMPLEX_EXPRESSION,
  COMPLEX_EXPRESSION_RESULT,
  SIMPLE_EXPRESSION,
  SIMPLE_EXPRESSION_RESULT,
} from "./constants.js";

describe("Library", () => {
  it("should evaluate simple expression via mathjs", () => {
    let actual: string = viaMathJs(SIMPLE_EXPRESSION);
    expect(actual).toContain(SIMPLE_EXPRESSION_RESULT);
  });

  it("should evaluate complex expression via mathjs", () => {
    let actual: string = viaMathJs(COMPLEX_EXPRESSION);
    expect(actual).toContain(COMPLEX_EXPRESSION_RESULT);
  });

  it("should evaluate invalid expression via mathjs", () => {
    let actual: string = viaMathJs(NAN);
    expect(actual).toContain(NAN);
  });

  it("should evaluate simple expression via exprtk", () => {
    let actual: string = viaExprtk(SIMPLE_EXPRESSION);
    expect(actual).toContain(SIMPLE_EXPRESSION_RESULT);
  });

  it("should evaluate complex expression via exprtk", () => {
    let actual: string = viaExprtk(COMPLEX_EXPRESSION);
    expect(actual).toContain(COMPLEX_EXPRESSION_RESULT);
  });

  it("should evaluate invalid expression via exprtk", () => {
    let actual: string = viaExprtk(NAN);
    expect(actual).toContain(NAN);
  });
});
