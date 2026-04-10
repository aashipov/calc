import { Injectable } from '@nestjs/common';
import cluster from 'cluster';
import { availableParallelism } from 'os';

const numCPUs = Math.max(2, availableParallelism());

const gracefulShutdown = (
  mainProcess: NodeJS.Process,
  sig: NodeJS.Signals,
) => {
  mainProcess.on(sig, () => {
    if (cluster.workers) {
      Object.values(cluster.workers).forEach((w) => {
        if (w) {
          w.process.kill(sig);
        }
      });
    }
    mainProcess.exit(0);
  });
};

@Injectable()
export class ClusterService {
  static clusterize(callback: Function): void {
    if (cluster.isPrimary) {
      console.log(`Primary process ${process.pid}`);
      for (let i = 0; i < numCPUs; i++) {
        cluster.fork();
      }
      process.on('SIGTERM', () => gracefulShutdown(process, 'SIGTERM'));
      process.on('SIGINT', () => gracefulShutdown(process, 'SIGINT'));
    } else {
      console.log(`Worker process ${process.pid}`);
      callback();
    }
  }
}
