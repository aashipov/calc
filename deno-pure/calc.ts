import { EXPRTK, MEVAL, viaExprtk, viaMathJs, viaMeval } from "./expr-adapter";

const WELCOME: string =
  "Welcome to calc service\nHTTP POST your expression / (via mathjs)";
const NAN_STR: string = "NaN";

const HTTP_PORT: number = Deno.env.has("HTTP_PORT")
  ? parseInt(Deno.env.get("HTTP_PORT")!)
  : 8080;

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

export function startServer(port = HTTP_PORT) {
  return Deno.serve({ port }, handler);
}

startServer();

Deno.addSignalListener("SIGTERM", () => {
  console.log("SIGTERM signal received.");
  Deno.exit(0);
});

Deno.addSignalListener("SIGINT", () => {
  console.log("SIGINT signal received.");
  Deno.exit(0);
});
