import { Application, Context } from "@oak/oak";
import { evaluate, ResultSet } from "mathjs";

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
const NAN_STR: string = "NaN";
const EXPRTK: string = "exprtk";
const C_EXPRTK_ADAPTER_NAME: string = "libc-exprtk-adapter.so";
const TEXT_ENCODER: TextEncoder = new TextEncoder();

const C_EXPRTK_ADAPTER = Deno.dlopen(C_EXPRTK_ADAPTER_NAME, {
  calculate: { parameters: ["buffer"], result: "f64" },
});

const HTTP_PORT: number = Deno.env.has("HTTP_PORT")
  ? parseInt(Deno.env.get("HTTP_PORT")!)
  : 8080;

const viaMathJs = (expression: string): number => {
  try {
    let result = evaluate(expression);
    if (result != undefined && result != null) {
      if (result.entries) {
        const entries = (result as ResultSet).entries;
        if (entries.length > 0 && entries[0] instanceof Number) {
          return entries[0] as number;
        }
      } else {
        return result as number;
      }
    }
  } catch {}
  return Number.NaN;
};

const viaExprtk = (expr: string): number => {
  try {
    const c_string_buf = TEXT_ENCODER.encode(expr + "\0");
    return C_EXPRTK_ADAPTER.symbols.calculate(c_string_buf);
  } catch {}
  return Number.NaN;
};

const handler = async (
  ctx: Context<Record<string, any>, Record<string, any>>,
): Promise<void> => {
  if (ctx.request.method === "POST") {
    const expr: string = await ctx.request.body.text();
    let result: string = NAN_STR;
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
