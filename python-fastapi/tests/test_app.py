from fastapi.testclient import TestClient
from httpx import Response

from src.app import WELCOME
from tests.conf import (
    COMPLEX_EXPRESSION,
    COMPLEX_EXPRESSION_RESULT,
    NAN,
    SIMPLE_EXPRESSION,
    SIMPLE_EXPRESSION_RESULT,
    test_client,
)


def test_welcome(test_client: TestClient):
    response: Response = test_client.get("/")
    assert 200 == response.status_code
    assert WELCOME == response.text


def test_simple_expression(test_client: TestClient):
    response: Response = test_client.post(
        url="/", content=SIMPLE_EXPRESSION, headers={"Content-Type": "text/plain"}
    )
    assert 200 == response.status_code
    assert str(SIMPLE_EXPRESSION_RESULT) == response.text


def test_complex_expression(test_client: TestClient):
    response: Response = test_client.post(
        url="/", content=COMPLEX_EXPRESSION, headers={"Content-Type": "text/plain"}
    )
    assert 200 == response.status_code
    assert str(COMPLEX_EXPRESSION_RESULT) == response.text


def test_invalid_expression(test_client: TestClient):
    response: Response = test_client.post(
        url="/", content=NAN, headers={"Content-Type": "text/plain"}
    )
    assert 200 == response.status_code
    assert NAN == response.text
