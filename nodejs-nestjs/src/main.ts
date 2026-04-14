import { NestFactory } from "@nestjs/core";
import bodyParser from "body-parser";
import { AppModule } from "./app.module";
import { ClusterService } from "./cluster.service";

const isClustered = (): boolean => process.env.HTTP_PORT === undefined;
const HTTP_PORT: number = isClustered()
  ? 8080
  : parseInt(process.env.HTTP_PORT!);

async function bootstrap() {
  const app = await NestFactory.create(AppModule);
  app.use(bodyParser.text());
  await app.listen(HTTP_PORT, "0.0.0.0");
}
if (isClustered()) {
  ClusterService.clusterize(bootstrap);
} else {
  console.log(`Single process ${process.pid}`);
  bootstrap();
}
