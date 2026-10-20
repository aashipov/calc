import { describe, it, expect, beforeAll, afterAll } from "vitest";
import { buildServerInstance, WELCOME } from "./server";
import { AddressInfo } from "net";
import { EXPRTK, MEVAL } from "./expr-adapter";
import {
  COMPLEX_EXPRESSION,
  COMPLEX_EXPRESSION_RESULT,
  NAN_EXPRESSION,
  NAN_EXPRESSION_RESULT,
  SIMPLE_EXPRESSION,
  SIMPLE_EXPRESSION_RESULT,
} from "./test-constants";

const GET: string = "GET";
const POST: string = "POST";

describe("Server", () => {
  let server: ReturnType<typeof buildServerInstance>;

  beforeAll(async () => {
    return new Promise<void>((resolve) => {
      server = buildServerInstance();
      server.listen(0, () => resolve());
    });
  });

  afterAll(() => {
    return new Promise<void>((resolve) => {
      server.close(() => resolve());
    });
  });

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
        const port = (server.address() as AddressInfo).port;
        let res;
        if (method === GET) {
          res = await fetch(`http://localhost:${port}/${path}`, {
            method: method,
          });
        } else {
          res = await fetch(`http://localhost:${port}/${path}`, {
            method: method,
            body: payload,
          });
        }
        expect(await res.text()).toBe(String(expected));
      });
    },
  );
});
