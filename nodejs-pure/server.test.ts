import { describe, it, expect, beforeAll, afterAll } from "vitest";
import { createServerInstance, NAN, EXPRTK } from "./server";

const SIMPLE_EXPRESSION: string = "2 + 2";
const SIMPLE_EXPRESSION_RESULT: string = "4";
const COMPLEX_EXPRESSION: string =
  "(-abs(pi*2-e-(32-4)/(23+4/5)-(2-4)*(4+6-98.2)+4))+1.9e2";
const COMPLEX_EXPRESSION_RESULT: string = "19.988432890485228";

describe("Calculator Server", () => {
  let server: ReturnType<typeof createServerInstance>;

  beforeAll(async () => {
    return new Promise<void>((resolve) => {
      server = createServerInstance();
      server.listen(0, () => resolve());
    });
  });

  afterAll(() => {
    return new Promise<void>((resolve) => {
      server.close(() => resolve());
    });
  });

  describe("GET /", () => {
    it("should return welcome message", async () => {
      const port = (server.address() as any).port;
      const res = await fetch(`http://localhost:${port}/`);
      const body = await res.text();
      expect(body).toContain("Welcome to calc service");
      expect(body).toContain("HTTP POST your expression");
    });
  });

  describe("POST / (mathjs)", () => {
    it("should evaluate simple expression via mathjs", async () => {
      const port = (server.address() as any).port;
      const res = await fetch(`http://localhost:${port}/`, {
        method: "POST",
        body: SIMPLE_EXPRESSION,
      });
      expect(await res.text()).toBe(SIMPLE_EXPRESSION_RESULT);
    });
  });

  describe("POST / (mathjs)", () => {
    it("should evaluate complex expression via mathjs", async () => {
      const port = (server.address() as any).port;
      const res = await fetch(`http://localhost:${port}/`, {
        method: "POST",
        body: COMPLEX_EXPRESSION,
      });
      expect(await res.text()).toBe(COMPLEX_EXPRESSION_RESULT);
    });
  });

  describe("POST / (mathjs)", () => {
    it("should evaluate invalid expression via mathjs", async () => {
      const port = (server.address() as any).port;
      const res = await fetch(`http://localhost:${port}/`, {
        method: "POST",
        body: NAN,
      });
      expect(await res.text()).toBe(NAN);
    });
  });

  describe("POST / (exprtk)", () => {
    it("should evaluate simple expression via exprtk", async () => {
      const port = (server.address() as any).port;
      const res = await fetch(`http://localhost:${port}/${EXPRTK}`, {
        method: "POST",
        body: SIMPLE_EXPRESSION,
      });
      expect(await res.text()).toBe(SIMPLE_EXPRESSION_RESULT);
    });
  });

  describe("POST / (exprtk)", () => {
    it("should evaluate complex expression via exprtk", async () => {
      const port = (server.address() as any).port;
      const res = await fetch(`http://localhost:${port}/${EXPRTK}`, {
        method: "POST",
        body: COMPLEX_EXPRESSION,
      });
      expect(await res.text()).toBe(COMPLEX_EXPRESSION_RESULT);
    });
  });

  describe("POST / (exprtk)", () => {
    it("should evaluate invalid expression via exprtk", async () => {
      const port = (server.address() as any).port;
      const res = await fetch(`http://localhost:${port}/${EXPRTK}`, {
        method: "POST",
        body: NAN,
      });
      expect(await res.text()).toBe(NAN);
    });
  });
});
