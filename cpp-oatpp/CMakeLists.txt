cmake_minimum_required(VERSION 3.15)

set(project_name calc-oatpp)

project(${project_name})

add_library(${project_name}-lib
        src/calc/AppComponent.hpp
        src/calc/SwaggerComponent.hpp
        src/calc/controller/CalcController.hpp
        src/calc/Runner.cpp
        src/calc/Runner.hpp
)

## include directories

target_include_directories(${project_name}-lib PUBLIC src)


## link libs

find_package(oatpp          1.3.0 REQUIRED)
find_package(oatpp-swagger  1.3.0 REQUIRED)

target_link_libraries(${project_name}-lib
        PUBLIC oatpp::oatpp
        PUBLIC oatpp::oatpp-swagger
)

## define path to swagger-ui res folder

add_definitions(-DOATPP_SWAGGER_RES_PATH="${OATPP_BASE_DIR}/bin/oatpp-swagger/res")


## add executables

add_executable(${project_name} src/calc/App.cpp)
target_link_libraries(${project_name} ${project_name}-lib c-exprtk-adapter)
