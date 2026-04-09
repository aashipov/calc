import { createServer, IncomingMessage, ServerResponse, Server } from "http";
import { evaluate, ResultSet } from "mathjs";
import cluster from "cluster";
import { availableParallelism } from "os";
import koffi from "koffi";

export const WELCOME: string =
  "Welcome to calc service\nHTTP POST your expression / (via mathjs) or /exprtk (via exprtk)";
export const NAN = "NaN";
export const EXPRTK: string = "exprtk";
const NUM_CPUS = Math.max(2, availableParallelism());

export const viaMathJs = (expr: string): string => {
  let result: unknown = evaluate(expr);
  if (result === undefined || result === null) {
    return NAN;
  }
  if ((result as ResultSet).entries !== undefined) {
    const entries = (result as ResultSet).entries;
    result = entries.length === 0 ? NAN : entries[0];
  }
  return String(result);
};

const cExprtkAdapter: koffi.IKoffiLib = koffi.load("libc-exprtk-adapter.so");
const koffiFunctionExprtk: koffi.KoffiFunction = cExprtkAdapter.func("double calculate(str)");
export const viaExprtk = (expr: string): string => {
  let result: unknown = koffiFunctionExprtk(expr);
  if (result === undefined || result === null) {
    return NAN;
  }
  if (Array.isArray(result)) {
    result = result.length === 0 ? NAN : result[0];
  }
  return String(result);
};

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
