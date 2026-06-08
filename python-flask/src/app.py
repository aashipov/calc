import os

from flask import Flask, request

from src.py_exprtk_adapter import PyExprtkAdapter

WELCOME: str = "Welcome to calc service\nHTTP POST your expression\n"
HTTP_PORT: int = int(os.getenv("HTTP_PORT", 8080))
PY_EXPRTK_ADAPTER: PyExprtkAdapter = PyExprtkAdapter()
NAN: str = str(PY_EXPRTK_ADAPTER.NAN)


def create_calc(is_testing: bool) -> Flask:
    calc: Flask = Flask(__name__)

    if is_testing:
        calc.config.update({"TESTING": True})

    @calc.route("/", defaults={"path": ""}, methods=["GET"])
    @calc.route("/<path:path>", methods=["GET"])
    def welcome(path: str) -> str:
        return WELCOME

    @calc.route("/", defaults={"path": ""}, methods=["POST"])
    @calc.route("/<path:path>", methods=["POST"])
    def evaluate(path: str) -> str:
        result: str = NAN
        try:
            body_string: str = request.data.decode("utf-8")
            res: float = PY_EXPRTK_ADAPTER.calculate(body_string)
            result = str(res)
        except Exception:
            pass
        return result

    return calc


app: Flask = create_calc(False)


if __name__ == "__main__":
    app.run(host="0.0.0.0", port=HTTP_PORT)
