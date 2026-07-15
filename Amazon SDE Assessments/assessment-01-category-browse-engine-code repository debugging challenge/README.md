# Category Browse Engine Package

This archive contains a complete C++ project for the category-browse debugging exercise.

## Contents

- `docs/Category_Browse_Engine_Exercise.pdf` — problem statement and reference fixes.
- `starter/` — intentionally broken project used for the exercise.
- `solution/` — corrected implementation that passes the included tests.

Both project folders are self-contained and use the same structure:

```text
include/browse/       Public headers
src/browse/           Implementation and CLI entry point
tests/                Test executable
data/                 Categories, products, and requests
CMakeLists.txt         CMake project
run.sh                 Build and run the CLI
test.sh                Build and run tests
```

## Requirements

- CMake 3.16 or later
- A compiler with C++17 support

## Run

```bash
cd starter
./test.sh
```

The starter tests are expected to fail. To verify the completed implementation:

```bash
cd solution
./test.sh
./run.sh
```
