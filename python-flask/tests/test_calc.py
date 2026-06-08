from flask.testing import FlaskClient

from src.app import NAN, WELCOME
from tests.conf import (
    COMPLEX_EXPRESSION,
    COMPLEX_EXPRESSION_RESULT,
    SIMPLE_EXPRESSION,
    SIMPLE_EXPRESSION_RESULT,
    app,
    client,
)


def test_welcome(client: FlaskClient):
    response = client.get("/")
    assert 200 == response.status_code
    assert WELCOME == response.text


def test_simple_expression(client: FlaskClient):
    response = client.post("/", data=SIMPLE_EXPRESSION)
    assert 200 == response.status_code
    assert str(SIMPLE_EXPRESSION_RESULT) == response.text


def test_complex_expression(client: FlaskClient):
    response = client.post("/", data=COMPLEX_EXPRESSION)
    assert 200 == response.status_code
    assert str(COMPLEX_EXPRESSION_RESULT) == response.text


def test_invalid_expression(client: FlaskClient):
    response = client.post("/", data=NAN)
    assert 200 == response.status_code
    assert NAN == response.text
