from typing import Generator

import pytest
from fastapi.testclient import TestClient

from calc.calc import create_calc


@pytest.fixture
def test_client() -> Generator[TestClient]:
    with TestClient(create_calc()) as test_client:
        yield test_client
