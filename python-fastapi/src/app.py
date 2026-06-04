import os

import uvicorn
from fastapi import Body, FastAPI
from fastapi.responses import PlainTextResponse

from src.py_exprtk_adapter import PyExprtkAdapter

WELCOME: str = "Welcome to calc service\nHTTP POST your expression\n"
HTTP_PORT: int = int(os.getenv("HTTP_PORT", 8080))
PY_EXPRTK_ADAPTER: PyExprtkAdapter = PyExprtkAdapter()


def create_calc() -> FastAPI:
    app: FastAPI = FastAPI(title="Calc", docs_url="/openapi-ui")

    @app.get("/{catch_all:path}", response_class=PlainTextResponse)
    async def welcome() -> str:
        return WELCOME

    @app.post("/{catch_all:path}", response_class=PlainTextResponse)
    async def evaluate(body: str = Body(..., media_type="text/plain")) -> str:
        res: float = PY_EXPRTK_ADAPTER.calculate(body)
        return str(res)

    return app


app = create_calc()

if __name__ == "__main__":
    uvicorn.run("app:app", host="0.0.0.0", port=HTTP_PORT)
