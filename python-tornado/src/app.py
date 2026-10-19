import asyncio
import os
from typing import override

import tornado

from src.expr_adapter import ExprAdapter

WELCOME: str = "Welcome to calc service\nHTTP POST your expression\n"
HTTP_PORT: int = int(os.getenv("HTTP_PORT", "8080"))
EXPR_ADAPTER: ExprAdapter = ExprAdapter()
NAN: str = str(EXPR_ADAPTER.NAN)
MEVAL: str = "meval"


class CalcHandler(tornado.web.RequestHandler):
    @override
    async def get(self):
        self.set_header("Content-Type", "text/plain")
        self.write(WELCOME)

    @override
    async def post(self):
        result: str = NAN
        try:
            body: str = self.request.body.decode("utf-8")
            result: float = (
                EXPR_ADAPTER.via_meval(body)
                if MEVAL in self.request.path
                else EXPR_ADAPTER.via_exprtk(body)
            )
        except:
            pass
        self.set_header("Content-Type", "text/plain")
        self.write(f"{result}")


def create_calc() -> tornado.web.Application:
    return tornado.web.Application(
        [
            (r"/", CalcHandler),
            (r"/exprtk", CalcHandler),
            (r"/mxparser", CalcHandler),
            (r"/meval", CalcHandler),
        ]
    )


app = create_calc()


async def create_standalone_calc():
    _ = app.listen(port=HTTP_PORT)
    _ = await asyncio.Event().wait()


if __name__ == "__main__":
    _ = asyncio.run(create_standalone_calc())
