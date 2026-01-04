import { NestFactory } from '@nestjs/core';
import * as bodyParser from 'body-parser';
import { AppModule } from './app.module';

async function bootstrap() {
  const app = await NestFactory.create(AppModule);
  app.use(bodyParser.text({ type: 'text/plain' }));
  await app.listen(8080, '0.0.0.0');
}
bootstrap();
