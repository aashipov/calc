import cexprtk
from flask import Flask, request

WELCOME: str = "Welcome to calc service\nHTTP POST your expression\n"

SYMBOL_TABLE = cexprtk.Symbol_Table(
    {
        "pi": 3.14159265358979323846264338327950288419716939937510,
        "e": 2.71828182845904523536028747135266249775724709369996,
    }
)

NAN = "NaN"


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
            expression = cexprtk.Expression(body_string, SYMBOL_TABLE)
            result = str(expression())
        except:
            pass
        return result

    return calc


calc = create_calc(False)


if __name__ == "__main__":
    calc.run(host="0.0.0.0", port=8080)
