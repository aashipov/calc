import { describe, it, expect, beforeAll, afterAll } from "vitest";
import { INestApplication } from "@nestjs/common";
import { Test } from "@nestjs/testing";
import request from "supertest";
import { App } from "supertest/types";
import { AppModule } from "./../src/app.module";
import { NAN_STR, WELCOME } from "../src/app.service";
import bodyParser from "body-parser";
import { EXPRTK, MEVAL } from "./../src/expr-adapter";
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
  let app: INestApplication<App>;

  beforeAll(async () => {
    const moduleFixture = await Test.createTestingModule({
      imports: [AppModule],
    }).compile();

    app = moduleFixture.createNestApplication();
    app.use(bodyParser.text());
    await app.init();
  });

  afterAll(async () => {
    await app.close();
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
        const expectedString = String(expected);
        if (method === GET) {
          return request(app.getHttpServer())
            .get(path)
            .expect(200)
            .expect(expectedString);
        } else {
          return request(app.getHttpServer())
            .post(path)
            .set("Content-Type", "text/plain")
            .send(payload)
            .expect(200)
            .expect(expectedString);
        }
      });
    },
  );
});
