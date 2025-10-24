package br.com.engcomp.financemanager.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp



@Composable
fun TransacaoScreen(){
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center) {
        Text("TRansacaoes")

        SimpleVerticalBarChartCard()

    }
}



data class MonthlyData(
    val month: String,
    val value: Float,
    val color: Color = Color(0xFF2196F3)
)

data class LegendItemTransaction(
    val color: Color,
    val label: String,
    val value: String
)

@Composable
fun VerticalBarChartCard(
    monthlyData: List<MonthlyData>,
    modifier: Modifier = Modifier,
    barWidth: Dp = 5.dp,
    maxBarHeight: Dp = 150.dp,
    showValues: Boolean = true,
    showLegend: Boolean = true
) {
    Card(
        modifier = modifier,
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Título do gráfico
            Text(
                text = "Vendas Mensais",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // Gráfico de barras
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(maxBarHeight + 40.dp) // Altura máxima + espaço para labels
            ) {
                VerticalBarChart(
                    monthlyData = monthlyData,
                    barWidth = barWidth,
                    maxBarHeight = maxBarHeight,
                    showValues = showValues,
                    modifier = Modifier.fillMaxSize()
                )
            }

            // Legenda
            if (showLegend) {
                Spacer(modifier = Modifier.height(16.dp))
                LegendView(
                    monthlyData = monthlyData,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            // Informações adicionais
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Total: R$ ${"%.2f".format(monthlyData.sumOf { it.value.toDouble() })}",
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Text(
                    text = "Média: R$ ${"%.2f".format(monthlyData.sumOf { it.value.toDouble() } / monthlyData.size)}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            //Mais informacoes adicionais
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Total: R$ ${"%.2f".format(monthlyData.sumOf { it.value.toDouble() })}",
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Text(
                    text = "Média: R$ ${"%.2f".format(monthlyData.sumOf { it.value.toDouble() } / monthlyData.size)}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }


        }
    }
}

@Composable
fun VerticalBarChart(
    monthlyData: List<MonthlyData>,
    modifier: Modifier = Modifier,
    barWidth: Dp = 3.dp,
    maxBarHeight: Dp = 150.dp,
    showValues: Boolean = true
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.BottomCenter
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val maxValue = monthlyData.maxOfOrNull { it.value } ?: 1f
            val canvasHeight = size.height - 30f // Reserva espaço para labels dos meses
            val canvasWidth = size.width
            val barSpacing = (canvasWidth - (barWidth.toPx() * monthlyData.size)) / (monthlyData.size + 1)

            // Linha de base
            drawLine(
                color = Color.Gray.copy(alpha = 0.3f),
                start = Offset(0f, canvasHeight),
                end = Offset(canvasWidth, canvasHeight),
                strokeWidth = 1f
            )

            // Desenhar cada barra
            monthlyData.forEachIndexed { index, data ->
                val barHeight = (data.value / maxValue) * (canvasHeight - 20f)
                val xPosition = barSpacing + (barSpacing + barWidth.toPx()) * index
                val yPosition = canvasHeight - barHeight

                // Desenhar a barra
                drawRect(
                    color = data.color,
                    topLeft = Offset(xPosition, yPosition),
                    size = Size(barWidth.toPx(), barHeight)
                )

                // Desenhar o valor acima da barra
                if (showValues && data.value > 0) {
                    drawValueLabel(
                        value = data.value,
                        position = Offset(xPosition + barWidth.toPx() / 2, yPosition - 10f),
                        color = data.color
                    )
                }

                // Desenhar label do mês
                drawMonthLabel(
                    month = data.month,
                    position = Offset(xPosition + barWidth.toPx() / 2, canvasHeight + 15f)
                )
            }

            // Grade de referência
            drawGridLines(canvasHeight, canvasWidth, maxValue)
        }
    }
}

//TODO: MODIFICAR A COR PARA FICAR VISIVEL NO MODO DARK
private fun DrawScope.drawValueLabel(value: Float, position: Offset, color: Color) {
    val text = "%.0f".format(value)

    val textPaint = android.graphics.Paint().apply {
        this.color = android.graphics.Color.argb(255, 100, 100, 100)
        textSize = 20f
        textAlign = android.graphics.Paint.Align.CENTER
    }

    drawContext.canvas.nativeCanvas.drawText(
        text,
        position.x,
        position.y,
        textPaint
    )
}

//TODO: MODIFICAR A COR PARA FICAR VISIVEL NO MODO DARK
private fun DrawScope.drawMonthLabel(month: String, position: Offset) {
    val textPaint = android.graphics.Paint().apply {
        color = android.graphics.Color.argb(255, 100, 100, 100)
        textSize = 20f
        textAlign = android.graphics.Paint.Align.CENTER
    }

    drawContext.canvas.nativeCanvas.drawText(
        month,
        position.x,
        position.y,
        textPaint
    )
}

private fun DrawScope.drawGridLines(canvasHeight: Float, canvasWidth: Float, maxValue: Float) {
    val gridSteps = 5
    val stepValue = maxValue / gridSteps

    for (i in 1..gridSteps) {
        val yPosition = canvasHeight - (i * (canvasHeight - 20f) / gridSteps)
        val value = stepValue * i

        // Linha de grade
        drawLine(
            color = Color.Gray.copy(alpha = 0.2f),
            start = Offset(0f, yPosition),
            end = Offset(canvasWidth, yPosition),
            strokeWidth = 0.5f
        )

        //TODO: MODIFICAR A LABEL DO VALOR PARA QUE SEJA VISIVEL NO MODO DARK
        // Label do valor
        val textPaint = android.graphics.Paint().apply {
            color = android.graphics.Color.argb(255, 150, 150, 150)
            textSize = 12f
            textAlign = android.graphics.Paint.Align.LEFT
        }

        drawContext.canvas.nativeCanvas.drawText(
            "%.0f".format(value),
            5f,
            yPosition - 5f,
            textPaint
        )
    }
}

@Composable
fun LegendView(
    monthlyData: List<MonthlyData>,
    modifier: Modifier = Modifier
) {
    val legendItems = monthlyData.map { data ->
        LegendItemTransaction(
            color = data.color,
            label = data.month,
            value = "R$ ${"%.2f".format(data.value)}"
        )
    }

    Column(
        modifier = modifier
    ) {
        Text(
            text = "Legenda:",
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(legendItems) { item ->
                LegendItemRow(legendItem = item)
            }
        }
    }
}

@Composable
fun LegendItemRow(
    legendItem: LegendItemTransaction
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // Indicador de cor
        Box(
            modifier = Modifier
                .size(12.dp)
                .background(legendItem.color)
        )

        Column {
            Text(
                text = legendItem.label,
                style = MaterialTheme.typography.bodySmall,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = legendItem.value,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontSize = 10.sp
            )
        }
    }
}

// Dados de exemplo
fun getSampleMonthlyData(): List<MonthlyData> {
    return listOf(
        MonthlyData("Jan", 1500f, Color(0xFF4CAF50)),
        MonthlyData("Fev", 1800f, Color(0xFF2196F3)),
        MonthlyData("Mar", 2200f, Color(0xFFFFC107)),
        MonthlyData("Abr", 1900f, Color(0xFFF44336)),
        MonthlyData("Mai", 2500f, Color(0xFF9C27B0)),
        MonthlyData("Jun", 2800f, Color(0xFF00BCD4)),
        MonthlyData("Jul", 3200f, Color(0xFF8BC34A)),
        MonthlyData("Ago", 3000f, Color(0xFFFF9800)),
        MonthlyData("Set", 2700f, Color(0xFF795548)),
        MonthlyData("Out", 3100f, Color(0xFF607D8B)),
        MonthlyData("Nov", 3500f, Color(0xFFE91E63)),
        MonthlyData("Dez", 4000f, Color(0xFF3F51B5))
    )
}

// Versão simplificada para uso rápido
@Composable
fun SimpleVerticalBarChartCard(
    modifier: Modifier = Modifier
) {
    val sampleData = listOf(
        MonthlyData("Jan", 1200f),
        MonthlyData("Fev", 1600f),
        MonthlyData("Mar", 2000f),
        MonthlyData("Abr", 1800f),
        MonthlyData("Mai", 2400f),
        MonthlyData("Jun", 2200f),
        MonthlyData("Jul", 2200f),
        MonthlyData("Ago", 2200f),
        MonthlyData("Set", 2200f),
        MonthlyData("Out", 2200f),
        MonthlyData("Nov", 2200f),
        MonthlyData("Dez", 2200f)
    )

    val dadosComCores = getSampleMonthlyData()


    VerticalBarChartCard(
        monthlyData = dadosComCores,//sampleData,
        modifier = modifier,
        barWidth = 10.dp,
        maxBarHeight = 100.dp
    )
}

@Preview(showBackground = true)
@Composable
fun SimpleVerticalBarChartCardPreview() {
    MaterialTheme {
        SimpleVerticalBarChartCard(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        )
    }
}