#!/usr/bin/env bash
set -euo pipefail

ROOT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
BUILD_DIR="$ROOT_DIR/build"

cmake -S "$ROOT_DIR" -B "$BUILD_DIR"
cmake --build "$BUILD_DIR"

"$BUILD_DIR/category_browse" \
  --categories "$ROOT_DIR/data/categories.json" \
  --products "$ROOT_DIR/data/products.jsonl" \
  --requests "$ROOT_DIR/data/requests.jsonl" \
  --out "$ROOT_DIR/data/results.json"

echo "Results written to $ROOT_DIR/data/results.json"
