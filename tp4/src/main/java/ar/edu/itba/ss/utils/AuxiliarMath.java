package ar.edu.itba.ss.utils;

public class AuxiliarMath {

    private AuxiliarMath(){}

    public static int factorial(int n) {
        int fact = 1;
        for (int i = 2; i <= n; i++) {
            fact = fact * i;
        }
        return fact;
    }

}
