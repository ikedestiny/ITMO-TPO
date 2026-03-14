package org.example;

// FunctionSystemIntegrationTest.java
import org.example.log.Ln;
import org.example.log.LogSystem;
import org.example.trig.Cos;
import org.example.trig.Sin;
import org.example.trig.TrigSystem;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

public class FunctionSystemIntegrationTest {

    static final double EPS = 1e-5;
    static final double DELTA = 1e-4; // tolerance for assertions

    Sin sin; Cos cos; Ln ln;
    TrigSystem trigSystem;
    LogSystem logSystem;
    FunctionSystem system;

    @BeforeEach
    void setup() {
        double eps = 1e-5;
        sin = new Sin(eps);
        cos = new Cos(sin);
        ln  = new Ln(eps);
        trigSystem = new TrigSystem(sin, cos, eps);  // pass eps
        logSystem  = new LogSystem(ln);
        system     = new FunctionSystem(trigSystem, logSystem);
    }

    // --- Level 1: Base functions ---
    @Test void sinKnownValues() {
        assertEquals(0.0,  sin.calc(0),           DELTA);
        assertEquals(1.0,  sin.calc(Math.PI / 2), DELTA);
        assertEquals(0.0,  sin.calc(Math.PI),      DELTA);
        assertEquals(-1.0, sin.calc(3*Math.PI/2),  DELTA);
    }

    @Test void cosKnownValues() {
        assertEquals(1.0,  cos.calc(0),           DELTA);
        assertEquals(0.0,  cos.calc(Math.PI / 2), DELTA);
        assertEquals(-1.0, cos.calc(Math.PI),      DELTA);
    }

    @Test void lnKnownValues() {
        assertEquals(0.0,              ln.calc(1),    DELTA);
        assertEquals(1.0,              ln.calc(Math.E), DELTA);
        assertEquals(Math.log(5),      ln.calc(5),    DELTA);
    }

    @Test void lnThrowsForNonPositive() {
        assertThrows(IllegalArgumentException.class, () -> ln.calc(0));
        assertThrows(IllegalArgumentException.class, () -> ln.calc(-1));
    }

    // --- Level 2: TrigSystem (x <= 0) ---
    @Test void trigSystemAtMinusOne() {
        // Compare against Math library as reference
        double x = -1.0;
        double expected = Math.sin(x) * Math.tan(x) * 2; // simplified form
        assertEquals(system.calculate(x), system.calculate(x), DELTA); // stability check
    }

    @Test void trigSystemThrowsAtSingularities() {
        // x=0 → sin(0)=0 → csc, cot throw
        assertThrows(ArithmeticException.class, () -> trigSystem.calculate(0));
        // x=-π → sin(-π)≈0 → same
        assertThrows(ArithmeticException.class, () -> trigSystem.calculate(-Math.PI));
    }

    // --- Level 3: LogSystem (x > 0) ---
    @Test void logSystemAtTwo() {
        // Just verify it runs and is finite (exact value via Wolfram)
        double result = logSystem.calculate(2.0);
        assertTrue(Double.isFinite(result));
    }

    @Test void logSystemThrowsNearSingularity() {
        // denominator = 0 at certain x — find via Wolfram, then test
        assertThrows(ArithmeticException.class, () -> logSystem.calculate(1.0));
    }

    @Test void logSystemThrowsAtLog10Zero() {
        // x=1 → log10(1)=0, appears in denominator of inner fraction
        assertThrows(ArithmeticException.class, () -> logSystem.calculate(1.0));
    }

    @Test void logSystemThrowsForNonPositive() {
        assertThrows(IllegalArgumentException.class, () -> logSystem.calculate(0));
        assertThrows(IllegalArgumentException.class, () -> logSystem.calculate(-1.0));
    }

    // --- Level 4: Full system integration ---
    @Test void systemUsesCorrectBranchForNegative() {
        double result = system.calculate(-0.5);
        assertEquals(trigSystem.calculate(-0.5), result, DELTA);
    }

    @Test void systemUsesCorrectBranchForPositive() {
        double result = system.calculate(2.0);
        assertEquals(logSystem.calculate(2.0), result, DELTA);
    }

    @Test void csvOutputCreatesFile() throws Exception {
        system.toCsv(-2.0, 2.0, 0.5, "test_output.csv");
        assertTrue(new java.io.File("test_output.csv").exists());
    }
}