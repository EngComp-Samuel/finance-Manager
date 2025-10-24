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
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.graphics.Path

import kotlin.math.max


@Composable
fun LineChart(
    monthlyData: List<Pair<String, Float>>,
    modifier: Modifier = Modifier,
    isIncome: Boolean = true
) {
    val maxValue = monthlyData.maxOfOrNull { it.second } ?: 0f
    val minValue = monthlyData.minOfOrNull { it.second } ?: 0f
    val range = maxValue - minValue

    Canvas(modifier = modifier.height(200.dp)) {
        val canvasHeight = size.height
        val canvasWidth = size.width
        val padding = 40.dp.toPx()
        val graphHeight = canvasHeight - (2 * padding)

        // Eixos
        drawLine(
            color = Color.Gray,
            start = Offset(padding, padding),
            end = Offset(padding, canvasHeight - padding),
            strokeWidth = 2.dp.toPx()
        )

        drawLine(
            color = Color.Gray,
            start = Offset(padding, canvasHeight - padding),
            end = Offset(canvasWidth - padding, canvasHeight - padding),
            strokeWidth = 2.dp.toPx()
        )

        // Linha do gráfico
        val points = monthlyData.mapIndexed { index, pair ->
            val x = padding + (index.toFloat() / (monthlyData.size - 1)) * (canvasWidth - 2 * padding)
            val y = if (range > 0) {
                canvasHeight - padding - ((pair.second - minValue) / range) * graphHeight
            } else {
                canvasHeight / 2
            }
            Offset(x, y)
        }

        // Desenha a linha
        drawPath(
            path = Path().apply {
                points.forEachIndexed { index, point ->
                    if (index == 0) {
                        moveTo(point.x, point.y)
                    } else {
                        lineTo(point.x, point.y)
                    }
                }
            },
            color = if (isIncome) Color(0xFF4CAF50) else Color(0xFFF44336),
            style = Stroke(
                width = 3.dp.toPx(),
                cap = StrokeCap.Round,
                join = StrokeJoin.Round
            )
        )

        // Desenha os pontos
        points.forEach { point ->
            drawCircle(
                color = if (isIncome) Color(0xFF2E7D32) else Color(0xFFC62828),
                center = point,
                radius = 5.dp.toPx()
            )
        }

        // Marcadores do eixo X (meses)
        monthlyData.forEachIndexed { index, pair ->
            val x = padding + (index.toFloat() / (monthlyData.size - 1)) * (canvasWidth - 2 * padding)

            drawContext.canvas.nativeCanvas.apply {
                drawText(
                    pair.first,
                    x - 20.dp.toPx(),
                    canvasHeight - padding + 30.dp.toPx(),
                    Paint().apply {
                        color = android.graphics.Color.BLACK
                        textSize = 10.sp.toPx()
                        textAlign = android.graphics.Paint.Align.CENTER
                    }
                )
            }
        }

        // Marcadores do eixo Y (valores)
        val yStep = range / 4
        (0..4).forEach { step ->
            val yValue = minValue + (step * yStep)
            val y = canvasHeight - padding - (step.toFloat() / 4) * graphHeight

            drawContext.canvas.nativeCanvas.apply {
                drawText(
                    "%.2f".format(yValue),
                    padding - 30.dp.toPx(),
                    y + 5.dp.toPx(),
                    Paint().apply {
                        color = android.graphics.Color.BLACK
                        textSize = 10.sp.toPx()
                        textAlign = android.graphics.Paint.Align.RIGHT
                    }
                )
            }

            drawLine(
                color = Color.LightGray.copy(alpha = 0.5f),
                start = Offset(padding, y),
                end = Offset(canvasWidth - padding, y),
                strokeWidth = 1.dp.toPx()
            )
        }
    }
}