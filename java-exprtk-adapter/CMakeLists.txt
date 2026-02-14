cmake_minimum_required(VERSION 3.15)

set(project_name java-exprtk-adapter)

project(${project_name} VERSION 0.1.0)

include(GNUInstallDirs)

add_library(${project_name} SHARED java-exprtk-adapter.cpp)

set_target_properties(${project_name} PROPERTIES
    VERSION ${PROJECT_VERSION}
    PUBLIC_HEADER org_dummy_calc_ExprtkAdapter.h
)

install(TARGETS ${PROJECT_NAME}
    LIBRARY DESTINATION ${CMAKE_INSTALL_LIBDIR}
    PUBLIC_HEADER DESTINATION ${CMAKE_INSTALL_INCLUDEDIR}
)
