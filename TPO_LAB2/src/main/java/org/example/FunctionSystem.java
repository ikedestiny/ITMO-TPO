package org.example;

import org.example.log.LogSystem;
import org.example.trig.TrigSystem;

// FunctionSystem.java — the top-level entry point
public class FunctionSystem {
    private final TrigSystem trig;
    private final LogSystem log;

    public FunctionSystem(TrigSystem trig, LogSystem log) {
        this.trig = trig;
        this.log  = log;
    }

    public double calculate(double x) {
        if (x <= 0) return trig.calculate(x);
        else        return log.calculate(x);
    }

    public void toCsv(double from, double to, double step, String filename) throws Exception {
        try (var w = new java.io.FileWriter(filename)) {
            w.write("x,result\n");
            for (double x = from; x <= to; x += step) {
                try {
                    w.write(x + "," + calculate(x) + "\n");
                } catch (Exception e) {
                    w.write(x + ",undefined\n");
                }
            }
        }
    }
}