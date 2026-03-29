import {
  Body,
  Controller,
  Get,
  HttpCode,
  HttpStatus,
  Post,
} from '@nestjs/common';
import { AppService } from './app.service';

@Controller()
export class AppController {
  constructor(private readonly appService: AppService) {}

  @Get('/*')
  get(): string {
    return this.appService.welcome();
  }
  @HttpCode(HttpStatus.OK)
  @Post('/exprtk')
  exprtk(@Body() expr: string): string {
    return this.appService.doViaExprtk(expr);
  }
  @HttpCode(HttpStatus.OK)
  @Post(['/', '/mxparser'])
  mathjs(@Body() expr: string): string {
    return this.appService.doViaMathJs(expr);
  }
}
