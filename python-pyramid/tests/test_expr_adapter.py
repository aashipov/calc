import math

import pytest

from src.expr_adapter import ExprAdapter
from tests.conf import (
    COMPLEX_EXPRESSION,
    COMPLEX_EXPRESSION_RESULT,
    SIMPLE_EXPRESSION,
    SIMPLE_EXPRESSION_RESULT,
)

ADAPTER = ExprAdapter()

FUNC_MAP = {"via_meval": ADAPTER.via_meval, "via_exprtk": ADAPTER.via_exprtk}


@pytest.mark.parametrize(
    "func_name, expr, expected",
    [
        pytest.param(
            "via_exprtk",
            SIMPLE_EXPRESSION,
            SIMPLE_EXPRESSION_RESULT,
            id="simple_expression_exprtk",
        ),
        pytest.param(
            "via_exprtk",
            COMPLEX_EXPRESSION,
            COMPLEX_EXPRESSION_RESULT,
            id="complex_expression_exprtk",
        ),
        pytest.param(
            "via_exprtk",
            "",
            ExprAdapter.NAN,
            id="blank_exprtk",
        ),
        pytest.param(
            "via_exprtk",
            "   \n\r\t  ",
            ExprAdapter.NAN,
            id="whitespace_exprtk",
        ),
        pytest.param(
            "via_meval",
            SIMPLE_EXPRESSION,
            SIMPLE_EXPRESSION_RESULT,
            id="simple_expression_meval",
        ),
        pytest.param(
            "via_meval",
            COMPLEX_EXPRESSION,
            COMPLEX_EXPRESSION_RESULT,
            id="complex_expression_meval",
        ),
        pytest.param(
            "via_meval",
            "",
            ExprAdapter.NAN,
            id="blank_meval",
        ),
        pytest.param(
            "via_meval",
            "   \n\r\t  ",
            ExprAdapter.NAN,
            id="whitespace_meval",
        ),
    ],
)
def test_expr_adapter(func_name: str, expr: str, expected: float):
    assert FUNC_MAP[func_name](expr) == pytest.approx(expected, nan_ok=True)
