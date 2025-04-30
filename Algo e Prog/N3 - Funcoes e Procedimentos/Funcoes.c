//Função que receberá um valor inteiro positivo A função deverá calcular o número fatorial dele ou 0 caso o valor informado for negativo
//Função que retorne o dobro de um número passado como parâmetro pelo usuário.
//Função que retorne o valor absoluto de um número passado como parâmetro. O valor absoluto de um número será o valor transformado em positivo

#include <stdio.h>

int fatorial(int n) {
  if (n < 0) {
    printf ("0\n");
  }
  else if (n == 0) {
    printf ("0\n");
  }
  else {
    int i, fat = 1;
    for (i = 1; i <= n; i++) {
      fat = fat * i;
    }
    printf ("%d\n", fat);
  }

int dobro(int n) {
  int dobro = n * 2;
  printf ("\n%d", dobro;
}

int absoluto(int n) {
  if (n < 0) {
    n = n * -1;
    printf ("\n%d", n);
  }
  else {
    printf ("\n%d", n);
  }
}