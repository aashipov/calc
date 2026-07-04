import { evaluate, ResultSet } from "mathjs";

const WELCOME: string =
  "Welcome to calc service\nHTTP POST your expression / (via mathjs)";
const NAN: string = "NaN";
const EXPRTK: string = "exprtk";
const C_EXPRTK_ADAPTER_NAME: string = "libc-exprtk-adapter.so";

const C_EXPRTK_ADAPTER = Deno.dlopen(C_EXPRTK_ADAPTER_NAME, {
  calculate: { parameters: ["buffer"], result: "f64" },
});

const HTTP_PORT: number = Deno.env.has("HTTP_PORT")
  ? parseInt(Deno.env.get("HTTP_PORT")!)
  : 8080;

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
  const enc = new TextEncoder();
  const c_string_buf = enc.encode(expr + "\0");
  const result: number = C_EXPRTK_ADAPTER.symbols.calculate(c_string_buf);
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
