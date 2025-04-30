//
// Created by joaop on 30/09/2024.
//
//2.Leia uma matriz 5 x 5. Leia também um valor X. O programa devera fazer uma busca desse valor
//na matriz e, ao final, escrever a localização(linha e coluna) ou uma mensagem de “ não encontrado”
#include <stdio.h>




int main(void) {
    int matriz[5][5];
    int valor_x = 0;


    for (int i = 0; i < 5; i++) {
        for (int j = 0; j < 5; j++) {
            printf("Digite o valor da matriz[%d][%d]: ", i, j);
            scanf("%d", &matriz[i][j]);
        }
    }


    printf("Digite o valor de X: ");
    scanf("%d", &valor_x);


    for (int i = 0; i < 5; i++) {
        for (int j = 0; j < 5; j++) {
            if (matriz[i][j] == valor_x) {
                printf("O valor %d foi encontrado na matriz[%d][%d]\n", valor_x, i, j);
            }
            else {
                printf("O valor %d não foi encontrado na matriz\n", valor_x);
            }
        }
    }
    return 0;
}


