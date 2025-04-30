//
// Created by joaop on 30/09/2024.
//
//5. Leia uma matriz de 3 x 3 elementos.
// Calcule a soma dos elementos que estão na diagonal principal.


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
        soma += matriz[i][i];
    }

    printf("A soma dos elementos da diagonal principal é: %d\n", soma);

    return 0;
}