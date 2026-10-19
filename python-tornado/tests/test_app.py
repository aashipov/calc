from collections.abc import Generator

import pytest
from tornado.httpclient import HTTPClient, HTTPResponse

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


@pytest.mark.gen_test
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
def test_app(
    method: str,
    path: str,
    payload: float,
    expected,
    http_client: HTTPClient,
    base_url: str,
) -> Generator[HTTPResponse, HTTPResponse, None]:
    if POST == method:
        response: HTTPResponse = yield http_client.fetch(
            base_url + path,
            method="POST",
            body=payload,
            headers={"Content-Type": "text/plain"},
        )
        assert 200 == response.code
        assert str(expected) == response.body.decode()
    else:
        response: HTTPResponse = yield http_client.fetch(base_url + path)
        assert 200 == response.code
        assert response.body.decode() == expected
