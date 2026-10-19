import platform
from ctypes import CDLL, c_char_p, c_double
from typing import Tuple


class ExprAdapter:
    """
    An adapter class to load and interact with the native C libraries
    for expression calculation.
    """

    EXPRTK_LIBRARY_NAME: str = "libc-exprtk-adapter"
    MEVAL_LIBRARY_NAME: str = "libc-meval-adapter"
    NAN: float = float("nan")

    def __init__(self):
        adapters: tuple[CDLL, CDLL] = self._load_libraries()
        self._exprtk_adapter: CDLL = adapters[0]
        self._meval_adapter: CDLL = adapters[1]
        self._setup_signatures()

    def _is_windows(self) -> bool:
        """Detects if the operating system is Windows."""
        return platform.system().lower() == "win"

    def _load_libraries(self) -> tuple[CDLL, CDLL]:
        """Loads the appropriate C library (.so or .dll)."""
        exprtk_library_full_name: str = self.EXPRTK_LIBRARY_NAME
        meval_library_full_name: str = self.MEVAL_LIBRARY_NAME
        if self._is_windows():
            exprtk_library_full_name += ".dll"
            meval_library_full_name += ".dll"
        else:
            exprtk_library_full_name += ".so"
            meval_library_full_name += ".so"

        try:
            return (CDLL(exprtk_library_full_name), CDLL(meval_library_full_name))
        except Exception as e:
            # Specific error handling for missing library files
            raise FileNotFoundError(
                f"Failed to load native libraries. Check if files exist. Error: {e}"
            )

    def _setup_signatures(self):
        """Sets up the function signature for the native functions."""
        self._exprtk_adapter.calculate_via_exprtk.argtypes = [c_char_p]
        self._exprtk_adapter.calculate_via_exprtk.restype = c_double
        self._meval_adapter.calculate_via_meval.argtypes = [c_char_p]
        self._meval_adapter.calculate_via_meval.restype = c_double

    def via_exprtk(self, expression: str) -> float:
        """
        Calculates the result of an expression string by calling the exprtk adapter.
        Returns "NaN" if a critical error occurs.
        """
        if not expression:
            return self.NAN

        try:
            # 1. Encode the expression to UTF-8 bytes for C function input
            encoded_bytes = expression.encode("utf-8")
            ptr: c_char_p = c_char_p(encoded_bytes)

            # 2. Call the native function
            res_double: c_double = self._exprtk_adapter.calculate_via_exprtk(ptr)

            # 3. Convert result to float
            return float(res_double)

        except Exception:
            return self.NAN

    def via_meval(self, expression: str) -> float:
        """
        Calculates the result of an expression string by calling the meval adapter.
        Returns "NaN" if a critical error occurs.
        """
        if not expression:
            return self.NAN

        try:
            # 1. Encode the expression to UTF-8 bytes for C function input
            encoded_bytes = expression.encode("utf-8")
            ptr: c_char_p = c_char_p(encoded_bytes)

            # 2. Call the native function
            res_double: c_double = self._meval_adapter.calculate_via_meval(ptr)

            # 3. Convert result to float
            return float(res_double)

        except Exception:
            return self.NAN
