package org.example.trig;

public class TrigSystem {
    private final Sin sin;
    private final Cos cos;
    private final TrigFunctions trig;
    private final double epsilon;  // add this

    public TrigSystem(Sin sin, Cos cos, double epsilon) {  // pass epsilon in
        this.sin = sin;
        this.cos = cos;
        this.trig = new TrigFunctions(sin, cos);
        this.epsilon = epsilon;
    }

    public double calculate(double x) {
        double sinVal = sin.calc(x);
        double cosVal = cos.calc(x);

        if (Math.abs(sinVal) < Math.sqrt(epsilon))  // e.g. sqrt(1e-5) = ~3e-3... too big
            // better: use epsilon directly but scaled
            if (Math.abs(sinVal) < epsilon * 100)        // 1e-5 * 100 = 1e-3 — safe margin
                throw new ArithmeticException("TrigSystem undefined: sin(x)=0 at x=" + x);
        if (Math.abs(cosVal) < epsilon * 100)
            throw new ArithmeticException("TrigSystem undefined: cos(x)=0 at x=" + x);

        double cotx = trig.cot(x);
        double cscx = trig.csc(x);
        double tanx = trig.tan(x);
        double secx = trig.sec(x);

        double inner = ((cotx / cscx) / (cscx * tanx)) + (sinVal * secx);
        double left  = inner * tanx;
        double right = sinVal * ((cosVal / (secx * sinVal)) + tanx);
        return left + right;
    }
}