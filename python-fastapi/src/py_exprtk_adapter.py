import platform
from ctypes import CDLL, c_char_p, c_double


class PyExprtkAdapter:
    """
    A wrapper class to load and interact with the native C library
    for expression calculation.
    """

    LIBRARY_NAME: str = "libc-exprtk-adapter"
    NAN: float = float("nan")

    def __init__(self):
        self._adapter: CDLL = self._load_library()
        self._setup_signatures()

    def _is_windows(self) -> bool:
        """Detects if the operating system is Windows."""
        return platform.system().lower() == "win"

    def _load_library(self) -> CDLL:
        """Loads the appropriate C library (.so or .dll)."""
        library_full_name: str = self.LIBRARY_NAME
        if self._is_windows():
            library_full_name += ".dll"
        else:
            library_full_name += ".so"

        try:
            adapter = CDLL(library_full_name)
            return adapter
        except Exception as e:
            # Specific error handling for missing library files
            raise FileNotFoundError(
                f"Failed to load native library '{library_full_name}'. Check if the file exists. Error: {e}"
            )

    def _setup_signatures(self):
        """Sets up the function signature for the native 'calculate' function."""
        self._adapter.calculate.argtypes = [c_char_p]
        self._adapter.calculate.restype = c_double

    def calculate(self, expression: str) -> float:
        """
        Calculates the result of an expression string by calling the C library adapter.
        Returns "NaN" if a critical error occurs.
        """
        if not expression:
            return self.NAN

        try:
            # 1. Encode the expression to UTF-8 bytes for C function input
            encoded_bytes = expression.encode("utf-8")
            ptr: c_char_p = c_char_p(encoded_bytes)

            # 2. Call the native function
            res_double: c_double = self._adapter.calculate(ptr)

            # 3. Convert result to float
            return float(res_double)

        except Exception:
            return self.NAN
