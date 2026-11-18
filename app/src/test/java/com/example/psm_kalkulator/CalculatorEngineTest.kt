
package com.example.psm_kalkulator

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class CalculatorEngineTest {

    private val calculator = CalculatorEngine()

    @Test
    fun multiplicationHasPriority() {
        val result = calculator.calculate("2+2*2")

        assertEquals(
            CalculationResult.Success(6.0),
            result
        )
    }

    @Test
    fun parenthesesWork() {
        val result = calculator.calculate("(2+2)*2")

        assertEquals(
            CalculationResult.Success(8.0),
            result
        )
    }

    @Test
    fun divisionByZeroReturnsError() {
        val result = calculator.calculate("1/0")

        assertTrue(result is CalculationResult.Error)
    }

    @Test
    fun incompleteExpressionReturnsError() {
        val result = calculator.calculate("2+")

        assertTrue(result is CalculationResult.Error)
    }

    @Test
    fun unclosedParenthesisReturnsError() {
        val result = calculator.calculate("((2+2)")

        assertTrue(result is CalculationResult.Error)
    }

    @Test
    fun emptyExpressionReturnsEmpty() {
        val result = calculator.calculate("")

        assertEquals(CalculationResult.Empty, result)
    }

    @Test
    fun negativeSquareRootReturnsError() {
        val result = calculator.calculate("sqrt(-1)")

        assertTrue(result is CalculationResult.Error)
    }

    @Test
    fun largeNumbersAreCalculated() {
        val result = calculator.calculate("1234567890*1234567890")

        assertEquals(
            CalculationResult.Success(1.524157875019052E18),
            result
        )
    }

    @Test
    fun decimalCommaIsAccepted() {
        val result = calculator.calculate("1,5+2,5")

        assertEquals(
            CalculationResult.Success(4.0),
            result
        )
    }
}
