//Escreva uma função recursiva que verifica se uma String é um palíndromo.
//Escreva uma função recursiva que calcule o n-ésimo número da sequência de Fibonacci.

public static boolean isPalindrome(String str) {
    if (str.length() <= 1) {
        return true;
    }
    if (str.charAt(0) != str.charAt(str.length() - 1)) {
        return false;
    }
    return isPalindrome(str.substring(1, str.length() - 1));
}

public static int fibonacci(int n) {
    if (n <= 0) {
        return 0;
    }
    if (n == 1) {
        return 1;
    }
    return fibonacci(n - 1) + fibonacci(n - 2);
}

public static class Main {
    static void main() {
        String testStr = "radar";
        int n = 10;
        System.out.println(testStr + " is palindrome: " + isPalindrome(testStr));
        System.out.println("Fibonacci of " + n + " is: " + fibonacci(n));
        }
    }
