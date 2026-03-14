package org.example.trig;

// Sin.java — Taylor series expansion
public class Sin {
    private final double epsilon;
    public Sin(double epsilon) { this.epsilon = epsilon; }

    public double calc(double x) {
        // Correct normalization for all x
        x = x - 2 * Math.PI * Math.floor(x / (2 * Math.PI)); // map to [0, 2π]
        x = x - 2 * Math.PI * Math.round(x / (2 * Math.PI)); // then to [-π, π]
        double term = x, result = x;
        for (int n = 1; Math.abs(term) > epsilon; n++) {
            term *= -x * x / ((2.0 * n) * (2.0 * n + 1));
            result += term;
        }
        return result;
    }
}
