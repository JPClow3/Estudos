#include <stdio.h>
#include <stdlib.h>
#include <math.h>

int calcularDistancia(int x1, int x2, int y1, int y2) {

    int d1,d2;
    int d3;
    int distancia;

    d1 = x2-x1;
    d1 = d1*d1;
    d2 = y2-y1;
    d2 = d2*d2;
    d3 = d1+d2;

    distancia = sqrt(d3);

    return distancia;

}

int main (){

    int x1 = 2,x2 = 3, y1 = 5, y2 =4;

    int distancia = calcularDistancia(x1,x2,y1,y2);


    printf("A distancia e %d ", distancia);


    return 0;
}
