//
// Created by joaop on 30/09/2024.
//
//4. Leia uma matriz de 3 x 3 elementos.
// Calcule a soma dos elementos que estão abaixo da diagonal principal.

#include <stdio.h>

int main() {
    int matriz[3][3];
    int soma = 0;

    for (int i = 0; i < 3; i++) {
        for (int j = 0; j < 3; j++) {
            printf("Digite o valor da matriz[%d][%d]: ", i, j);
            scanf("%d", &matriz[i][j]);
        }
    }

    for (int i = 0; i < 3; i++) {
        for (int j = 0; j < 3; j++) {
            if (i > j) {
                soma += matriz[i][j];
            }
        }
    }

    printf("A soma dos elementos abaixo da diagonal principal é: %d\n", soma);

    return 0;
}