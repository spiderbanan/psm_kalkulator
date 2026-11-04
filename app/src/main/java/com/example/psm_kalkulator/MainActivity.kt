
package com.example.psm_kalkulator

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

enum class KeyRole {
    NUMBER,
    OPERATOR,
    ACTION,
    EQUALS
}

data class CalculatorKey(
    val label: String,
    val role: KeyRole
)



val calculatorKeys = listOf(
    CalculatorKey("AC", KeyRole.ACTION),
    CalculatorKey("(", KeyRole.ACTION),
    CalculatorKey(")", KeyRole.ACTION),
    CalculatorKey("÷", KeyRole.OPERATOR),

    CalculatorKey("7", KeyRole.NUMBER),
    CalculatorKey("8", KeyRole.NUMBER),
    CalculatorKey("9", KeyRole.NUMBER),
    CalculatorKey("×", KeyRole.OPERATOR),

    CalculatorKey("4", KeyRole.NUMBER),
    CalculatorKey("5", KeyRole.NUMBER),
    CalculatorKey("6", KeyRole.NUMBER),
    CalculatorKey("-", KeyRole.OPERATOR),

    CalculatorKey("1", KeyRole.NUMBER),
    CalculatorKey("2", KeyRole.NUMBER),
    CalculatorKey("3", KeyRole.NUMBER),
    CalculatorKey("+", KeyRole.OPERATOR),

    CalculatorKey("0", KeyRole.NUMBER),
    CalculatorKey(".", KeyRole.NUMBER),
    CalculatorKey("⌫", KeyRole.ACTION),
    CalculatorKey("=", KeyRole.EQUALS)
)


// Główna aktywność
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            CalculatorScreen()
        }
    }
}

// Wygląd pojedynczego przycisku
@Composable
fun CalculatorButton(
    key: CalculatorKey,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {

    val buttonColor = when (key.role) {
        KeyRole.NUMBER -> Color(0xFF333333)
        KeyRole.OPERATOR -> Color(0xFFFF9800)
        KeyRole.ACTION -> Color(0xFF777777)
        KeyRole.EQUALS -> Color(0xFF2196F3)
    }

    Button(
        onClick = onClick,
        modifier = modifier,
        shape = RoundedCornerShape(50),
        colors = ButtonDefaults.buttonColors(
            containerColor = buttonColor
        ),
        contentPadding = PaddingValues(0.dp)
    ) {

        Text(
            text = key.label,
            fontSize = 24.sp,
            color = Color.White
        )
    }
}

// Główny ekran kalkulatora
@Composable
fun CalculatorScreen() {

    var expression by remember {
        mutableStateOf("")
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(16.dp),

        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {

        // Pole wyświetlania działania
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            contentAlignment = androidx.compose.ui.Alignment.BottomEnd
        ) {

            Text(
                text = expression,
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                color = Color.White,
                fontSize = 36.sp,
                textAlign = TextAlign.End
            )
        }

        // Przyciski kalkulatora
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(2f),

            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {

            // Podział listy na 5 wierszy po 4 przyciski
            calculatorKeys.chunked(4).forEach { rowKeys ->

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),

                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {

                    rowKeys.forEach { key ->

                        CalculatorButton(
                            key = key,

                            modifier = Modifier
                                .weight(1f)
                                .fillMaxHeight(),

                            onClick = {

                                expression = when (key.label) {

                                    "AC" -> ""

                                    "⌫" -> expression.dropLast(1)

                                    "=" -> expression

                                    else -> expression + key.label
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}
