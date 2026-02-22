cmake_minimum_required(VERSION 3.15)

set(project_name exprtk-calculator)

project(${project_name})

include(GNUInstallDirs)

add_executable(${project_name} main.cpp)
target_link_libraries(${project_name}
    PUBLIC
        c-exprtk-adapter
)

install(TARGETS ${PROJECT_NAME} DESTINATION ${CMAKE_INSTALL_BINDIR})
