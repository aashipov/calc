const http = require("http");
const math = require("mathjs");

const welcome =
  "Welcome to calc service\nHTTP POST your expression / (via mathjs)";
const canNotEvaluate = "Can not evaluate";

const textResponse = (response, statusCode, body) => {
  response.statusCode = statusCode;
  response.setHeader("Content-Type", "text/plain");
  response.end(body);
};

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
          textResponse(response, 200, result);
        }
      })
      .on("error", (err) => {
        textResponse(response, 200, err);
      });
  } else {
    textResponse(response, 200, welcome);
  }
};

const server = http.createServer(handler).listen(8080);

process.on("SIGTERM", () => {
  console.log("SIGTERM signal received.");
  server.close();
});

process.on("SIGINT", () => {
  console.log("SIGINT signal received.");
  server.close();
});
