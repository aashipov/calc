import { Test, TestingModule } from '@nestjs/testing';
import { AppController } from './app.controller';
import { AppService } from './app.service';
import { WELCOME, CAN_NOT_EVALUATE } from './app.service';

const SAMPLE_EXPRESSION: string =
  '(-abs(pi*2-e-(32-4)/(23+4/5)-(2-4)*(4+6-98.2)+4))+1.9e2';
const EXPECTED_RESULT_BY_SAMPLE_EXPRESSION: string = '19.988432890485228';

describe('AppController', () => {
  let app: TestingModule;

  beforeAll(async () => {
    app = await Test.createTestingModule({
      controllers: [AppController],
      providers: [AppService],
    }).compile();
  });

  describe('get', () => {
    it('should return "WELCOME"', () => {
      const appController = app.get(AppController);
      expect(appController.get()).toBe(WELCOME);
    });
  });
  describe('post ok', () => {
    it('should return "EXPECTED_RESULT_BY_SAMPLE_EXPRESSION"', () => {
      const appController = app.get(AppController);
      expect(appController.post(SAMPLE_EXPRESSION)).toBe(
        EXPECTED_RESULT_BY_SAMPLE_EXPRESSION,
      );
    });
    it('should return "CAN_NOT_EVALUATE"', () => {
      const appController = app.get(AppController);
      expect(appController.post(CAN_NOT_EVALUATE)).toContain(CAN_NOT_EVALUATE);
    });
  });
});
