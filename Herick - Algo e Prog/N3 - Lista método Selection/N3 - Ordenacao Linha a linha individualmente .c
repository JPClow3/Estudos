//Criar um algortimo em C para ordenar uma matriz de 4x8 linha a linha individualmente

#include <stdio.h>
#include <stdlib.h>
#include <time.h>

int main() {
  int matriz[4][8];
    int aux, i, j, min, tempo;

    for (i = 0; i < 4; i++) {
      for (j = 0; j < 8; j++) {
        matriz[i][j] = rand() % 100;
      }
    }

    printf("Matriz desordenada: \n");

    for (i = 0; i < 4; i++) {
      for (j = 0; j < 8; j++) {
        printf("%d ", matriz[i][j]);
      }
      printf("\n");
    }

    for (i = 0; i < 4; i++) {
      min = i;
      for (j = i+1; j < 8; j++) {
        if (matriz[i][j] < matriz[i][min]) {
          min = j;
          tempo++;
        }
      }
      aux = matriz[i][i];
      matriz[i][i] = matriz[i][min];
      matriz[i][min] = aux;
    }

    printf("Matriz ordenada: \n");
    for (i = 0; i < 4; i++) {
      for (j = 0; j < 8; j++) {
        printf("%d ", matriz[i][j]);
      }
      printf("\n");
    }

    printf ("Complexidade de tempo: O(n^2)\n", tempo);





  return 0;
  }
