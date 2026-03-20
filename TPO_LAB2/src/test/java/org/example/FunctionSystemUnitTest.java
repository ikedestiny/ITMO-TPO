package org.example;

import org.example.log.Ln;
import org.example.log.LogSystem;
import org.example.trig.Cos;
import org.example.trig.Sin;
import org.example.trig.TrigSystem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.Mockito.*;

public class FunctionSystemUnitTest {

    private static final double DELTA = 1e-4;

    private TrigSystem mockTrig;

    private LogSystem mockLog;

    @BeforeEach
    void setup() {
        mockTrig = mock(TrigSystem.class); 
        mockLog  = mock(LogSystem.class);
    }

    @ParameterizedTest
    @CsvSource({
        "-2.5, 123.456",
        "-1.0, 42.0",
        "0.0, 21.0"
    })
    void testDelegatesToTrigSystemForNonPositive(double x, double expected) {
        FunctionSystem fs = new FunctionSystem(mockTrig, mockLog);
        when(mockTrig.calculate(x)).thenReturn(expected);

        double result = fs.calculate(x);

        assertEquals(expected, result, DELTA);
        verify(mockTrig, times(1)).calculate(x);
        verify(mockLog, never()).calculate(anyDouble());
    }

    @ParameterizedTest
    @CsvSource({
        "3.0, 789.012",
        "5.5, 111.222"
    })
    void testDelegatesToLogSystemForPositive(double x, double expected) {
        FunctionSystem fs = new FunctionSystem(mockTrig, mockLog);
        when(mockLog.calculate(x)).thenReturn(expected);

        double result = fs.calculate(x);

        assertEquals(expected, result, DELTA);
        verify(mockLog, times(1)).calculate(x);
        verify(mockTrig, never()).calculate(anyDouble());
    }

    @ParameterizedTest
    @MethodSource("exceptionTestData")
    void testPropagatesException(double x, Class<? extends Exception> expectedException, String mockType) {
        FunctionSystem fs = new FunctionSystem(mockTrig, mockLog);

        if (mockType.equals("trig")) {
            when(mockTrig.calculate(x)).thenThrow(new ArithmeticException("Division by zero"));
        } else {
            when(mockLog.calculate(x)).thenThrow(new IllegalArgumentException("Log of non-positive"));
        }

        assertThrows(expectedException, () -> fs.calculate(x));

        if (mockType.equals("trig")) {
            verify(mockTrig, times(1)).calculate(x);
            verify(mockLog, never()).calculate(anyDouble());
        } else {
            verify(mockLog, times(1)).calculate(x);
            verify(mockTrig, never()).calculate(anyDouble());
        }
    }

    static Stream<Arguments> exceptionTestData() {
        return Stream.of(
            Arguments.of(-1.0, ArithmeticException.class, "trig"),
            Arguments.of(2.0, IllegalArgumentException.class, "log")
        );
    }
}