//3- Crie uma matriz 5x5 de tipo inteiro. Preencha a matriz de acordo com os exemplos abaixo:
//primeira matriz : diagonal principal com 12345 restante da primeira linha sendo 9 da segunda sendo 6 da terceira sendo 9 da quarta sendo 6 e da quinta sendo 9
//segunda matriz : diagonal secundária com 12345
//terceira matriz : diagonal principal e secundaria sendo 00200 e demais posicoes 1
//quarta matriz : diagonal principal sendo 22222 e triangular superior e inferior sendo 1 e 3 respectivamente

#include <stdio.h>

#include <stdio.h>

int main() {
    int matriz1[5][5], matriz2[5][5], matriz3[5][5], matriz4[5][5];
    int i, j;

    for (i = 0; i < 5; i++) {
        for (j = 0; j < 5; j++) {
            if (i == j) {
                matriz1[i][j] = i + 1;
            } else if (i == 0) {
                matriz1[i][j] = 9;
            } else if (i == 1) {
                matriz1[i][j] = 6;
            } else if (i == 2) {
                matriz1[i][j] = 9;
            } else if (i == 3) {
                matriz1[i][j] = 6;
            } else if (i == 4) {
                matriz1[i][j] = 9;
            }
        }
    }

    for (i = 0; i < 5; i++) {
        for (j = 0; j < 5; j++) {
            if (i + j == 4) {
                matriz2[i][j] = i + 1;
            } else {
                matriz2[i][j] = 0;
            }
        }
    }

    for (i = 0; i < 5; i++) {
        for (j = 0; j < 5; j++) {
            if (i == j) {
                matriz3[i][j] = 2;
            } else if (i + j == 4) {
                matriz3[i][j] = 2;
            } else {
                matriz3[i][j] = 1;
            }
        }
    }

    for (i = 0; i < 5; i++) {
        for (j = 0; j < 5; j++) {
            if (i == j) {
                matriz4[i][j] = 2;
            } else {
                matriz4[i][j] = 1;
            }
        }
    }

    printf("Matriz1:\n");
    for (i = 0; i < 5; i++) {
        for (j = 0; j < 5; j++) {
            printf("%d ", matriz1[i][j]);
        }
        printf("\n");
    }
    printf("\n");

    printf("Matriz2:\n");
    for (i = 0; i < 5; i++) {
        for (j = 0; j < 5; j++) {
            printf("%d ", matriz2[i][j]);
        }
        printf("\n");
    }
    printf("\n");


    printf("Matriz3:\n");
    for (i = 0; i < 5; i++) {
        for (j = 0; j < 5; j++) {
            printf("%d ", matriz3[i][j]);
        }
        printf("\n");
    }
    printf("\n");

    printf("Matriz4:\n");
    for (i = 0; i < 5; i++) {
        for (j = 0; j < 5; j++) {
            printf("%d ", matriz4[i][j]);
        }
        printf("\n");
    }
    printf("\n");

    return 0;
}
