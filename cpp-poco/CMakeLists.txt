cmake_minimum_required(VERSION 3.15)

set(project_name calc)

project(${project_name} VERSION 0.1.0)

include(GNUInstallDirs)

find_package(Poco REQUIRED COMPONENTS Foundation Util Net)

add_executable(${project_name} app.cpp)

target_include_directories(${project_name} PRIVATE "src")

target_link_libraries(${project_name}
    PRIVATE
        Poco::Foundation
        Poco::Net
        Poco::Util
        c-exprtk-adapter
)

install(TARGETS ${PROJECT_NAME} DESTINATION bin)

add_custom_target(uninstall
    COMMAND ${CMAKE_COMMAND} -E rm -rf ${CMAKE_INSTALL_PREFIX}/bin/${PROJECT_NAME}
)

if (ENABLE_TESTS)
    add_subdirectory(tests)
endif ()
