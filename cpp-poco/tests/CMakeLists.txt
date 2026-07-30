cmake_minimum_required(VERSION 3.15)

set(project_name calc-poco-test)

project(${project_name} VERSION 0.1.0)

include(GNUInstallDirs)

find_package(Poco REQUIRED COMPONENTS Foundation Util Net)

find_package(GTest REQUIRED)

enable_testing()

add_executable(${project_name} CalcTest.cpp)
target_include_directories(${project_name} PRIVATE "${CMAKE_SOURCE_DIR}/src")
target_link_libraries(${project_name}
    PRIVATE
        Poco::Foundation
        Poco::Net
        Poco::Util
        c-exprtk-adapter
        GTest::gtest_main
)

gtest_discover_tests(${project_name})
