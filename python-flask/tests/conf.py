from collections.abc import Generator
from flask.testing import FlaskClient
import pytest
from flask import Flask

from calc.calc import create_calc


@pytest.fixture
def app():
    app: Flask = create_calc(True)
    yield app


@pytest.fixture
def client(app: Flask) -> Generator[FlaskClient]:
    with app.test_client() as test_client:
        yield test_client
