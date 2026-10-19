from collections.abc import Generator

import pytest
from sanic import Sanic

from src.app import create_calc

SIMPLE_EXPRESSION: str = "2+2"
SIMPLE_EXPRESSION_RESULT: float = 4.0
COMPLEX_EXPRESSION: str = "(-abs(pi*2-e-(32-4)/(23+4/5)-(2-4)*(4+6-98.2)+4))+1.9e2"
COMPLEX_EXPRESSION_RESULT: float = 19.988432890485228

@pytest.fixture
def app() -> Generator[Sanic]:
    app: Sanic = create_calc("TestCalc", True)
    yield app
