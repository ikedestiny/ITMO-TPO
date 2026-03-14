package org.example.trig;

// TrigFunctions.java — cot, csc, sec, tan all from Sin/Cos
public class TrigFunctions {
    private final Sin sin;
    private final Cos cos;

    public TrigFunctions(Sin sin, Cos cos) { this.sin = sin; this.cos = cos; }

    public double tan(double x) {
        double c = cos.calc(x);
        if (Math.abs(c) < 1e-10) throw new ArithmeticException("tan undefined: cos(x) = 0 at x=" + x);
        return sin.calc(x) / c;
    }

    public double cot(double x) {
        double s = sin.calc(x);
        if (Math.abs(s) < 1e-10) throw new ArithmeticException("cot undefined: sin(x) = 0 at x=" + x);
        return cos.calc(x) / s;
    }

    public double sec(double x) {
        double c = cos.calc(x);
        if (Math.abs(c) < 1e-10) throw new ArithmeticException("sec undefined: cos(x) = 0 at x=" + x);
        return 1.0 / c;
    }

    public double csc(double x) {
        double s = sin.calc(x);
        if (Math.abs(s) < 1e-10) throw new ArithmeticException("csc undefined: sin(x) = 0 at x=" + x);
        return 1.0 / s;
    }
}