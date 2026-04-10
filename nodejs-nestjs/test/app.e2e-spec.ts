import { describe, it, expect, beforeAll, afterAll } from "vitest";
import { INestApplication } from '@nestjs/common';
import { Test } from '@nestjs/testing';
import request from 'supertest';
import { App } from 'supertest/types';
import { AppModule } from './../src/app.module';
import { EXPRTK, NAN, WELCOME } from '../src/app.service';
import bodyParser from 'body-parser';
import { COMPLEX_EXPRESSION, COMPLEX_EXPRESSION_RESULT, SIMPLE_EXPRESSION, SIMPLE_EXPRESSION_RESULT } from "./constants";

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


  describe("GET /", () => {
    it("should return welcome message", async () => {
      return request(app.getHttpServer()).get('/').expect(200).expect(WELCOME);
    });
  });

  describe("POST / (mathjs)", () => {
    it("should evaluate simple expression via mathjs", async () => {
      return request(app.getHttpServer())
        .post('/')
        .set('Content-Type', 'text/plain')
        .send(SIMPLE_EXPRESSION)
        .expect(200)
        .expect(SIMPLE_EXPRESSION_RESULT);
    });
  });

  describe("POST / (mathjs)", () => {
    it("should evaluate complex expression via mathjs", async () => {
      return request(app.getHttpServer())
        .post('/')
        .set('Content-Type', 'text/plain')
        .send(COMPLEX_EXPRESSION)
        .expect(200)
        .expect(COMPLEX_EXPRESSION_RESULT);
    });
  });

  describe("POST / (mathjs)", () => {
    it("should evaluate invalid expression via mathjs", async () => {
      return request(app.getHttpServer())
        .post('/')
        .set('Content-Type', 'text/plain')
        .send(NAN)
        .expect(200)
        .expect(NAN);
    });
  });

  describe("POST / (exprtk)", () => {
    it("should evaluate simple expression via exprtk", async () => {
      return request(app.getHttpServer())
        .post('/' + EXPRTK)
        .set('Content-Type', 'text/plain')
        .send(SIMPLE_EXPRESSION)
        .expect(200)
        .expect(SIMPLE_EXPRESSION_RESULT);
    });
  });

  describe("POST / (exprtk)", () => {
    it("should evaluate complex expression via exprtk", async () => {
      return request(app.getHttpServer())
        .post('/' + EXPRTK)
        .set('Content-Type', 'text/plain')
        .send(COMPLEX_EXPRESSION)
        .expect(200)
        .expect(COMPLEX_EXPRESSION_RESULT);
    });
  });

  describe("POST / (exprtk)", () => {
    it("should evaluate invalid expression via exprtk", async () => {
      return request(app.getHttpServer())
        .post('/' + EXPRTK)
        .set('Content-Type', 'text/plain')
        .send(NAN)
        .expect(200)
        .expect(NAN);
    });
  });
});
