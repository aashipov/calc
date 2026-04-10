from collections.abc import Generator

import pytest
from sanic import Sanic

from src.app import create_calc


@pytest.fixture
def app() -> Generator[Sanic]:
    app: Sanic = create_calc("TestCalc", True)
    yield app
