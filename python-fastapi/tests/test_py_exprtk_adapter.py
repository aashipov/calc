import math

from src.py_exprtk_adapter import PyExprtkAdapter
from tests.conf import (
    COMPLEX_EXPRESSION,
    COMPLEX_EXPRESSION_RESULT,
    SIMPLE_EXPRESSION,
    SIMPLE_EXPRESSION_RESULT,
)

ADAPTER = PyExprtkAdapter()


def test_simple_expression():
    """Tests calculation of a basic, known expression."""
    res: float = ADAPTER.calculate(SIMPLE_EXPRESSION)
    assert math.isclose(res, SIMPLE_EXPRESSION_RESULT)


def test_complex_expression():
    """Tests calculation of a complex, known expression."""
    res: float = ADAPTER.calculate(COMPLEX_EXPRESSION)
    assert math.isclose(res, COMPLEX_EXPRESSION_RESULT)


def test_nan_input_string():
    """Tests if an explicit string 'nan' (or equivalent) is handled correctly."""
    res: float = ADAPTER.calculate("nan")
    assert math.isnan(res)


def test_blank_string():
    """Tests handling of empty strings and strings containing only whitespace."""
    res_whitespace: float = ADAPTER.calculate("\r\n\t")
    res_empty: float = ADAPTER.calculate("")
    assert math.isnan(res_whitespace)
    assert math.isnan(res_empty)


def test_negative_numbers():
    """Tests expressions involving negative numbers."""
    negative_expr = "-10 + 5"
    expected_result = -5.0
    res: float = ADAPTER.calculate(negative_expr)
    assert math.isclose(res, expected_result)
