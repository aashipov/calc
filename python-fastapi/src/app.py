import os

import uvicorn
from fastapi import Body, FastAPI
from fastapi.responses import PlainTextResponse

from src.expr_adapter import ExprAdapter

WELCOME: str = "Welcome to calc service\nHTTP POST your expression\n"
HTTP_PORT: int = int(os.getenv("HTTP_PORT", "8080"))
ADAPTER: ExprAdapter = ExprAdapter()
MEVAL: str = "meval"


def create_calc() -> FastAPI:
    app: FastAPI = FastAPI(title="Calc", docs_url="/openapi-ui")

    @app.get("/{catch_all:path}", response_class=PlainTextResponse)
    async def welcome() -> str:
        return WELCOME

    @app.post("/{catch_all:path}", response_class=PlainTextResponse)
    async def evaluate(
        catch_all: str, body: str = Body(..., media_type="text/plain")
    ) -> str:
        res: float = (
            ADAPTER.via_meval(body) if MEVAL in catch_all else ADAPTER.via_exprtk(body)
        )
        return str(res)

    return app


app = create_calc()

if __name__ == "__main__":
    uvicorn.run("app:app", host="0.0.0.0", port=HTTP_PORT)
