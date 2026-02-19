cmake_minimum_required(VERSION 3.15)

set(project_name calc-poco)

project(${project_name} VERSION 0.1.0)

include(GNUInstallDirs)

include_directories("src")

find_library(Poco_LIBRARIES NAMES PocoFoundation PocoUtil PocoNet)
if(NOT ${Poco_FOUND})
    message(FATAL_ERROR "Poco C++ Libraries not found.")
endif()
include_directories(${Poco_INCLUDE_DIRS})

add_executable(${project_name} app.cpp)

target_link_libraries(${project_name}
    PRIVATE
        PocoFoundation
        PocoNet
        PocoUtil
        c-exprtk-adapter
)

install(TARGETS ${PROJECT_NAME} DESTINATION bin)

add_custom_target(uninstall
    COMMAND ${CMAKE_COMMAND} -E rm -rf ${CMAKE_INSTALL_PREFIX}/bin/${PROJECT_NAME}
)

if (ENABLE_TESTS)
    add_subdirectory(tests)
endif ()
