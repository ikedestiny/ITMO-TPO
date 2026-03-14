package org.example.log;

public class DenominatorCheck {

    public static void main(String[] args) {

        for (double x = 1e-8; x < 10; x += 1e-6) {

            double log3 = Math.log(x) / Math.log(3);
            double ln = Math.log(x);

            double den = log3 - 1 - ln;

            if (Math.abs(den) < 1e-6) {
                System.out.println("Possible zero denominator near x = " + x);
            }
        }

    }
}