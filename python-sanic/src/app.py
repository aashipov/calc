from sanic import Sanic
from sanic.response import text
from src.c_exprtk_adapter import calculate_via_exprtk

WELCOME: str = "Welcome to calc service\nHTTP POST your expression\n"

NAN = "nan"


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
            result = calculate_via_exprtk(body)
        except:
            pass
        return text(result)

    return calc


app = create_calc("__main__", False)

if __name__ == "__main__":
    app.run(port=8080)
