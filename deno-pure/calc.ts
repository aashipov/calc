import { evaluate } from "mathjs";
import { via_meval } from "./lib/rs_lib.js";

const WELCOME: string =
  "Welcome to calc service\nHTTP POST your expression / (via mathjs)";
const NAN: string = "NaN";
const EXPRTK: string = "exprtk";
const MXPARSER: string = "mxparser";
const C_EXPRTK_ADAPTER_NAME: string = "libc-exprtk-adapter.so";

const getExprtkAdapter = () =>
  Deno.dlopen(C_EXPRTK_ADAPTER_NAME, {
    calculate: { parameters: ["buffer"], result: "f64" },
  });

const viaMathJs = (expr: string): number => {
  return evaluate(expr).entries[0];
};

const viaExprtk = (expr: string): number => {
  const enc = new TextEncoder();
  const c_string_buf = enc.encode(expr + "\0");
  const result: number = getExprtkAdapter().symbols.calculate(c_string_buf);
  return result;
};

const textResponse = (body: string): Response =>
  new Response(body, {
    status: 200,
    headers: {
      "Content-Type": "text/plain",
    },
  });

const handler = async (req: Request): Promise<Response> => {
  let expr: string = NAN;
  let result: string = NAN;
  if (req.method === "POST") {
    expr = await req.text();
    const url: string = req.url;
    if (url.includes(MXPARSER)) {
      result = "" + via_meval(expr);
    } else if (url.includes(EXPRTK)) {
      result = "" + viaExprtk(expr);
    } else {
      result = "" + viaMathJs(expr);
    }
    return textResponse(result);
  } else {
    return textResponse(WELCOME);
  }
};

export function startServer(port = 8080) {
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
