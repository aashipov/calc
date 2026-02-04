from sanic import Sanic

from calc.calc import NAN, WELCOME
from tests.conf import app

EXPRESSION: str = "(-abs(pi*2-e-(32-4)/(23+4/5)-(2-4)*(4+6-98.2)+4))+1.9e2"

EXPRESSION_RESULT: str = "19.988432890485228"

NOT_AN_EXPRESSION = "abc"


def test_welcome(app: Sanic):
    request, response = app.test_client.get("/")
    assert 200 == response.status
    assert WELCOME == response.text


def test_expression(app: Sanic):
    request, response = app.test_client.post("/", data=EXPRESSION)
    assert 200 == response.status_code
    assert EXPRESSION_RESULT == response.text


def test_not_an_expression(app: Sanic):
    request, response = app.test_client.post("/", data=NOT_AN_EXPRESSION)
    assert 200 == response.status_code
    assert NAN == response.text
