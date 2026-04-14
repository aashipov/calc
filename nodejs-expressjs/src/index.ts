import express from "express";
import { postMiddleware, getMiddleware } from "./middleware/middleware.js";
import bodyParser from "body-parser";
import cluster from "cluster";
import { availableParallelism, hostname } from "os";

const NUM_CPUS = Math.max(2, availableParallelism());
const isClustered = (): boolean => process.env.HTTP_PORT === undefined;
const HTTP_PORT: number = isClustered()
  ? 8080
  : parseInt(process.env.HTTP_PORT!);

export const buildApp = (): express.Express => {
  const app = express();
  app.use(bodyParser.text());
  app.get("*any", getMiddleware);
  app.post("*any", postMiddleware);
  return app;
};

/**
 * Enable graceful shutdown on a {@link Process}.
 * @param mainProcess {@link Process}
 * @param sig {@link Signals}
 */
const gracefulShutdown = (mainProcess: NodeJS.Process, sig: NodeJS.Signals) => {
  mainProcess.on(sig, () => {
    if (cluster.workers) {
      Object.values(cluster.workers).forEach((w) => {
        if (w) w.process.kill(sig);
      });
    }
    mainProcess.exit(0);
  });
};

/**
 * Cluster and worker launch statements.
 */
if (isClustered()) {
  if (cluster.isPrimary) {
    console.log(`Primary process ${process.pid}`);
    for (let i = 0; i < NUM_CPUS; i++) {
      cluster.fork();
    }
    process.on("SIGTERM", () => gracefulShutdown(process, "SIGTERM"));
    process.on("SIGINT", () => gracefulShutdown(process, "SIGINT"));
  } else {
    console.log(`Worker process ${process.pid}`);
    buildApp().listen(HTTP_PORT, "0.0.0.0", () => {});
  }
} else {
  console.log(`Single process ${process.pid}`);
  buildApp().listen(HTTP_PORT, "0.0.0.0", () => {});
}
