cmake_minimum_required(VERSION 3.29)
project(Estudos)

set(CMAKE_C_STANDARD 11)

include_directories("Herick - Algo e Prog/N2 - Atividade presença 28")
include_directories("Herick - Algo e Prog/N2 - Revisao")

function(replace_spaces_in_name name result)
    string(REPLACE " " "_" new_name "${name}")
    set(${result} "${new_name}" PARENT_SCOPE)
endfunction()

# Função para adicionar executáveis automaticamente
function(add_executables_from_folder folder)
    file(GLOB SOURCES "${folder}/*.c")
    foreach (SOURCE ${SOURCES})
        get_filename_component(EXECUTABLE_NAME ${SOURCE} NAME_WE)
        replace_spaces_in_name(${EXECUTABLE_NAME} EXECUTABLE_NAME_UNDERSCORE)
        add_executable(${EXECUTABLE_NAME_UNDERSCORE} ${SOURCE})
    endforeach ()
endfunction()

# Adicionar executáveis automaticamente das pastas específicas
add_executables_from_folder("Herick - Algo e Prog/N2 - Atividade presença 28")
add_executables_from_folder("Herick - Algo e Prog/N2 - Revisao")
