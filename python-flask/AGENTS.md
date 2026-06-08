# Calculator service: Flask flavor

Relies upon `../c-exprtk-adapter` to perform algebraic computation

## Project Structure

- `src/` - Contains all the source code
- `src/c_exprtk_adapter.py` - a `CDDL` adapter to `libc-exprtk-adapter` (see `../c-exprtk-adapter`)
- `src/app.py` - routes, main entry point (single file for the sake of brevity)
- `tests/` - Integration tests
- `build.sh` - Sets venv (./.venv/) up, builds a wheel & runs integration tests
- `run.sh` - to launch the app, perform load tests in a separate process and more

## Code Standards

* **Strict Typing**: Enforce Python type hints (PEP 484) on all function arguments and return signatures.
