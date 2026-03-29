import express from 'express';
import { postMiddleware, getMiddleware } from './middleware/middleware.js';
import bodyParser from 'body-parser';
import cluster from 'node:cluster';
import { availableParallelism } from 'node:os';

const numCPUs = Math.max(2, availableParallelism());
const port = 8080;

const buildApp = (): express.Express => {
  const app = express();
  app.use(bodyParser.text());
  app.get('*any', getMiddleware);
  app.post('*any', postMiddleware);
  return app;
};

const gracefulShutdown = (
  mainProcess: typeof process,
  sig: 'SIGINT' | 'SIGTERM',
) => {
  mainProcess.on(sig, () => {
    Object.values(cluster.workers).forEach((w) => {
      w.process.kill(sig);
    });
    mainProcess.exit(0);
  });
};

if (cluster.isPrimary) {
  console.log(`Primary process ${process.pid}`);
  for (let i = 0; i < numCPUs; i++) {
    cluster.fork();
  }
  process.on('SIGTERM', () => gracefulShutdown(process, 'SIGTERM'));
  process.on('SIGINT', () => gracefulShutdown(process, 'SIGINT'));
} else {
  console.log(`Worker process ${process.pid}`);
  buildApp().listen(port, '0.0.0.0', () => {});
}
