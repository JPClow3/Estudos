//a. Crie um VETOR de 50 posições com valores preenchidos aleatoriamente até 10, exiba esses valores
//em seguida através de uma função ou procedimento coloque todos os valores pares em um segundo vetor
//e através do metodo de ordenação de bolha faça a ordenação desse vetor. OBS. Todos os vetores devem ser impressos

#include <stdio.h>
#include <stdlib.h>
#include <time.h>

int pares(int vetor[50], int pares[50]) {
  int contador = 0;
  for (int i = 0; i < 50; i++) {
    if (vetor[i] % 2 == 0) {
      pares[contador] = vetor[i];
      contador++;
    }
  }
  return contador;
}

void bolha(int pares[50], int tamanho) {
  for (int i = 0; i < tamanho - 1; i++) {
    for (int j = 0; j < tamanho - i - 1; j++) {
      if (pares[j] > pares[j + 1]) {
        int aux = pares[j];
        pares[j] = pares[j + 1];
        pares[j + 1] = aux;
      }
    }
  }
}

int main() {
  int vetor[50];
  int pares[50];
  int quantPares;

  srand(time(NULL));
  for (int i = 0; i < 50; i++) {
    vetor[i] = rand() % 11;
  }

  printf("Vetor original:\n");
  for (int i = 0; i < 50; i++) {
    printf("%d ", vetor[i]);
  }
  printf("\n");

  quantPares = pares(vetor, pares);

  printf("Vetor de pares (antes da ordenação):\n");
  for (int i = 0; i < quantPares; i++) {
    printf("%d ", pares[i]);
  }
  printf("\n");

  bolha(pares, quantPares);

  printf("Vetor de pares (depois da ordenação):\n");
  for (int i = 0; i < quantPares; i++) {
    printf("%d ", pares[i]);
  }
  printf("\n");

  return 0;
}