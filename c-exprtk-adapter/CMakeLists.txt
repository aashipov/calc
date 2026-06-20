cmake_minimum_required(VERSION 3.15)

set(project_name c-exprtk-adapter)

project(${project_name} VERSION 0.1.0)

include(GNUInstallDirs)

add_library(${project_name} SHARED ${project_name}.cpp)

set_target_properties(${project_name} PROPERTIES
    VERSION ${PROJECT_VERSION}
    PUBLIC_HEADER ${project_name}.h
)

install(TARGETS ${PROJECT_NAME}
    LIBRARY DESTINATION ${CMAKE_INSTALL_LIBDIR}
    PUBLIC_HEADER DESTINATION ${CMAKE_INSTALL_INCLUDEDIR}
)

if (ENABLE_CALCULATOR)
    add_subdirectory(exprtk-calculator)
endif ()
