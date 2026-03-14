package org.example.log;

// LogSystem.java — handles x > 0
public class LogSystem {
    private final Ln ln;
    private final Log log2, log3, log5, log10;

    public LogSystem(Ln ln) {
        this.ln = ln;
        this.log2  = new Log(ln, 2);
        this.log3  = new Log(ln, 3);
        this.log5  = new Log(ln, 5);
        this.log10 = new Log(ln, 10);
    }

    public double calculate(double x) {
        if (x <= 0) throw new IllegalArgumentException("LogSystem undefined for x <= 0");

        double lnx    = ln.calc(x);
        double log2x  = log2.calc(x);
        double log3x  = log3.calc(x);
        double log5x  = log5.calc(x);
        double log10x = log10.calc(x);

        // log_10(x) / log_10(x) simplifies to 1 when x != 1,
        // but at x=1 both are 0 — guard explicitly
        if (Math.abs(log10x) < 1e-10)
            throw new ArithmeticException("LogSystem undefined: log10(x)=0 at x=" + x);

        double num = Math.pow(log2x / log10x, 3) * lnx * (log5x / log3x);
        double den = (log3x - (1.0)) - lnx;  // = log3(x) - 1 - ln(x)

        if (Math.abs(den) < 1e-10)
            throw new ArithmeticException("LogSystem undefined: denominator=0 at x=" + x);

        return num / den;
    }
}
