from collections.abc import Generator

import pytest
from tornado.httpclient import HTTPClient, HTTPResponse

from src.app import NAN, WELCOME
from tests.conf import app

SIMPLE_EXPRESSION: str = "2+2"
SIMPLE_EXPRESSION_RESULT: str = "4.0"
COMPLEX_EXPRESSION: str = "(-abs(pi*2-e-(32-4)/(23+4/5)-(2-4)*(4+6-98.2)+4))+1.9e2"
COMPLEX_EXPRESSION_RESULT: str = "19.988432890485228"


@pytest.mark.gen_test
def test_welcome(
    http_client: HTTPClient, base_url: str
) -> Generator[HTTPResponse, HTTPResponse, None]:
    response: HTTPResponse = yield http_client.fetch(base_url)
    assert 200 == response.code
    assert WELCOME == response.body.decode()


@pytest.mark.gen_test
def test_simple_expression(
    http_client: HTTPClient, base_url: str
) -> Generator[HTTPResponse, HTTPResponse, None]:
    response: HTTPResponse = yield http_client.fetch(
        base_url,
        method="POST",
        body=SIMPLE_EXPRESSION,
        headers={"Content-Type": "text/plain"},
    )
    assert 200 == response.code
    assert SIMPLE_EXPRESSION_RESULT == response.body.decode()


@pytest.mark.gen_test
def test_complex_expression(
    http_client: HTTPClient, base_url: str
) -> Generator[HTTPResponse, HTTPResponse, None]:
    response: HTTPResponse = yield http_client.fetch(
        base_url,
        method="POST",
        body=COMPLEX_EXPRESSION,
        headers={"Content-Type": "text/plain"},
    )
    assert 200 == response.code
    assert COMPLEX_EXPRESSION_RESULT == response.body.decode()


@pytest.mark.gen_test
def test_invalid_expression(
    http_client: HTTPClient, base_url: str
) -> Generator[HTTPResponse, HTTPResponse, None]:
    response: HTTPResponse = yield http_client.fetch(
        base_url,
        method="POST",
        body=NAN,
        headers={"Content-Type": "text/plain"},
    )
    assert 200 == response.code
    assert NAN == response.body.decode()
