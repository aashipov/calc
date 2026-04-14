import pytest
from webtest import TestApp

from src.app import create_calc


@pytest.fixture
def app() -> TestApp:
    return TestApp(create_calc())
