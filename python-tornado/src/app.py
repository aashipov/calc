import asyncio
import os
from typing import override

import tornado

from src.c_exprtk_adapter import calculate_via_exprtk

WELCOME: str = "Welcome to calc service\nHTTP POST your expression\n"
NAN: str = "nan"
HTTP_PORT: int = int(os.getenv("HTTP_PORT", 8080))


class CalcHandler(tornado.web.RequestHandler):
    @override
    async def get(self):
        self.set_header("Content-Type", "text/plain")
        self.write(WELCOME)

    @override
    async def post(self):
        result: str = NAN
        try:
            expr: str = self.request.body.decode("utf-8")
            result = calculate_via_exprtk(expr)
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
        ]
    )


app = create_calc()


async def create_standalone_calc():
    _ = app.listen(port=HTTP_PORT)
    _ = await asyncio.Event().wait()


if __name__ == "__main__":
    _ = asyncio.run(create_standalone_calc())
