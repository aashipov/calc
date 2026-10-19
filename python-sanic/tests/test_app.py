import pytest
from sanic import Sanic

from src.app import MEVAL, NAN, WELCOME
from tests.conf import (
    COMPLEX_EXPRESSION,
    COMPLEX_EXPRESSION_RESULT,
    SIMPLE_EXPRESSION,
    SIMPLE_EXPRESSION_RESULT,
    app,
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
def test_app(method: str, path: str, payload: float, expected, app: Sanic):
    if POST == method:
        request, response = app.test_client.post(path, data=payload)
        assert response.status == 200
        assert response.text == str(expected)
    else:
        request, response = app.test_client.get(path)
        assert 200 == response.status
        assert response.text == expected
