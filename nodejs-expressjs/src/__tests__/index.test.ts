import { describe, it, expect } from "vitest";
import request from "supertest";
import { buildApp } from "../index";
import { NAN, EXPRTK } from "../middleware/middleware";

import {
  COMPLEX_EXPRESSION,
  COMPLEX_EXPRESSION_RESULT,
  SIMPLE_EXPRESSION,
  SIMPLE_EXPRESSION_RESULT,
} from "./constants.js";

describe("Server", () => {
  let app = buildApp();

  describe("GET /", () => {
    it("should return welcome message", async () => {
      const response = await request(app).get("/");
      expect(response.status).toBe(200);
      expect(response.text).toContain("Welcome to calc service");
    });
  });

  describe("POST / (mathjs)", () => {
    it("should evaluate simple expression via mathjs", async () => {
      const response = await request(app)
        .post("/")
        .set("Content-Type", "text/plain")
        .send(SIMPLE_EXPRESSION);
      expect(response.status).toBe(200);
      expect(response.text).toContain(SIMPLE_EXPRESSION_RESULT);
    });
  });

  describe("POST / (mathjs)", () => {
    it("should evaluate complex expression via mathjs", async () => {
      const response = await request(app)
        .post("/")
        .set("Content-Type", "text/plain")
        .send(COMPLEX_EXPRESSION);
      expect(response.status).toBe(200);
      expect(response.text).toContain(COMPLEX_EXPRESSION_RESULT);
    });
  });

  describe("POST / (mathjs)", () => {
    it("should evaluate invalid expression via mathjs", async () => {
      const response = await request(app)
        .post("/")
        .set("Content-Type", "text/plain")
        .send(NAN);
      expect(response.status).toBe(200);
      expect(response.text).toContain(NAN);
    });
  });

  describe("POST / (exprtk)", () => {
    it("should evaluate simple expression via exprtk", async () => {
      const response = await request(app)
        .post("/" + EXPRTK)
        .set("Content-Type", "text/plain")
        .send(SIMPLE_EXPRESSION);
      expect(response.status).toBe(200);
      expect(response.text).toContain(SIMPLE_EXPRESSION_RESULT);
    });
  });

  describe("POST / (exprtk)", () => {
    it("should evaluate complex expression via exprtk", async () => {
      const response = await request(app)
        .post("/" + EXPRTK)
        .set("Content-Type", "text/plain")
        .send(COMPLEX_EXPRESSION);
      expect(response.status).toBe(200);
      expect(response.text).toContain(COMPLEX_EXPRESSION_RESULT);
    });
  });

  describe("POST / (exprtk)", () => {
    it("should evaluate invalid expression via exprtk", async () => {
      const response = await request(app)
        .post("/" + EXPRTK)
        .set("Content-Type", "text/plain")
        .send(NAN);
      expect(response.status).toBe(200);
      expect(response.text).toContain(NAN);
    });
  });
});
