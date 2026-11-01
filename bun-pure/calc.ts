import { EXPRTK, MEVAL, viaExprtk, viaMathJs, viaMeval } from "./expr-adapter";

const NAN_STR: string = "NaN";
const HTTP_PORT: number =
  process.env.HTTP_PORT === undefined ? 8080 : parseInt(process.env.HTTP_PORT);
const WELCOME: string =
  "Welcome to calc service\nHTTP POST your expression / (via mathjs)";

const textResponse = (body: string): Response =>
  new Response(body, {
    status: 200,
    headers: {
      "Content-Type": "text/plain",
    },
  });

const handler = async (req: Request): Promise<Response> => {
  if (req.method === "POST") {
    const expr: string = await req.text();
    let result: string = NAN_STR;
    const url: string = req.url;
    if (url.includes(EXPRTK)) {
      result = "" + viaExprtk(expr);
    } else if (url.includes(MEVAL)) {
      result = "" + viaMeval(expr);
    } else {
      result = "" + viaMathJs(expr);
    }
    return textResponse(result);
  } else {
    return textResponse(WELCOME);
  }
};

Bun.serve({
  port: HTTP_PORT,
  fetch: handler,
  reusePort: true,
});
