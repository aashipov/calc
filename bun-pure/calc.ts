import { dlopen, FFIType, suffix } from "bun:ffi";

const EXPRTK: string = "exprtk";
const C_EXPRTK_ADAPTER_NAME: string = `libc-${EXPRTK}-adapter.${suffix}`;

const C_EXPRTK_ADAPTER = dlopen(C_EXPRTK_ADAPTER_NAME, {
  calculate: {
    args: [FFIType.cstring],
    returns: FFIType.double,
  },
});

const viaExprtk = (expr: string): number => {
  const enc = new TextEncoder();
  const c_string_buf = enc.encode(expr + "\0");
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
    let result: string = "" + viaExprtk(expr);
    result = "" + viaExprtk(expr);
    return textResponse(result);
  } else {
    return textResponse(WELCOME);
  }
};

Bun.serve({
  port: 8080,
  fetch: handler,
});
