import platform
from ctypes import CDLL, c_char_p, c_double

LIBRARY_NAME: str = "libc-exprtk-adapter"
DOT_SO: str = ".so"
DOT_DLL: str = ".dll"
NAN: str = "NaN"


def get_c_exprtk_adapter():
    os_name = platform.system().lower()
    library_full_name: str = LIBRARY_NAME
    if os_name.__contains__("win"):
        library_full_name += DOT_DLL
    else:
        library_full_name += DOT_SO
    adapter = CDLL(library_full_name)
    adapter.calculate.argtypes = [c_char_p]
    adapter.calculate.restype = c_double
    return adapter


ADAPTER = get_c_exprtk_adapter()


def calculate_via_exprtk(expression: str) -> str:
    res: str = NAN
    try:
        ptr: c_char_p = c_char_p(expression.encode("utf-8"))
        res_double: c_double = ADAPTER.calculate(ptr)
        res = str(res_double)
    except:
        pass
    return res
