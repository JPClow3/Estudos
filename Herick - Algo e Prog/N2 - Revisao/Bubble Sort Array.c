//2. Implemente o algoritmo Bubble Sort para ordenar um array de inteiros em ordem crescente.
//Crie um array de inteiros com pelo menos 50 elementos.
//Implemente o algoritmo Bubble Sort.
//Exiba o array antes e depois da ordenação.



#include <stdio.h>
#include <stdlib.h>
#include <time.h>


int main() {
    int array[5][10];
    int cont = 0;


    srand(time(NULL));


    for (int i = 0; i < 5; i++) {
        for (int j = 0; j < 10; j++) {
            array[i][j] = rand() % 100;
        }
    }


    printf("Array antes do Bubble Sort:\n");
    for (int i = 0; i < 5; i++) {
        for (int j = 0; j < 10; j++) {
            printf("%d ", array[i][j]);
        }
        printf("\n");
    }


    for (int i = 0; i < 5; i++) {
        for (int j = 0; j < 10; j++) {
            for (int k = j + 1; k < 10; k++) {
                if (array[i][j] < array[i][k]) {
                    int aux = array[i][j];
                    array[i][j] = array[i][k];
                    array[i][k] = aux;
                    cont++;
                }
            }
        }
    }


    printf("\nArray apos Bubble Sort:\n");
    for (int i = 0; i < 5; i++) {
        for (int j = 0; j < 10; j++) {
            printf("%d ", array[i][j]);
        }
        printf("\n");
    }


    printf("\nSwaps Feitos: %d\n", cont);


    return 0;
}