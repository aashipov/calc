cmake_minimum_required(VERSION 3.15)

set(project_name calc-oatpp)

project(${project_name})

find_package(oatpp          1.3.0 REQUIRED)
find_package(oatpp-swagger  1.3.0 REQUIRED)
add_definitions(-DOATPP_SWAGGER_RES_PATH="${OATPP_BASE_DIR}/bin/oatpp-swagger/res")
add_library(${project_name}-lib
        src/AppComponent.hpp
        src/SwaggerComponent.hpp
        src/CalcController.hpp
        src/Runner.cpp
        src/Runner.hpp
)
target_include_directories(${project_name}-lib PUBLIC src)
target_link_libraries(${project_name}-lib
        PUBLIC oatpp::oatpp
        PUBLIC oatpp::oatpp-swagger
        c-exprtk-adapter
)

add_executable(${project_name} app.cpp)
target_link_libraries(${project_name} ${project_name}-lib)
