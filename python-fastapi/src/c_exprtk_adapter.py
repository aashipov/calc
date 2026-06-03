import platform
from ctypes import CDLL, c_char_p, c_double

LIBRARY_NAME: str = "libc-exprtk-adapter"
DOT_SO: str = ".so"
DOT_DLL: str = ".dll"
NAN: str = "NaN"

# Detect platform type
def is_windows() -> bool:
    return platform.system().lower() == "win"

def get_c_exprtk_adapter():
    library_full_name: str = LIBRARY_NAME
    if is_windows():
        library_full_name += DOT_DLL
    else:
        library_full_name += DOT_SO
    adapter: CDLL = CDLL(library_full_name)
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
