import { createServer, IncomingMessage, ServerResponse, Server } from "http";
import { evaluate } from "mathjs";
import cluster from "cluster";
import { availableParallelism } from "os";
import koffi from "koffi";

export const WELCOME: string =
  "Welcome to calc service\nHTTP POST your expression / (via mathjs) or /exprtk (via exprtk)";
export const NAN = "NaN";
export const EXPRTK: string = "exprtk";
const NUM_CPUS = Math.max(2, availableParallelism());

const viaMathJs = (expr: string): string => evaluate(expr);
const cExprtkAdapter = koffi.load("libc-exprtk-adapter.so");
const viaExprtk = cExprtkAdapter.func("double calculate(str)");

export const handler = (request: IncomingMessage, response: ServerResponse) => {
  if (request.method === "POST") {
    const chunks: string[] = [];
    request
      .on("data", (chunk: string) => {
        chunks.push(chunk);
      })
      .on("end", () => {
        let result = NAN;
        let expr = "";
        try {
          expr = chunks.join("").trim();
          if (request.url?.includes(EXPRTK)) {
            result = "" + viaExprtk(expr);
          } else {
            result = "" + viaMathJs(expr);
          }
        } catch (exc: any) {
          result += " " + expr + ": " + exc.message;
        } finally {
          response.end(result);
        }
      })
      .on("error", (err) => {
        response.end(err);
      });
  } else {
    response.end(WELCOME);
  }
};

export const createServerInstance = (
  port = 8080,
): Server<typeof IncomingMessage, typeof ServerResponse> => {
  return createServer(handler).listen({ host: "0.0.0.0", port });
};

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

if (cluster.isPrimary) {
  console.log(`Primary process ${process.pid}`);
  for (let i = 0; i < NUM_CPUS; i++) {
    cluster.fork();
  }
  process.on("SIGTERM", () => gracefulShutdown(process, "SIGTERM"));
  process.on("SIGINT", () => gracefulShutdown(process, "SIGINT"));
} else {
  console.log(`Worker process ${process.pid}`);
  createServerInstance();
}
