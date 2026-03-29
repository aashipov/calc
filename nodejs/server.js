const http = require("http");
const math = require("mathjs");
const cluster = require("cluster");
const numCPUs = Math.max(2, require("os").availableParallelism());

const welcome =
  "Welcome to calc service\nHTTP POST your expression / (via mathjs)";
const canNotEvaluate = "Can not evaluate";

const handler = (request, response) => {
  if (request.method === "POST") {
    const chunks = [];
    request
      .on("data", (chunk) => {
        chunks.push(chunk);
      })
      .on("end", () => {
        let result = canNotEvaluate;
        let expr = "";
        try {
          expr = Buffer.concat(chunks).toString().trim();
          result = "" + math.evaluate(expr);
        } catch (exc) {
          result += " " + expr + ": " + exc.message;
        } finally {
          response.end(result);
        }
      })
      .on("error", (err) => {
        response.end(err);
      });
  } else {
    response.end(welcome);
  }
};

const gracefulShutdown = (mainProcess, sig) => {
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
  process.on("SIGTERM", () => gracefulShutdown(process, "SIGTERM"));
  process.on("SIGINT", () => gracefulShutdown(process, "SIGINT"));
} else {
  console.log(`Worker process ${process.pid}`);
  const server = http
    .createServer(handler)
    .listen({ host: "0.0.0.0", port: 8080 });
}
