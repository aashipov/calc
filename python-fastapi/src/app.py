import os

import uvicorn
from fastapi import Body, FastAPI
from fastapi.responses import PlainTextResponse

from src.c_exprtk_adapter import calculate_via_exprtk

WELCOME: str = "Welcome to calc service\nHTTP POST your expression\n"
NAN: str = "NaN"
HTTP_PORT: int = int(os.getenv("HTTP_PORT", 8080))


def create_calc() -> FastAPI:
    app: FastAPI = FastAPI(title="Calc", docs_url="/openapi-ui")

    @app.get("/{catch_all:path}", response_class=PlainTextResponse)
    async def welcome() -> str:
        return WELCOME

    @app.post("/{catch_all:path}", response_class=PlainTextResponse)
    async def evaluate(body: str = Body(..., media_type="text/plain")) -> str:
        result = NAN
        try:
            result = calculate_via_exprtk(body)
        except:
            pass
        return result

    return app


app = create_calc()

if __name__ == "__main__":
    uvicorn.run("app:app", host="0.0.0.0", port=HTTP_PORT, log_level="error")
