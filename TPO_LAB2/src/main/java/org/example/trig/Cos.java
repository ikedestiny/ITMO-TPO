package org.example.trig;

// Cos.java — derived from Sin
public class Cos {
    private final Sin sin;
    public Cos(Sin sin) { this.sin = sin; }

    public double calc(double x) {
        return sin.calc(Math.PI / 2 - x); // cos(x) = sin(π/2 - x)
    }
}
