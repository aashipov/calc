import pytest
from webtest import TestApp, TestResponse

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
def test_app(method: str, path: str, payload: float, expected, app: TestApp):
    if POST == method:
        response: TestResponse = app.post(path, str(payload), headers={"Content-Type": "text/plain"}
        )
        assert response.status_code == 200
        assert response.text == str(expected)
    else:
        response: TestResponse = app.get(path)
        assert 200 == response.status_code
        assert response.text == expected
