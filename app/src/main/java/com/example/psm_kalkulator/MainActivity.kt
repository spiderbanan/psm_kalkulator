
package com.example.psm_kalkulator

import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

import com.example.psm_kalkulator.ui.theme.AppTheme
import androidx.compose.runtime.saveable.rememberSaveable

enum class KeyRole {
    NUMBER, OPERATOR, ACTION, EQUALS, SCIENTIFIC
}

data class CalculatorKey(
    val label: String,
    val role: KeyRole,
    val insertText: String = label,
    val landscapeOnly: Boolean = false
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
    CalculatorKey("=", KeyRole.EQUALS),

    CalculatorKey("√", KeyRole.SCIENTIFIC, "sqrt(", true),
    CalculatorKey("xʸ", KeyRole.SCIENTIFIC, "^", true),
    CalculatorKey("sin", KeyRole.SCIENTIFIC, "sin(", true),
    CalculatorKey("cos", KeyRole.SCIENTIFIC, "cos(", true),
    CalculatorKey("tan", KeyRole.SCIENTIFIC, "tan(", true),
    CalculatorKey("log", KeyRole.SCIENTIFIC, "log(", true),
    CalculatorKey("π", KeyRole.SCIENTIFIC, "pi", true),
    CalculatorKey("e", KeyRole.SCIENTIFIC, "e", true)
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            AppTheme {
                CalculatorScreen()
            }
        }
    }
}

@Composable
fun CalculatorButton(
    key: CalculatorKey,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    val colors = MaterialTheme.colorScheme

    val buttonColor = when (key.role) {
        KeyRole.NUMBER -> colors.surfaceVariant
        KeyRole.OPERATOR -> colors.secondary
        KeyRole.ACTION -> colors.tertiary
        KeyRole.EQUALS -> colors.primary
        KeyRole.SCIENTIFIC -> colors.secondaryContainer
    }

    val textColor = when (key.role) {
        KeyRole.NUMBER -> colors.onSurfaceVariant
        KeyRole.OPERATOR -> colors.onSecondary
        KeyRole.ACTION -> colors.onTertiary
        KeyRole.EQUALS -> colors.onPrimary
        KeyRole.SCIENTIFIC -> colors.onSecondaryContainer
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
            fontSize = 22.sp,
            color = textColor
        )
    }
}

@Composable
fun CalculatorGrid(
    keys: List<CalculatorKey>,
    columns: Int,
    modifier: Modifier = Modifier,
    onKeyClick: (CalculatorKey) -> Unit
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        keys.chunked(columns).forEach { rowKeys ->
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
                        onClick = { onKeyClick(key) }
                    )
                }
            }
        }
    }
}

@Composable
fun CalculatorScreen() {
    val configuration = LocalConfiguration.current
    val isLandscape =
        configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

    var expression by rememberSaveable { mutableStateOf("") }

    val standardKeys = calculatorKeys.filter { !it.landscapeOnly }
    val scientificKeys = calculatorKeys.filter { it.landscapeOnly }

    fun handleKeyClick(key: CalculatorKey) {
        when (key.label) {
            "AC" -> expression = ""
            "⌫" -> {
                if (expression.isNotEmpty()) {
                    expression = expression.dropLast(1)
                }
            }
            "=" -> {
                // Obliczanie dodamy w kolejnym etapie.
            }
            else -> expression += key.insertText
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(8.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .background(
                    MaterialTheme.colorScheme.surface,
                    RoundedCornerShape(12.dp)
                )
                .padding(12.dp),
            contentAlignment = Alignment.BottomEnd
        ) {
            Text(
                text = expression,
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                color = MaterialTheme.colorScheme.onSurface,
                fontSize = 32.sp,
                textAlign = TextAlign.End
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        if (isLandscape) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(2f),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                CalculatorGrid(
                    keys = scientificKeys,
                    columns = 2,
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight(),
                    onKeyClick = ::handleKeyClick
                )

                CalculatorGrid(
                    keys = standardKeys,
                    columns = 4,
                    modifier = Modifier
                        .weight(2f)
                        .fillMaxHeight(),
                    onKeyClick = ::handleKeyClick
                )
            }
        } else {
            CalculatorGrid(
                keys = standardKeys,
                columns = 4,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(2f),
                onKeyClick = ::handleKeyClick
            )
        }
    }
}
