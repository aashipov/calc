from collections.abc import Generator

import pytest
from flask import Flask
from flask.testing import FlaskClient

from src.app import create_calc

SIMPLE_EXPRESSION: str = "2+2"
SIMPLE_EXPRESSION_RESULT: float = 4.0
COMPLEX_EXPRESSION: str = "(-abs(pi*2-e-(32-4)/(23+4/5)-(2-4)*(4+6-98.2)+4))+1.9e2"
COMPLEX_EXPRESSION_RESULT: float = 19.988432890485228


@pytest.fixture
def app() -> Generator[Flask]:
    app: Flask = create_calc(True)
    yield app


@pytest.fixture
def client(app: Flask) -> Generator[FlaskClient]:
    with app.test_client() as test_client:
        yield test_client
