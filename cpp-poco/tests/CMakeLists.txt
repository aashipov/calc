cmake_minimum_required(VERSION 3.15)

set(project_name calc-poco-test)

project(${project_name} VERSION 0.1.0)

include(GNUInstallDirs)

find_library(Poco_LIBRARIES NAMES PocoFoundation PocoUtil PocoNet)
include_directories(${Poco_INCLUDE_DIRS})

find_package(GTest REQUIRED)
include_directories(${GTEST_INCLUDE_DIRS})

enable_testing()

add_executable(${project_name} CalcTest.cpp)
target_link_libraries(${project_name}
    PRIVATE
        PocoFoundation
        PocoNet
        PocoUtil
        c-exprtk-adapter
        GTest::gtest_main
)

gtest_discover_tests(${project_name})
