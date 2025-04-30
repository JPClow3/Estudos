//Criar um array de inteiros com pelo menos 40 elementos
//Implementar o Selection Sort para ordenar o array em ordem crescente
//Imprimir o array antes e depois da ordenacao

#include <stdio.h>
#include <stdlib.h>
#include <time.h>


int main(){

  int matriz [8][8];
    int aux, i, j, min, tempo;

    for (i = 0; i < 8; i++) {
      for (j = 0; j < 8; j++) {
        matriz[i][j] = rand() % 100;
      }
    }

    printf("Matriz desordenada: \n");

    for (i = 0; i < 8; i++) {
      for (j = 0; j < 8; j++) {
        printf("%d ", matriz[i][j]);
      }
      printf("\n");
    }

    for (i = 0; i < 8; i++) {
      min = i;
      for (j = i+1; j < 8; j++) {
        if (matriz[j] < matriz[min]) {
          min = j;
          tempo++;
        }
      }
      aux = matriz[i];
      matriz[i] = matriz[min];
      matriz[min] = aux;
    }
    printf("Matriz ordenada: \n");
    for (i = 0; i < 8; i++) {
      for (j = 0; j < 8; j++) {
        printf("%d ", matriz[i][j]);
      }
      printf("\n");
    }
    printf("Complexidade de tempo: O(n^2)\n", tempo);


 return 0;
}