import { INestApplication } from '@nestjs/common';
import { Test } from '@nestjs/testing';
import request from 'supertest';
import { App } from 'supertest/types';
import { AppModule } from './../src/app.module';
import { WELCOME } from '../src/app.service';
import bodyParser from 'body-parser';

const EXPRESSION: string =
  '(-abs(pi*2-e-(32-4)/(23+4/5)-(2-4)*(4+6-98.2)+4))+1.9e2';
const MATHJS_EXPECTED: string = '19.988432890485228';
const NOT_AN_EXPRESSION: string = 'NaN';

describe('AppController (e2e)', () => {
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

  it('/ (welcome)', () => {
    return request(app.getHttpServer()).get('/').expect(200).expect(WELCOME);
  });

  it('/ (expression via mathjs)', () => {
    return request(app.getHttpServer())
      .post('/')
      .set('Content-Type', 'text/plain')
      .send(EXPRESSION)
      .expect(200)
      .expect(MATHJS_EXPECTED);
  });

  it('/ (NaN via mathjs)', () => {
    return request(app.getHttpServer())
      .post('/')
      .set('Content-Type', 'text/plain')
      .send(NOT_AN_EXPRESSION)
      .expect(200)
      .expect(NOT_AN_EXPRESSION);
  });

  it('/ (expression via exprtk)', () => {
    return request(app.getHttpServer())
      .post('/exprtk')
      .set('Content-Type', 'text/plain')
      .send(EXPRESSION)
      .expect(200)
      .expect(MATHJS_EXPECTED);
  });

  it('/ (NaN via exprtk)', () => {
    return request(app.getHttpServer())
      .post('/exprtk')
      .set('Content-Type', 'text/plain')
      .send(NOT_AN_EXPRESSION)
      .expect(200)
      .expect(NOT_AN_EXPRESSION);
  });
});
