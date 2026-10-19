import os

from sanic import Sanic
from sanic.response import text

from src.expr_adapter import ExprAdapter

WELCOME: str = "Welcome to calc service\nHTTP POST your expression\n"
HTTP_PORT: int = int(os.getenv("HTTP_PORT", "8080"))
EXPR_ADAPTER: ExprAdapter = ExprAdapter()
NAN: str = str(EXPR_ADAPTER.NAN)
MEVAL: str = "meval"


def create_calc(app_name: str, is_testing: bool) -> Sanic:
    calc = Sanic(app_name)
    if is_testing:
        calc.ctx.test_mode = True

    @calc.get("/<path:path>")
    async def welcome(request, path):
        return text(WELCOME)

    @calc.post("/<path:path>", ignore_body=False)
    async def evaluate(request, path):
        result = NAN
        try:
            if not request.body:
                await request.receive_body()

            body: str = request.body.decode("utf-8")
            result: float = (
                EXPR_ADAPTER.via_meval(body)
                if MEVAL in request.path
                else EXPR_ADAPTER.via_exprtk(body)
            )
        except:
            pass
        return text(str(result))

    return calc


app = create_calc("__main__", False)

if __name__ == "__main__":
    app.run(host="0.0.0.0", port=HTTP_PORT)
