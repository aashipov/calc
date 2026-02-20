cmake_minimum_required(VERSION 3.15)

set(project_name calc-Crow-test)

project(${project_name} VERSION 0.1.0)

include(GNUInstallDirs)

find_package(OpenSSL REQUIRED)
find_package(ZLIB REQUIRED)
find_package(Crow REQUIRED)
find_package(GTest REQUIRED)
include_directories(${GTEST_INCLUDE_DIRS})

enable_testing()

add_executable(${project_name} CalcTest.cpp)
target_link_libraries(${project_name}
    PRIVATE
        OpenSSL::SSL
        OpenSSL::Crypto
        ZLIB::ZLIB
        Crow::Crow
        c-exprtk-adapter
        GTest::gtest_main
)

gtest_discover_tests(${project_name})
