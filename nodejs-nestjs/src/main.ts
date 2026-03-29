import { NestFactory } from '@nestjs/core';
import bodyParser from 'body-parser';
import { AppModule } from './app.module';
import { ClusterService } from './cluster.service';

async function bootstrap() {
  const app = await NestFactory.create(AppModule);
  app.use(bodyParser.text());
  await app.listen(8080, '0.0.0.0');
}

ClusterService.clusterize(bootstrap);
