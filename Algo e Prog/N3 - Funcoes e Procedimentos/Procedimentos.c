//Procedimento que imprima todos os numeros de 0 10
//Procedimento que imprima todos os numeros de 0 a 100
//Procedimento que imprima a tabela de 5
//Procedimento que deve receber um valor inteiro positivo como parâmetro e exibir se o valor informado pelo usuário é par ou ímpar.
//Procedimento que imprima a tabuada de um número inteiro dado como entrada
//procedimento que receba dois valores informados pelo usuário.
//O programa deverá exibir na tela  os números ímpares dentro desse intervalo(incluindo os informados pelo usuário)

#include <stdio.h>
#include <stdlib.h>
#include <time.h>

int void () {

  int i;
  for (i = 0; i <= 10; i++) {
    printf("%d\n", i);
  }
}

int void () {

  int i;
  for (i=0; i <= 100; i++) {
    printf("%d\n", i);
    }
    }

int void () {

  int tabuada_expo, tabuada_5 = 5, Tabuada_result;

  for (tabuada_expo = 0; tabuada_expo <= 10; tabuada_expo++) {
    Tabuada_result = tabuada_5 * tabuada_expo;
    printf("%d x %d = %d\n", tabuada_5, tabuada_expo, Tabuada_result);
  }

int void (int i = 10) {
    if (i % 2 == 0) {
      printf("O número %d é par\n", i);
    } else {
      printf("O número %d é ímpar\n", i);
    }
  }

int void (int tabuada_base) {

    int tabuada_expo,Tabuada_result;

    for (tabuada_expo = 0; tabuada_expo <= 10; tabuada_expo++) {
      Tabuada_result = tabuada_base * tabuada_expo;
      printf("%d x %d = %d\n", tabuada_base, tabuada_expo, Tabuada_result);
    }
int void (int i, int j) {
    int k;
    for (k = i; k <= j; k++) {
      if (k % 2 != 0) {
        printf("%d\n", k);
      }
    }
  }
  return 0;
}


