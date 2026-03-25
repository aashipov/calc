cmake_minimum_required(VERSION 3.15)

set(project_name calc-cpprestsdk-test)

project(${project_name} VERSION 0.1.0)

include(GNUInstallDirs)

find_package(GTest REQUIRED)
find_package(cpprestsdk REQUIRED)
find_package(OpenSSL REQUIRED)
include_directories(${GTEST_INCLUDE_DIRS})

enable_testing()

add_executable(${project_name} CalcTest.cpp)
target_link_libraries(${project_name}
    PRIVATE
        GTest::gtest_main
        cpprestsdk::cpprest
        OpenSSL::Crypto
        c-exprtk-adapter
)

gtest_discover_tests(${project_name})
