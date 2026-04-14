import pytest
from webtest import TestApp

from src.app import NAN, WELCOME
from tests.conf import app

SIMPLE_EXPRESSION: str = "2+2"
SIMPLE_EXPRESSION_RESULT: str = "4.0"
COMPLEX_EXPRESSION: str = "(-abs(pi*2-e-(32-4)/(23+4/5)-(2-4)*(4+6-98.2)+4))+1.9e2"
COMPLEX_EXPRESSION_RESULT: str = "19.988432890485228"


def test_welcome(app: TestApp) -> None:
    response = app.get("/")
    assert 200 == response.status_int
    assert WELCOME == response.text


def test_simple_expression(app: TestApp) -> None:
    response = app.post("/", SIMPLE_EXPRESSION, headers={"Content-Type": "text/plain"})
    assert 200 == response.status_int
    assert SIMPLE_EXPRESSION_RESULT == response.text


def test_complex_expression(app: TestApp) -> None:
    response = app.post("/", COMPLEX_EXPRESSION, headers={"Content-Type": "text/plain"})
    assert 200 == response.status_int
    assert COMPLEX_EXPRESSION_RESULT == response.text


def test_invalid_expression(app: TestApp) -> None:
    response = app.post("/", NAN, headers={"Content-Type": "text/plain"})
    assert 200 == response.status_int
    assert NAN == response.text
