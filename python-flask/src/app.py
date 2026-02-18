import cexprtk
from flask import Flask, request
from src.c_exprtk_adapter import calculate_via_exprtk

WELCOME: str = "Welcome to calc service\nHTTP POST your expression\n"
NAN: str = "nan"


def create_calc(is_testing: bool) -> Flask:
    calc: Flask = Flask(__name__)

    if is_testing:
        calc.config.update({"TESTING": True})

    @calc.route("/", defaults={"path": ""}, methods=["GET"])
    @calc.route("/<path:path>", methods=["GET"])
    def welcome(path):
        return WELCOME

    @calc.route("/", defaults={"path": ""}, methods=["POST"])
    @calc.route("/<path:path>", methods=["POST"])
    def evaluate(path):
        result = NAN
        try:
            body_string: str = request.data.decode("utf-8")
            result = calculate_via_exprtk(body_string)
        except:
            pass
        return result

    return calc


app = create_calc(False)


if __name__ == "__main__":
    app.run(host="0.0.0.0", port=8080)
