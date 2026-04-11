import pytest
from tornado.web import Application

from src.app import create_calc


@pytest.fixture
def app() -> Application:
    return create_calc()
