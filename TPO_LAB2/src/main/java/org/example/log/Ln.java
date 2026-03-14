package org.example.log;

// Ln.java — Taylor series for ln(x), x > 0
public class Ln {
    private final double epsilon;
    public Ln(double epsilon) { this.epsilon = epsilon; }

    public double calc(double x) {
        if (x <= 0) throw new IllegalArgumentException("ln undefined for x <= 0");
        // Use ln((1+t)/(1-t)) series where t = (x-1)/(x+1)
        double t = (x - 1.0) / (x + 1.0);
        double t2 = t * t, term = t, result = t;
        for (int n = 1; Math.abs(term) > epsilon; n++) {
            term *= t2;
            result += term / (2 * n + 1);
        }
        return 2 * result;
    }
}