cmake_minimum_required(VERSION 3.15)

set(project_name calc-drogon)

project(${project_name})

add_executable(${project_name} app.cpp)

find_package(Drogon REQUIRED)
find_package(Trantor REQUIRED)

target_link_libraries(${project_name}
        PRIVATE Drogon::Drogon
        PRIVATE Trantor::Trantor
        c-exprtk-adapter
)

install(TARGETS ${PROJECT_NAME} DESTINATION bin)

add_custom_target(uninstall
    COMMAND ${CMAKE_COMMAND} -E rm -rf ${CMAKE_INSTALL_PREFIX}/bin/${PROJECT_NAME}
)