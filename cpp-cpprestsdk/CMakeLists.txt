cmake_minimum_required(VERSION 3.15)

set(project_name calc)

project(${project_name})

include(GNUInstallDirs)
include_directories("src")
add_executable(${project_name} app.cpp)

find_package(cpprestsdk REQUIRED)
find_package(OpenSSL REQUIRED)

target_link_libraries(${project_name}
    PRIVATE
        cpprestsdk::cpprest
        OpenSSL::Crypto
        c-exprtk-adapter
)

install(TARGETS ${PROJECT_NAME} DESTINATION bin)

add_custom_target(uninstall
    COMMAND ${CMAKE_COMMAND} -E rm -rf ${CMAKE_INSTALL_PREFIX}/bin/${PROJECT_NAME}
)

if (ENABLE_TESTS)
    add_subdirectory(tests)
endif ()
