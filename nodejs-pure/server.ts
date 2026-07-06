import { createServer, IncomingMessage, ServerResponse, Server } from "http";
import { evaluate, ResultSet } from "mathjs";
import cluster from "cluster";
import { availableParallelism } from "os";
import koffi from "koffi";

export const WELCOME: string =
  "Welcome to calc service\nHTTP POST your expression / (via mathjs) or /exprtk (via exprtk)";
export const NAN_STR = "NaN";
export const EXPRTK: string = "exprtk";
const C_EXPRTK_ADAPTER: koffi.IKoffiLib = koffi.load("libc-exprtk-adapter.so");
const KOFFI_C_EXPRTK_ADAPTER: koffi.KoffiFunction = C_EXPRTK_ADAPTER.func(
  "double calculate(str)",
);
const NUM_CPUS = Math.max(2, availableParallelism());
const isClustered = (): boolean => process.env.HTTP_PORT === undefined;
const HTTP_PORT: Number = isClustered()
  ? 8080
  : parseInt(process.env.HTTP_PORT!);

/**
 * Calculate expression via mathjs.
 * @param expr expression
 * @returns result
 */
export const viaMathJs = (expression: string): number => {
  try {
    let result = evaluate(expression);
    if (result != undefined && result != null) {
      if (result.entries) {
        const entries = (result as ResultSet).entries;
        if (entries.length > 0 && entries[0] instanceof Number) {
          return entries[0] as number;
        }
      } else {
        return result as number;
      }
    }
  } catch {}
  return Number.NaN;
};

/**
 * Calculate expression via exprtk.
 * @param expr expression
 * @returns result
 */
export const viaExprtk = (expr: string): number => {
  try {
    let result: unknown = KOFFI_C_EXPRTK_ADAPTER(expr);
    if (result != undefined && result != null) {
      if (Array.isArray(result) && result.length > 0) {
        return result[0];
      } else {
        return result as number;
      }
    }
  } catch {}
  return Number.NaN;
};

/**
 * {@link Server} handler.
 * @param request {@link IncomingMessage} request
 * @param response {@link ServerResponse} response
 */
export const handler = (request: IncomingMessage, response: ServerResponse) => {
  if (request.method === "POST") {
    const chunks: string[] = [];
    request
      .on("data", (chunk: string) => {
        chunks.push(chunk);
      })
      .on("end", () => {
        let result = NAN_STR;
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

/**
 * Build {@link Server} instance.
 * @param port HTTP port
 * @returns {@link Server}
 */
export const buildServerInstance = (): Server<
  typeof IncomingMessage,
  typeof ServerResponse
> => {
  return createServer(handler).listen({
    host: "0.0.0.0",
    port: HTTP_PORT,
    reusePort: true,
  });
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
    buildServerInstance();
  }
} else {
  console.log(`Single process ${process.pid}`);
  buildServerInstance();
}
