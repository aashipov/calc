cmake_minimum_required(VERSION 3.15)

set(project_name exprtk-calculator)

project(${project_name})

include(GNUInstallDirs)

add_executable(${project_name} main.cpp)
target_link_libraries(${project_name}
    PUBLIC
        c-exprtk-adapter
)
target_include_directories(${project_name} PRIVATE "${CMAKE_CURRENT_SOURCE_DIR}/..")

install(TARGETS ${project_name} DESTINATION ${CMAKE_INSTALL_BINDIR})
