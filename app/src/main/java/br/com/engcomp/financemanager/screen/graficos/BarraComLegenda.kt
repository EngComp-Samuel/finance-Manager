package br.com.engcomp.financemanager.screen.graficos

import android.R.attr.textSize
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


import android.graphics.Canvas
import android.graphics.Color.BLACK
import androidx.compose.ui.graphics.Color
import android.graphics.Color.GRAY
import android.graphics.Paint
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.height

import androidx.compose.ui.graphics.nativeCanvas

import androidx.compose.ui.text.drawText
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

import kotlin.math.max


data class DadosBarraComLegenda(
    val label: String,
    val value: Float
)


@Composable
fun BarChartWithLegends(
    data: DadosBarraComLegenda,
    modifier: Modifier = Modifier
) {
    val listaDados = listOf(
        DadosBarraComLegenda("Receitas", 4500f),
        DadosBarraComLegenda("Despesas", 3800f))
    val barColors = listOf(Color(0xFF4CAF50), Color(0xFF2196F3), Color(0xFFFFC107))
    val maxValue = data.value.toFloat() ?: 0f

    Canvas(modifier = modifier.height(250.dp)) {
        val canvasWidth = size.width
        val canvasHeight = size.height
        val barWidth = (canvasWidth * 0.8f) / listaDados.size
        val padding = 40.dp.toPx()

        // Desenhar eixos
        drawLine(
            color = Color.Gray,
            start = Offset(padding, canvasHeight - padding),
            end = Offset(canvasWidth - padding, canvasHeight - padding),
            strokeWidth = 2.dp.toPx()
        )

        drawLine(
            color = Color.Gray,
            start = Offset(padding, padding),
            end = Offset(padding, canvasHeight - padding),
            strokeWidth = 2.dp.toPx()
        )

        // Desenhar barras e legendas
        listaDados.mapIndexed { index, (label, value) ->
            val barHeight = (value / maxValue) * (canvasHeight - 2 * padding)
            val left = padding + (index * (barWidth + 10.dp.toPx()))
            val top = canvasHeight - padding - barHeight

            // Barra
            drawRect(
                color = barColors[index % barColors.size],
                topLeft = Offset(left, top),
                size = Size(barWidth, barHeight)
            )

            // Legenda abaixo da barra
            drawContext.canvas.nativeCanvas.apply {
                drawText(
                    label,
                    left + (barWidth / 2) - 20.dp.toPx(), // Centralizado
                    canvasHeight - padding + 30.dp.toPx(), // Posição Y
                    Paint().apply {
                        color = android.graphics.Color.BLACK
                        textSize = 12.sp.toPx()
                        textAlign = android.graphics.Paint.Align.CENTER
                    }
                )

                // Valor no topo da barra
                drawText(
                    "%.1f".format(value),
                    left + (barWidth / 2) - 20.dp.toPx(),
                    top - 10.dp.toPx(),
                    Paint().apply {
                        color = android.graphics.Color.BLACK
                        textSize = 10.sp.toPx()
                        textAlign = android.graphics.Paint.Align.CENTER
                    }
                )
            }
        }

        // Legenda do eixo Y
        val yStep = maxValue / 5
        (0..5).forEach { step ->
            val yValue = step * yStep
            val yPos = canvasHeight - padding - ((yValue / maxValue) * (canvasHeight - 2 * padding))

            drawContext.canvas.nativeCanvas.drawText(
                "%.1f".format(yValue),
                padding - 30.dp.toPx(),
                yPos + 5.dp.toPx(), // Ajuste para alinhar com a linha
                Paint().apply {
                    color = android.graphics.Color.BLACK
                    textSize = 10.sp.toPx()
                    textAlign = android.graphics.Paint.Align.RIGHT
                }
            )

            // Linha de grade
            drawLine(
                color = Color.LightGray.copy(alpha = 0.3f),
                start = Offset(padding, yPos),
                end = Offset(canvasWidth - padding, yPos),
                strokeWidth = 1.dp.toPx()
            )
        }
    }
}