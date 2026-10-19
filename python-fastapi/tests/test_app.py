import pytest
from fastapi.testclient import TestClient
from httpx import Response

from src.app import MEVAL, WELCOME
from tests.conf import (
    COMPLEX_EXPRESSION,
    COMPLEX_EXPRESSION_RESULT,
    NAN,
    SIMPLE_EXPRESSION,
    SIMPLE_EXPRESSION_RESULT,
    test_client,
)

GET: str = "GET"
POST: str = "POST"


@pytest.mark.parametrize(
    "method, path, payload, expected",
    [
        pytest.param(
            POST,
            "/",
            SIMPLE_EXPRESSION,
            SIMPLE_EXPRESSION_RESULT,
            id="simple_expression_exprtk",
        ),
        pytest.param(
            POST,
            "/",
            COMPLEX_EXPRESSION,
            COMPLEX_EXPRESSION_RESULT,
            id="complex_expression_exprtk",
        ),
        pytest.param(
            POST,
            "/",
            "   \n\r\t  ",
            NAN,
            id="whitespace_exprtk",
        ),
        pytest.param(
            POST,
            "/" + MEVAL,
            SIMPLE_EXPRESSION,
            SIMPLE_EXPRESSION_RESULT,
            id="simple_expression_meval",
        ),
        pytest.param(
            POST,
            "/" + MEVAL,
            COMPLEX_EXPRESSION,
            COMPLEX_EXPRESSION_RESULT,
            id="complex_expression_meval",
        ),
        pytest.param(
            POST,
            "/" + MEVAL,
            "   \n\r\t  ",
            NAN,
            id="whitespace_meval",
        ),
        pytest.param(GET, "/", "", WELCOME, id="welcome"),
    ],
)
def test_app(method: str, path: str, payload: float, expected, test_client: TestClient):
    if POST == method:
        response: Response = test_client.post(
            url=path, content=str(payload), headers={"Content-Type": "text/plain"}
        )
        assert response.status_code == 200
        assert response.text == str(expected)
    else:
        response: Response = test_client.get(path)
        assert 200 == response.status_code
        assert response.text == expected
