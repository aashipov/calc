from fastapi.testclient import TestClient
from httpx import Response

from src.app import NAN, WELCOME
from tests.conf import test_client

SIMPLE_EXPRESSION: str = "2+2"
SIMPLE_EXPRESSION_RESULT: str = "4.0"
COMPLEX_EXPRESSION: str = "(-abs(pi*2-e-(32-4)/(23+4/5)-(2-4)*(4+6-98.2)+4))+1.9e2"
COMPLEX_EXPRESSION_RESULT: str = "19.988432890485228"


def test_welcome(test_client: TestClient):
    response: Response = test_client.get("/")
    assert 200 == response.status_code
    assert WELCOME == response.text


def test_simple_expression(test_client: TestClient):
    response: Response = test_client.post(
        url="/", content=SIMPLE_EXPRESSION, headers={"Content-Type": "text/plain"}
    )
    assert 200 == response.status_code
    assert SIMPLE_EXPRESSION_RESULT == response.text


def test_complex_expression(test_client: TestClient):
    response: Response = test_client.post(
        url="/", content=COMPLEX_EXPRESSION, headers={"Content-Type": "text/plain"}
    )
    assert 200 == response.status_code
    assert COMPLEX_EXPRESSION_RESULT == response.text


def test_invalid_expression(test_client: TestClient):
    response: Response = test_client.post(
        url="/", content=NAN, headers={"Content-Type": "text/plain"}
    )
    assert 200 == response.status_code
    assert NAN.lower() == response.text.lower()
