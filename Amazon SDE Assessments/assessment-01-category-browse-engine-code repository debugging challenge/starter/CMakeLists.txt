cmake_minimum_required(VERSION 3.16)
project(category_browse_engine LANGUAGES CXX)

set(CMAKE_CXX_STANDARD 17)
set(CMAKE_CXX_STANDARD_REQUIRED ON)
set(CMAKE_CXX_EXTENSIONS OFF)

add_library(browse_core
    src/browse/parser.cpp
    src/browse/engine.cpp
    src/browse/file_io.cpp
)
target_include_directories(browse_core PUBLIC include)

add_executable(category_browse src/browse/main.cpp)
target_link_libraries(category_browse PRIVATE browse_core)

add_executable(engine_tests tests/engine_test.cpp)
target_link_libraries(engine_tests PRIVATE browse_core)

enable_testing()
add_test(NAME engine_tests COMMAND engine_tests)
