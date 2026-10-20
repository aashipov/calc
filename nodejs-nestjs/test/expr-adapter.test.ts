import { describe, expect, test } from "vitest";
import {
  COMPLEX_EXPRESSION,
  COMPLEX_EXPRESSION_RESULT,
  NAN_EXPRESSION,
  NAN_EXPRESSION_RESULT,
  SIMPLE_EXPRESSION,
  SIMPLE_EXPRESSION_RESULT,
} from "./test-constants";
import { viaExprtk, viaMathJs, viaMeval } from './../src/expr-adapter';

const vias = {
  exprtk: (expr: string) => viaExprtk(expr),
  mathjs: (expr: string) => viaMathJs(expr),
  meval: (expr: string) => viaMeval(expr),
};

type ViasNames = keyof typeof vias;

function executeCallable<K extends ViasNames>(
  callable: K,
  ...args: Parameters<(typeof vias)[K]>
) {
  const fn = vias[callable] as (...args: Parameters<(typeof vias)[K]>) => any;
  return fn(...args);
}

describe.each([
  {
    via: "exprtk",
    expr: SIMPLE_EXPRESSION,
    expected: SIMPLE_EXPRESSION_RESULT,
  },
  {
    via: "mathjs",
    expr: SIMPLE_EXPRESSION,
    expected: SIMPLE_EXPRESSION_RESULT,
  },
  {
    via: "meval",
    expr: SIMPLE_EXPRESSION,
    expected: SIMPLE_EXPRESSION_RESULT,
  },

  {
    via: "exprtk",
    expr: COMPLEX_EXPRESSION,
    expected: COMPLEX_EXPRESSION_RESULT,
  },
  {
    via: "mathjs",
    expr: COMPLEX_EXPRESSION,
    expected: COMPLEX_EXPRESSION_RESULT,
  },
  {
    via: "meval",
    expr: COMPLEX_EXPRESSION,
    expected: COMPLEX_EXPRESSION_RESULT,
  },

  {
    via: "exprtk",
    expr: NAN_EXPRESSION,
    expected: NAN_EXPRESSION_RESULT,
  },
  {
    via: "mathjs",
    expr: NAN_EXPRESSION,
    expected: NAN_EXPRESSION_RESULT,
  },
  {
    via: "meval",
    expr: NAN_EXPRESSION,
    expected: NAN_EXPRESSION_RESULT,
  },
])("$expr $via is $expected", ({ via, expr, expected }) => {
  test("should", () => {
    let actual: number = executeCallable(via as ViasNames, expr);
    if (expr === NAN_EXPRESSION) {
      expect(actual).toBe(expected);
    } else {
      expect(actual).toBeCloseTo(expected as number);
    }
  });
});
