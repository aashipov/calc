import { Application, Context } from "@oak/oak";
import { evaluate } from "mathjs";

Deno.addSignalListener("SIGTERM", () => {
  console.log("SIGTERM signal received.");
  Deno.exit(0);
});

Deno.addSignalListener("SIGINT", () => {
  console.log("SIGINT signal received.");
  Deno.exit(0);
});

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
  return evaluate(expr).entries[0];
};

const viaExprtk = (expr: string): number => {
  const enc = new TextEncoder();
  const c_string_buf = enc.encode(expr + "\0");
  const result: number = C_EXPRTK_ADAPTER.symbols.calculate(c_string_buf);
  return result;
};

const handler = async (
  ctx: Context<Record<string, any>, Record<string, any>>,
): Promise<void> => {
  if (ctx.request.method === "POST") {
    const expr: string = await ctx.request.body.text();
    let result: string = NAN;
    const url: string = ctx.request.url.toString();
    if (url.includes(EXPRTK)) {
      result = "" + viaExprtk(expr);
    } else {
      result = "" + viaMathJs(expr);
    }
    ctx.response.body = result;
  } else {
    ctx.response.body = WELCOME;
  }
};

const app = new Application();
app.use(handler);
await app.listen({ hostname: "0.0.0.0", port: HTTP_PORT });
