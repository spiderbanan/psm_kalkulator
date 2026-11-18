
package com.example.psm_kalkulator

import org.mariuszgromada.math.mxparser.Expression

sealed class CalculationResult {
    data class Success(val value: Double) : CalculationResult()
    data class Error(val message: String) : CalculationResult()
    object Empty : CalculationResult()
}

class CalculatorEngine {

    private fun hasIncompleteEnding(expression: String): Boolean {
        val trimmed = expression.trim()

        if (trimmed.isEmpty()) return false

        // Wyrażenie nie może kończyć się operatorem
        if (trimmed.last() in listOf('+', '-', '*', '/', '^', ',')) {
            return true
        }

        // Sprawdzamy, czy wszystkie nawiasy są zamknięte
        var parentheses = 0

        for (character in trimmed) {
            when (character) {
                '(' -> parentheses++
                ')' -> parentheses--
            }

            if (parentheses < 0) return true
        }

        return parentheses != 0
    }

    fun calculate(input: String): CalculationResult {

        val expression = input.trim().replace(',', '.')

        // Puste pole – bez wyniku i bez błędu
        if (expression.isEmpty()) {
            return CalculationResult.Empty
        }

        // Sprawdzenie, czy wyrażenie nie jest niedokończone
        if (hasIncompleteEnding(expression)) {
            return CalculationResult.Error("Niepełne wyrażenie")
        }

        // Utworzenie wyrażenia mXparser
        val parsedExpression = Expression(expression)

        // Sprawdzenie poprawności składni
        if (!parsedExpression.checkSyntax()) {
            return CalculationResult.Error("Nieprawidłowe wyrażenie")
        }

        // Wykonanie obliczenia
        val result = parsedExpression.calculate()

        // Obsługa niepoprawnego wyniku
        if (result.isNaN()) {
            return CalculationResult.Error("Nieprawidłowe wyrażenie")
        }

        // Obsługa dzielenia przez zero i nieskończoności
        if (result.isInfinite()) {
            return CalculationResult.Error("Nie można dzielić przez zero")
        }

        return CalculationResult.Success(result)
    }


}
