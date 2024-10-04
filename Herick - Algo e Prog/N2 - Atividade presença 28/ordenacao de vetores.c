//
// Created by joaop on 04/10/2024.
//
//Em C, utilizando o metodo bolha ordene de forma decrescente 10 inseridos pelo usuário.

#include <stdio.h>

int main() {
    int vetor[10];
    int i, j, aux;

    for (i = 0; i < 10; i++) {
        printf("Digite o %dº número: ", i + 1);
        scanf("%d", &vetor[i]);
    }

    for (i = 0; i < 10; i++) {
        for (j = i + 1; j < 10; j++) {
            if (vetor[i] < vetor[j]) {
                aux = vetor[i];
                vetor[i] = vetor[j];
                vetor[j] = aux;
            }
        }
    }

    printf("\nVetor ordenado de forma decrescente: ");
    for (i = 0; i < 10; i++) {
        printf("%d ", vetor[i]);
    }

    return 0;
}