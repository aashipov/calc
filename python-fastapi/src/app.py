import uvicorn
from fastapi import Body, FastAPI
from fastapi.responses import PlainTextResponse

from src.c_exprtk_adapter import calculate_via_exprtk

WELCOME: str = "Welcome to calc service\nHTTP POST your expression\n"
NAN = "NaN"


def create_calc() -> FastAPI:
    app: FastAPI = FastAPI(title="Calc", docs_url="/openapi-ui")

    @app.get("/{catch_all:path}", response_class=PlainTextResponse)
    async def welcome():
        return WELCOME

    @app.post("/{catch_all:path}", response_class=PlainTextResponse)
    async def evaluate(body: str = Body(..., media_type="text/plain")):
        result = NAN
        try:
            result = calculate_via_exprtk(body)
        except:
            pass
        return result

    return app


app = create_calc()

if __name__ == "__main__":
    uvicorn.run(app, host="0.0.0.0", port=8080, log_level="error")
