# C exprtk adapter

Refer to [README.md](./README.md) for human-facing project details.

## Core Operational Mantra

Prioritize structural safety over quick workarounds, and never introduce blind placeholders or incomplete logic. Do not rewrite or refactor structural boundaries or utility functions unless explicitly instructed. Adapt your changes to fit the existing patterns of the codebase.

## Project Layout

```
.
├── AGENTS.md                     # This file
├── README.md                     # Human-facing: exprtk download + build steps
├── c-exprtk-adapter.h            # Public C API
├── c-exprtk-adapter.cpp          # C++ implementation + C wrapper
├── CMakeLists.txt                # Library target + optional calculator subdir
├── debug.sh / release.sh         # Self-contained build+install+smoke-test scripts
├── exprtk-calculator/            # Optional CLI executable (guarded by ENABLE_CALCULATOR)
│   ├── CMakeLists.txt
│   └── main.cpp
├── Debug/  Release/             # Out-of-source build dirs produced by the scripts
└── .gitignore
```

### Dependency: exprtk

The only third-party dependency is [exprtk](https://github.com/ArashPartow/exprtk), a header-only C++ library. It is **not** vendored. The build scripts download `exprtk.hpp` into `${HOME}/.local/include/` on first run (`download_exprtk`). The public `README.md` documents fetching it manually. There is no package manager or CMake `FetchContent`; the header is expected to sit in a standard include path.

## Core Library: `c-exprtk-adapter`

A shared library exposing a single C-callable entry point so that non-C++ consumers (e.g. the calculator, or future language bindings) can evaluate algebraic expressions via exprtk.

## Local Conventions

- Keep `build_*` intact unless explicitly asked to change them — they are the structural boundary referenced in the mantra above.
- `Debug/` and `Release/` are build artifacts and are git-ignored.
