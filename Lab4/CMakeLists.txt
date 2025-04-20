cmake_minimum_required(VERSION 3.30)
project(Lab4)

set(CMAKE_CXX_STANDARD 20)

add_executable(Lab4 main.cpp
        utils.cpp
)
find_package(OpenMP REQUIRED)
if (OpenMP_CXX_FOUND)
    target_link_libraries(Lab4 PUBLIC OpenMP::OpenMP_CXX)
endif ()