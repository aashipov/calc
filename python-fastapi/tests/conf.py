from typing import Generator

import pytest
from fastapi.testclient import TestClient

from src import py_exprtk_adapter
from src.app import create_calc

SIMPLE_EXPRESSION: str = "2+2"
SIMPLE_EXPRESSION_RESULT: float = 4.0
COMPLEX_EXPRESSION: str = "(-abs(pi*2-e-(32-4)/(23+4/5)-(2-4)*(4+6-98.2)+4))+1.9e2"
COMPLEX_EXPRESSION_RESULT: float = 19.988432890485228
NAN: str = str(py_exprtk_adapter.PyExprtkAdapter.NAN)


@pytest.fixture
def test_client() -> Generator[TestClient]:
    with TestClient(create_calc()) as test_client:
        yield test_client
