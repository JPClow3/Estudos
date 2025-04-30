#include <stdio.h>

int Soma(int a, int b) {
    int resultado;
    resultado = a + b;

    return resultado;

}

int Subtracao(int a, int b) {
    int resultado;
    resultado = a - b;

    return resultado;

}

int Divisao(int a, int b) {
    int resultado;
    resultado = a / b;

    return resultado;

}

int Multiplicacao(int a, int b) {
    int resultado;
    resultado = a * b;

    return resultado;

}


int main(){

    int a, b;
    int resultado;
    int escolha;


    printf("Qual operação deseja realizar ?  \n (1) - Soma \n (2) - Subtracao\n (3) - Divisao\n (4) - Multiplicacao");
    scanf("%d", &escolha);

    printf("Digite os 2 numeros da operacao: ");
    scanf("%d%d", &a,&b);

    switch (escolha)
    {
        case 1:
            resultado = Soma(a,b);
        break;
        case 2:
            resultado = Subtracao(a,b);
        break;
        case 3:
            if (b == 0) {
                printf("Erro: Divisao por zero\n");
                return 1;
            }
        resultado = Divisao(a,b);
        break;
        case 4:
            resultado = Multiplicacao(a,b);
        break;
    }
    printf("resultado e %d\n", resultado);
    return 0;
}