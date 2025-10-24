package br.com.engcomp.financemanager.screen.graficos

import android.graphics.Canvas
import android.graphics.Color.BLACK
import androidx.compose.ui.graphics.Color
import android.graphics.Color.GRAY
import android.graphics.Paint
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.nativeCanvas

import androidx.compose.ui.text.drawText
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

import kotlin.math.max


@Composable
fun BarChart(
    income: Float,
    expenses: Float,
    modifier: Modifier = Modifier
) {
    val maxValue = max(income, expenses) * 1.2f
    val barWidth = 60.dp

    Canvas(modifier = modifier.height(300.dp)) {
        val canvasHeight = size.height
        val canvasWidth = size.width

        // Eixo horizontal
        drawLine(
            color = androidx.compose.ui.graphics.Color.Gray,
            start = Offset(0f, canvasHeight),
            end = Offset(canvasWidth, canvasHeight),
            strokeWidth = 2.dp.toPx()
        )

        // Barras
        val incomeHeight = (income / maxValue) * canvasHeight
        val expensesHeight = (expenses / maxValue) * canvasHeight

        // Barra de receitas (verde)
        drawRect(
            color = Color(0xFF4CAF50),
            topLeft = Offset(
                x = (canvasWidth / 4) - (barWidth.toPx() / 2),
                y = canvasHeight - incomeHeight
            ),
            size = Size(barWidth.toPx(), incomeHeight)
        )

        // Barra de despesas (vermelho)
        drawRect(
            color = Color(0xFFF44336),
            topLeft = Offset(
                x = (3 * canvasWidth / 4) - (barWidth.toPx() / 2),
                y = canvasHeight - expensesHeight
            ),
            size = Size(barWidth.toPx(), expensesHeight)
        )

        // Legendas
        drawContext.canvas.nativeCanvas.apply {
            drawText(
                "Receitas",
                (canvasWidth / 4) - 30.dp.toPx(),
                canvasHeight + 30.dp.toPx(),
                Paint().apply {
                    color = android.graphics.Color.BLACK
                    textSize = 12.sp.toPx()
                    textAlign = Paint.Align.CENTER
                }
            )

            drawText(
                "Despesas",
                ( canvasWidth / 4) - 30.dp.toPx(),
                canvasHeight + 30.dp.toPx(),
                Paint().apply {
                    color = android.graphics.Color.BLACK
                    textSize = 12.sp.toPx()
                    textAlign = Paint.Align.CENTER
                }
            )
        }
    }
}