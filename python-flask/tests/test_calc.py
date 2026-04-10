from flask.testing import FlaskClient
from src.app import NAN, WELCOME
from tests.conf import app, client

SIMPLE_EXPRESSION: str = "2+2"
SIMPLE_EXPRESSION_RESULT: str = "4.0"
COMPLEX_EXPRESSION: str = "(-abs(pi*2-e-(32-4)/(23+4/5)-(2-4)*(4+6-98.2)+4))+1.9e2"
COMPLEX_EXPRESSION_RESULT: str = "19.988432890485228"


def test_welcome(client: FlaskClient):
    response = client.get("/")
    assert 200 == response.status_code
    assert WELCOME == response.text

def test_simple_expression(client: FlaskClient):
    response = client.post("/", data=SIMPLE_EXPRESSION)
    assert 200 == response.status_code
    assert SIMPLE_EXPRESSION_RESULT == response.text

def test_complex_expression(client: FlaskClient):
    response = client.post("/", data=COMPLEX_EXPRESSION)
    assert 200 == response.status_code
    assert COMPLEX_EXPRESSION_RESULT == response.text


def test_invalid_expression(client: FlaskClient):
    response = client.post("/", data=NAN)
    assert 200 == response.status_code
    assert NAN == response.text
