import cexprtk
from sanic import Sanic
from sanic.response import text

WELCOME: str = "Welcome to calc service\nHTTP POST your expression\n"

SYMBOL_TABLE = cexprtk.Symbol_Table(
    {
        "pi": 3.14159265358979323846264338327950288419716939937510,
        "e": 2.71828182845904523536028747135266249775724709369996,
    }
)

NAN = "NaN"


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
            expression = cexprtk.Expression(body, SYMBOL_TABLE)
            result = str(expression())
        except:
            pass
        return text(result)

    return calc


calc = create_calc("__main__", False)

if __name__ == "__main__":
    calc.run(port=8080)
