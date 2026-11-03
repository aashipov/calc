import { Application, Context } from "@oak/oak";
import { EXPRTK, MEVAL, viaExprtk, viaMathJs, viaMeval } from "./expr-adapter";

const WELCOME: string =
  "Welcome to calc service\nHTTP POST your expression / (via mathjs)";
const NAN_STR: string = "NaN";


const HTTP_PORT: number = Deno.env.has("HTTP_PORT")
  ? parseInt(Deno.env.get("HTTP_PORT")!)
  : 8080;

const handler = async (
  ctx: Context<Record<string, any>, Record<string, any>>,
): Promise<void> => {
  if (ctx.request.method === "POST") {
    const expr: string = await ctx.request.body.text();
    let result: string = NAN_STR;
    const url: string = ctx.request.url.toString();
    if (url.includes(EXPRTK)) {
      result = "" + viaExprtk(expr);
    } else if (url.includes(MEVAL)) {
      result = "" + viaMeval(expr);
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

Deno.addSignalListener("SIGTERM", () => {
  console.log("SIGTERM signal received.");
  Deno.exit(0);
});

Deno.addSignalListener("SIGINT", () => {
  console.log("SIGINT signal received.");
  Deno.exit(0);
});
