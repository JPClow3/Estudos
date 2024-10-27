cmake_minimum_required(VERSION 3.29)
project(Estudos)

set(CMAKE_C_STANDARD 11)  # You can change this to 14 if necessary

include_directories("Herick - Algo e Prog/N2 - Atividade presença 28")
include_directories("Herick - Algo e Prog/N2 - Revisao")

# Function to replace spaces with underscores in target names
function(replace_spaces_in_name name result)
    string(REPLACE " " "_" new_name "${name}")
    set(${result} "${new_name}" PARENT_SCOPE)
endfunction()

# List of executables for N2 - Atividade presença 28
file(GLOB ATIVIDADE_SOURCES "Herick - Algo e Prog/N2 - Atividade presença 28/*.c")
foreach (SOURCE ${ATIVIDADE_SOURCES})
    get_filename_component(EXECUTABLE_NAME ${SOURCE} NAME_WE)  # Get file name without extension
    replace_spaces_in_name(${EXECUTABLE_NAME} EXECUTABLE_NAME_UNDERSCORE)
    add_executable(${EXECUTABLE_NAME_UNDERSCORE} ${SOURCE}
            "Herick - Algo e Prog/N2 - Revisao/Matriz Numeros diagonais.c")
endforeach ()

# List of executables for N2 - Revisao
file(GLOB REVISAO_SOURCES "Herick - Algo e Prog/N2 - Revisao/*.c")
foreach (SOURCE ${REVISAO_SOURCES})
    get_filename_component(EXECUTABLE_NAME ${SOURCE} NAME_WE)  # Get file name without extension
    replace_spaces_in_name(${EXECUTABLE_NAME} EXECUTABLE_NAME_UNDERSCORE)
    add_executable(${EXECUTABLE_NAME_UNDERSCORE} ${SOURCE}
            "Herick - Algo e Prog/N2 - Revisao/Matriz Numeros diagonais.c")
endforeach ()
