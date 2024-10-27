//1. Considerando que o metodo Bubble Sort possui complexidade O(n2)
//baseado no vetor [1, 6, 2, 0, 9, 3, 4, 7, 5, 8, 10, 11]
//qual será o total de comparações que serão realizadas até o final da execução do algoritmo.


#include <stdio.h>


int main() {
    int vetor [12] = {1,6,2,0,9,3,4,7,5,8,10,11};
    int i, j, aux;
    int cont = 0;


    for (i = 0; i < 10; i++) {
        for (j = i + 1; j < 10; j++) {
            if (vetor[i] < vetor[j]) {
                aux = vetor[i];
                vetor[i] = vetor[j];
                vetor[j] = aux;
                cont++;


            }
        }
    }


    printf("\nVetor ordenado de forma decrescente: ");
    for (i = 0; i < 10; i++) {
        printf("%d ", vetor[i]);
    }
    printf("\nQuantidade de vezes em que foi feito o swap: %d", cont);

    return 0;
}