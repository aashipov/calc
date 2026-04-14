import { evaluate } from "mathjs";
import { dlopen, FFIType, suffix } from "bun:ffi";

const NAN: string = "NaN";
const EXPRTK: string = "exprtk";
const MXPARSER: string = "mxparser";
const RS_EXPR_ADAPTER_NAME: string = `librs_expr_adapter.${suffix}`;
const TEXT_ENCODER: TextEncoder = new TextEncoder();

const RS_EXPR_ADAPTER = dlopen(RS_EXPR_ADAPTER_NAME, {
  via_exprtk: {
    args: [FFIType.cstring],
    returns: FFIType.double,
  },
  via_meval: {
    args: [FFIType.cstring],
    returns: FFIType.double,
  },
});

const HTTP_PORT: number =
  process.env.HTTP_PORT === undefined ? 8080 : parseInt(process.env.HTTP_PORT);

const viaMathJs = (expr: string): number => {
  return evaluate(expr).entries[0];
};

const viaExprtk = (expr: string): number => {
  const c_string_buf = TEXT_ENCODER.encode(expr + "\0");
  const result: number = RS_EXPR_ADAPTER.symbols.via_exprtk(c_string_buf);
  return result;
};

const viaMeval = (expr: string): number => {
  const c_string_buf = TEXT_ENCODER.encode(expr + "\0");
  const result: number = RS_EXPR_ADAPTER.symbols.via_meval(c_string_buf);
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
    if (url.includes(MXPARSER)) {
      result = "" + viaMeval(expr);
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

Bun.serve({
  port: HTTP_PORT,
  fetch: handler,
  reusePort: true,
});
