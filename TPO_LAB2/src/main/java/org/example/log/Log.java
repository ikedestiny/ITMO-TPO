package org.example.log;

// Log.java — any base, built from Ln
public class Log {
    private final Ln ln;
    private final double base;
    public Log(Ln ln, double base) { this.ln = ln; this.base = base; }

    public double calc(double x) {
        return ln.calc(x) / ln.calc(base); // change of base
    }
}
