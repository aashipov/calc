import platform
from ctypes import CDLL, c_char_p, c_double

LIBRARY_NAME: str = "libc-exprtk-adapter"
DOT_SO: str = ".so"
DOT_DLL: str = ".dll"
NAN: str = "NaN"


def is_windows() -> bool:
    """Detects if the operating system is Windows."""
    return platform.system().lower() == "win"


def get_c_exprtk_adapter() -> CDLL:
    """Loads the appropriate C library (.so or .dll) and sets up the function signature."""
    library_full_name: str = LIBRARY_NAME
    if is_windows():
        library_full_name += DOT_DLL
    else:
        library_full_name += DOT_SO
    adapter: CDLL = CDLL(library_full_name)
    adapter.calculate.argtypes = [c_char_p]
    adapter.calculate.restype = c_double
    return adapter


ADAPTER: CDLL = get_c_exprtk_adapter()


def calculate_via_exprtk(expression: str) -> str:
    """
    Calculates the result of an expression string by calling the C library adapter.
    Returns "NaN" if an error occurs during calculation (e.g., encoding or ctypes failure).
    """
    try:
        # Encode the expression to UTF-8 bytes for C function input
        ptr: c_char_p = c_char_p(expression.encode("utf-8"))
        res_double: c_double = ADAPTER.calculate(ptr)
        return str(res_double)
    except:
        return NAN
