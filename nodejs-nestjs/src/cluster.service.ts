import { Injectable } from '@nestjs/common';
import cluster from 'node:cluster';
import { availableParallelism } from 'node:os';

const numCPUs = Math.max(2, availableParallelism());

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
