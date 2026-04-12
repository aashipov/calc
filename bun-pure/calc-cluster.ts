import { spawn } from "bun";

const NUM_CPUS = Math.max(2, navigator.hardwareConcurrency);
const BUNS = new Array(NUM_CPUS);

for (let i = 0; i < NUM_CPUS; i++) {
  BUNS[i] = spawn({
    cmd: ["bun", "./calc.ts"],
    stdout: "inherit",
    stderr: "inherit",
    stdin: "inherit",
  });
}

const gracefulShutdown = () => {
  for (const bun of BUNS) {
    bun.kill();
  }
};

process.on("SIGINT", gracefulShutdown);
process.on("exit", gracefulShutdown);
