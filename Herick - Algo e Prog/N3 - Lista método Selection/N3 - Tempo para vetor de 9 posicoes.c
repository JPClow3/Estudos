//Consdierando o vetor [1,2,3,4,5,6,7,8,9]
//qual seria a complexidade de tempo do Selection Sort ?

#include <stdio.h>
#include <stdlib.h>
#include <time.h>


int main () {

    int vetor [10] = 1,2,3,4,5,6,7,8,9;
    int aux, i, j, min, tempo;
  printf("Vetor desordenado: ");

    for (i = 0; i < 9; i++) {
      min = i;
      for (j = i+1; j < 10; j++) {
        if (vetor[j] < vetor[min]) {
          min = j;
         tempo++;
        }
      }
      aux = vetor[i];
      vetor[i] = vetor[min];
      vetor[min] = aux;
    }

    printf("Vetor ordenado: ");
    for (i = 0; i < 10; i++) {
      printf("%d ", vetor[i]);
    }
    printf("\n");
    printf("Complexidade de tempo: O(n^2)\n", tempo);



  return 0
  }