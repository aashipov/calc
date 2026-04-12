import { NestFactory } from "@nestjs/core";
import bodyParser from "body-parser";
import { AppModule } from "./app.module";
import { ClusterService } from "./cluster.service";

const HTTP_PORT: number = 8080;

async function bootstrap() {
  const app = await NestFactory.create(AppModule);
  app.use(bodyParser.text());
  await app.listen(HTTP_PORT, "0.0.0.0");
}
ClusterService.clusterize(bootstrap);
