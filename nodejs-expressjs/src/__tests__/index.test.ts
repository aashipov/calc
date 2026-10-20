import { describe, it, expect } from "vitest";
import request from "supertest";
import { buildApp } from "../index";
import { EXPRTK, MEVAL } from "../lib/expr-adapter";
import { WELCOME } from "../middleware/middleware";
import {
  COMPLEX_EXPRESSION,
  COMPLEX_EXPRESSION_RESULT,
  SIMPLE_EXPRESSION,
  SIMPLE_EXPRESSION_RESULT,
  NAN_EXPRESSION,
  NAN_EXPRESSION_RESULT,
} from "./test-constants.js";

const GET: string = "GET";
const POST: string = "POST";

describe("Server", () => {
  let app = buildApp();

  describe.each([
    { method: GET, path: "/", payload: "", expected: WELCOME },

    {
      method: POST,
      path: "/",
      payload: SIMPLE_EXPRESSION,
      expected: SIMPLE_EXPRESSION_RESULT,
    },
    {
      method: POST,
      path: "/",
      payload: COMPLEX_EXPRESSION,
      expected: COMPLEX_EXPRESSION_RESULT,
    },
    {
      method: POST,
      path: "/",
      payload: NAN_EXPRESSION,
      expected: NAN_EXPRESSION_RESULT,
    },

    {
      method: POST,
      path: "/" + EXPRTK,
      payload: SIMPLE_EXPRESSION,
      expected: SIMPLE_EXPRESSION_RESULT,
    },
    {
      method: POST,
      path: "/" + EXPRTK,
      payload: COMPLEX_EXPRESSION,
      expected: COMPLEX_EXPRESSION_RESULT,
    },
    {
      method: POST,
      path: "/" + EXPRTK,
      payload: NAN_EXPRESSION,
      expected: NAN_EXPRESSION_RESULT,
    },

    {
      method: POST,
      path: "/" + MEVAL,
      payload: SIMPLE_EXPRESSION,
      expected: SIMPLE_EXPRESSION_RESULT,
    },
    {
      method: POST,
      path: "/" + MEVAL,
      payload: COMPLEX_EXPRESSION,
      expected: COMPLEX_EXPRESSION_RESULT,
    },
    {
      method: POST,
      path: "/" + MEVAL,
      payload: NAN_EXPRESSION,
      expected: NAN_EXPRESSION_RESULT,
    },
  ])(
    "$method $path with $payload should return $expected",
    ({ method, path, payload, expected }) => {
      it("should", async () => {
        let res;
        if (method === GET) {
          res = await request(app).get(path);
        } else {
          res = await request(app)
            .post(path)
            .set("Content-Type", "text/plain")
            .send(payload);
        }
        expect(res.status).toBe(200);
        expect(res.text).toBe(String(expected));
      });
    },
  );
});
