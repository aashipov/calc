import { evaluate, ResultSet } from "mathjs";
import { dlopen, FFIType, suffix } from "bun:ffi";

const NAN: string = "NaN";
const EXPRTK: string = "exprtk";
const MXPARSER: string = "mxparser";
const C_EXPRTK_ADAPTER_NAME: string = `libc-exprtk-adapter.${suffix}`;
const TEXT_ENCODER: TextEncoder = new TextEncoder();

const C_EXPRTK_ADAPTER = dlopen(C_EXPRTK_ADAPTER_NAME, {
  calculate: {
    args: [FFIType.cstring],
    returns: FFIType.double,
  },
});

const HTTP_PORT: number =
  process.env.HTTP_PORT === undefined ? 8080 : parseInt(process.env.HTTP_PORT);

const viaMathJs = (expr: string): number => {
  let result: unknown = evaluate(expr);
  if (result === undefined || result === null) {
    return Number.NaN;
  }
  if ((result as ResultSet).entries !== undefined) {
    const entries = (result as ResultSet).entries;
    result = entries.length === 0 ? Number.NaN : entries[0];
  }
  return result as number;
};

const viaExprtk = (expr: string): number => {
  const c_string_buf = TEXT_ENCODER.encode(expr + "\0");
  const result: number = C_EXPRTK_ADAPTER.symbols.calculate(c_string_buf);
  return result;
};

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
    let result: string = NAN;
    const url: string = req.url;
    if (url.includes(EXPRTK)) {
      result = "" + viaExprtk(expr);
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
