# C exprtk adapter

Refer to [README.md](./README.md) for human-facing project details.

## Core Operational Mantra

Prioritize structural safety over quick workarounds, and never introduce blind placeholders or incomplete logic. Do not rewrite or refactor structural boundaries or utility functions unless explicitly instructed. Adapt your changes to fit the existing patterns of the codebase.

### Dependency: exprtk

The only third-party dependency is [exprtk](https://github.com/ArashPartow/exprtk), a header-only C++ library. It is **not** vendored. The build scripts download `exprtk.hpp` into `${HOME}/.local/include/` on first run (`download_exprtk`). The public `README.md` documents fetching it manually. There is no package manager or CMake `FetchContent`; the header is expected to sit in a standard include path.

## Core Library: `c-exprtk-adapter`

A shared library exposing a single C-callable entry point so that non-C++ consumers (e.g. the calculator, or future language bindings) can evaluate algebraic expressions via exprtk.
