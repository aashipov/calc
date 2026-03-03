import { evaluate } from "mathjs";

const WELCOME: string =
  "Welcome to calc service\nHTTP POST your expression / (via mathjs)";

const NAN: string = "NaN";

const textResponse = (body: string) =>
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
    result = "" + evaluate(expr);
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
  // Perform cleanup here (e.g., close DB connections)
  Deno.exit(0);
});

// Gracefully handle SIGINT (e.g., Ctrl+C)
Deno.addSignalListener("SIGINT", () => {
  console.log("SIGINT signal received.");
  Deno.exit(0);
});
