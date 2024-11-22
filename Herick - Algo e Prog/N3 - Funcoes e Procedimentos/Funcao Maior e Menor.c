#include <stdio.h>

int buscaMaiorValor(int a, int b) {
    int MaiorNumero;

    if(a>b){
        a = MaiorNumero;
    }

    else b = MaiorNumero;

    return MaiorNumero;
}



int main(){

    int a = 5, b = 4;

    int MaiorNumero = buscaMaiorValor (a,b);

    printf("Maior Numero e %d", MaiorNumero);


}

