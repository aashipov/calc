import cexprtk
import uvicorn
from fastapi import Body, FastAPI, Response
from fastapi.responses import PlainTextResponse

WELCOME: str = "Welcome to calc service\nHTTP POST your expression\n"

SYMBOL_TABLE = cexprtk.Symbol_Table(
    {
        "pi": 3.14159265358979323846264338327950288419716939937510,
        "e": 2.71828182845904523536028747135266249775724709369996,
    }
)

NAN = "NaN"


def create_calc() -> FastAPI:
    calc:FastAPI = FastAPI(title="Calc", docs_url="/openapi-ui")

    @calc.get("/{catch_all:path}", response_class=PlainTextResponse)
    async def welcome():
        return WELCOME

    @calc.post("/{catch_all:path}", response_class=PlainTextResponse)
    async def evaluate(body: str = Body(..., media_type="text/plain")):
        result = NAN
        try:
            expression = cexprtk.Expression(body, SYMBOL_TABLE)
            result = str(expression())
        except:
            pass
        return result

    return calc


calc = create_calc()

if __name__ == "__main__":
    uvicorn.run(calc, host="0.0.0.0", port=8080, log_level="error")
