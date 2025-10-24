package br.com.engcomp.financemanager.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

import androidx.compose.ui.Modifier

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.PI
import kotlin.math.min


@Composable
fun OrcamentoScreen(){
    Graficos()
}


data class DonutSegmentUnico(
    val value: Float,
    val color: Color,
    val label: String
)

@Composable
fun DonutChart(
    segments: List<DonutSegmentUnico>,
    modifier: Modifier = Modifier,
    strokeWidth: Dp = 40.dp,
    holeRadiusRatio: Float = 2.4f,
    showLabels: Boolean = true,
    labelStyle: LabelStyleUnico = LabelStyleUnico()
) {
    val textMeasurer = rememberTextMeasurer()

    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxWidth().height(400.dp)) {
            val canvasSize = min(size.width, size.height)
            val center = Offset(size.width / 2, size.height / 2)

            // Calcular o raio do donut
            val maxRadius = canvasSize / 2
            val holeRadius = maxRadius * holeRadiusRatio
            val donutRadius = maxRadius - (strokeWidth.toPx() / 2)

            // Desenhar o donut único
            drawDonutWithLabels(
                segments = segments,
                center = center,
                radius = donutRadius,
                strokeWidth = strokeWidth.toPx(),
                showLabels = showLabels,
                labelStyle = labelStyle,
                textMeasurer = textMeasurer
            )
        }
    }
}

private fun DrawScope.drawDonutWithLabels(
    segments: List<DonutSegmentUnico>,
    center: Offset,
    radius: Float,
    strokeWidth: Float,
    showLabels: Boolean,
    labelStyle: LabelStyleUnico,
    textMeasurer: TextMeasurer
) {
    val totalValue = segments.sumOf { it.value.toDouble() }.toFloat()
    var startAngle = -90f // Começar do topo (12 horas)

    segments.forEach { segment ->
        val sweepAngle = (segment.value / totalValue) * 360f
        val midAngle = startAngle + (sweepAngle / 2)

        // Desenhar o arco do segmento
        drawArc(
            color = segment.color,
            startAngle = startAngle,
            sweepAngle = sweepAngle,
            useCenter = false,
            topLeft = Offset(center.x - radius, center.y - radius),
            size = Size(radius * 2, radius * 2),
            style = Stroke(width = strokeWidth)
        )

        // Desenhar label se habilitado
        if (showLabels && segment.value / totalValue >= labelStyle.minSegmentPercentage) {
            drawSegmentLabel(
                segment = segment,
                center = center,
                radius = radius,
                midAngle = midAngle,
                strokeWidth = strokeWidth,
                labelStyle = labelStyle,
                textMeasurer = textMeasurer,
                totalValue = totalValue
            )
        }

        startAngle += sweepAngle
    }
}
/*

private fun DrawScope.drawSegmentLabel(
    segment: DonutSegmentUnico,
    center: Offset,
    radius: Float,
    midAngle: Float,
    strokeWidth: Float,
    labelStyle: LabelStyleUnico,
    textMeasurer: TextMeasurer,
    totalValue: Float
) {
    // Converter ângulo para radianos
    val midAngleRad = (midAngle * PI / 180).toFloat()

    // Calcular a posição do label (no meio do arco, dentro da barra)
    val labelRadius = radius - (strokeWidth / 2) + labelStyle.verticalOffset

    // Posição do texto
    val textX = center.x + (1.3f*labelRadius * cos(midAngleRad))
    val textY = center.y + (1.3f*labelRadius * sin(midAngleRad))
   // val textCenterX = center.x
   // val textCenterY = center.y


    // Cor do texto baseada no contraste
    val textColor = if (labelStyle.autoTextColor) {
        getContrastColor(segment.color)
    } else {
        labelStyle.textColor
    }

    // Calcular porcentagem
    val percentage = (segment.value / totalValue * 100).toInt()

    // Preparar o texto
    val text = when (labelStyle.content) {
        LabelContentUnico.LABEL_ONLY -> segment.label
        LabelContentUnico.PERCENTAGE_ONLY -> "$percentage%"
        LabelContentUnico.BOTH -> "${segment.label}\n$percentage%"
    }

    val annotatedString = buildAnnotatedString {
        withStyle(
            SpanStyle(
                color = textColor,
                fontSize = labelStyle.fontSize.sp,
                fontWeight = labelStyle.fontWeight
            )
        ) {
            append(text)
        }
    }

    // Medir o texto
    val textLayoutResult = textMeasurer.measure(
        text = annotatedString,
        style = androidx.compose.ui.text.TextStyle(
            textAlign = TextAlign.Center
        )
    )

    // Desenhar o texto centralizado
    translate(left = textX - textLayoutResult.size.width / 2, top = textY - textLayoutResult.size.height / 2) {
        drawText(textLayoutResult)
    }
   */
/* translate(left = textCenterX - textLayoutResult.size.width / 2, top = textCenterY - textLayoutResult.size.height / 2) {
        drawText(textLayoutResult)
    }*//*

}
*/

// Versão simplificada para uso rápido
/*@Composable
fun SimpleDonutChart(
    modifier: Modifier = Modifier,
    strokeWidth: Dp = 90.dp,
    holeRadiusRatio: Float = 0.4f
) {
    val segments = listOf(
        DonutSegmentUnico(40f, Color(0xFF4CAF50), "Alimentação"),
        DonutSegmentUnico(30f, Color(0xFF2196F3), "Transporte"),
        DonutSegmentUnico(20f, Color(0xFFFFC107), "Lazer"),
        DonutSegmentUnico(10f, Color(0xFFF44336), "Outros")
    )

    DonutChart(
        segments = segments,
        modifier = modifier,
        strokeWidth = strokeWidth,
        holeRadiusRatio = holeRadiusRatio,
        showLabels = true,
        labelStyle = LabelStyleUnico(
            fontSize = 11f,
            content = LabelContentUnico.BOTH,
            verticalOffset = -2f
        )
    )
}*/

// Exemplo de uso com dados dinâmicos
@Composable
fun DonutChartWithTitle(
    title: String,
    segments: List<DonutSegmentUnico>,
    modifier: Modifier = Modifier,
    strokeWidth: Dp = 25.dp
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        DonutChart(
            segments = segments,
            modifier = Modifier.size(180.dp),
            strokeWidth = strokeWidth,
            showLabels = true,
            labelStyle = LabelStyleUnico(
                fontSize = 10f,
                content = LabelContentUnico.PERCENTAGE_ONLY
            )
        )
    }
}
////////////////////////////////////////////////////////////////////

// Enum para definir o tipo do gráfico
enum class ChartType {
    DESPESA, RECEITA
}

@Composable
fun Graficos() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(top = 150.dp, start = 5.dp, end = 5.dp, bottom = 50.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Orçamento")

        // Gráfico de Despesas
        DonutChartWithTitle(
            title = "Despesas Mensais",
            segments = listOf(
                DonutSegmentUnico(40f, Color(0xFF4CAF50), "Alimentação"),
                DonutSegmentUnico(30f, Color(0xFF2196F3), "Transporte"),
                DonutSegmentUnico(20f, Color(0xFFFFC107), "Lazer"),
                DonutSegmentUnico(10f, Color(0xFFF44336), "Outros")
            ),
            chartType = ChartType.DESPESA,
            strokeWidth = 60.dp
        )

        // Gráfico de Receitas
        DonutChartWithTitle(
            title = "Receitas Mensais",
            segments = listOf(
                DonutSegmentUnico(50f, Color(0xFF4CAF50), "Salário"),
                DonutSegmentUnico(25f, Color(0xFF2196F3), "Freelance"),
                DonutSegmentUnico(15f, Color(0xFFFFC107), "Investimentos"),
                DonutSegmentUnico(10f, Color(0xFF9C27B0), "Outros")
            ),
            chartType = ChartType.RECEITA,
            strokeWidth = 60.dp
        )

        // Gráfico simples
        SimpleDonutChart()
    }
}


@Composable
fun DonutChart(
    segments: List<DonutSegmentUnico>,
    chartType: ChartType = ChartType.DESPESA, // Tipo padrão é DESPESA
    modifier: Modifier = Modifier,
    strokeWidth: Dp = 40.dp,
    holeRadiusRatio: Float = 0.4f, // Corrigido para valor adequado
    showLabels: Boolean = true,
    labelStyle: LabelStyleUnico = LabelStyleUnico()
) {
    val textMeasurer = rememberTextMeasurer()

    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxWidth().height(400.dp)) {
            val canvasSize = min(size.width, size.height)
            val center = Offset(size.width / 2, size.height / 2)

            // Calcular o raio do donut
            val maxRadius = canvasSize / 2
            val holeRadius = maxRadius * holeRadiusRatio
            val donutRadius = maxRadius - (strokeWidth.toPx() / 2)

            // Desenhar o donut único
            drawDonutWithLabels(
                segments = segments,
                center = center,
                radius = donutRadius,
                strokeWidth = strokeWidth.toPx(),
                showLabels = showLabels,
                labelStyle = labelStyle,
                textMeasurer = textMeasurer,
                chartType = chartType
            )
        }
    }
}

private fun DrawScope.drawDonutWithLabels(
    segments: List<DonutSegmentUnico>,
    center: Offset,
    radius: Float,
    strokeWidth: Float,
    showLabels: Boolean,
    labelStyle: LabelStyleUnico,
    textMeasurer: TextMeasurer,
    chartType: ChartType
) {
    val totalValue = segments.sumOf { it.value.toDouble() }.toFloat()
    var startAngle = -90f // Começar do topo (12 horas)

    segments.forEach { segment ->
        val sweepAngle = (segment.value / totalValue) * 360f
        val midAngle = startAngle + (sweepAngle / 2)

        // Desenhar o arco do segmento
        drawArc(
            color = segment.color,
            startAngle = startAngle,
            sweepAngle = sweepAngle,
            useCenter = false,
            topLeft = Offset(center.x - radius, center.y - radius),
            size = Size(radius * 2, radius * 2),
            style = Stroke(width = strokeWidth)
        )

        // Desenhar label se habilitado
        if (showLabels && segment.value / totalValue >= labelStyle.minSegmentPercentage) {
            drawSegmentLabel(
                segment = segment,
                center = center,
                radius = radius,
                midAngle = midAngle,
                strokeWidth = strokeWidth,
                labelStyle = labelStyle,
                textMeasurer = textMeasurer,
                totalValue = totalValue
            )
        }

        startAngle += sweepAngle
    }

    // Desenhar o texto central (DESPESA/RECEITA)
    drawCenterText(
        chartType = chartType,
        center = center,
        totalValue = totalValue,
        textMeasurer = textMeasurer
    )
}

private fun DrawScope.drawCenterText(
    chartType: ChartType,
    center: Offset,
    totalValue: Float,
    textMeasurer: TextMeasurer
) {
    // Cor baseada no tipo (Vermelho para despesa, Verde para receita)
    val centerColor = when (chartType) {
        ChartType.DESPESA -> Color(0xFFF44336) // Vermelho
        ChartType.RECEITA -> Color(0xFF4CAF50) // Verde
    }

    // Texto principal (DESPESA/RECEITA)
    val mainText = when (chartType) {
        ChartType.DESPESA -> "DESPESA"
        ChartType.RECEITA -> "RECEITA"
    }

    // Texto secundário com o valor total
    val valueText = "R$ ${"%.2f".format(totalValue)}"

    // Criar texto principal
    val mainAnnotatedString = buildAnnotatedString {
        withStyle(
            SpanStyle(
                color = centerColor,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        ) {
            append(mainText)
        }
    }

    // Criar texto do valor
    val valueAnnotatedString = buildAnnotatedString {
        withStyle(
            SpanStyle(
                color = Color.Gray,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium
            )
        ) {
            append(valueText)
        }
    }

    // Medir textos
    val mainTextLayout = textMeasurer.measure(
        text = mainAnnotatedString,
        style = androidx.compose.ui.text.TextStyle(textAlign = TextAlign.Center)
    )

    val valueTextLayout = textMeasurer.measure(
        text = valueAnnotatedString,
        style = androidx.compose.ui.text.TextStyle(textAlign = TextAlign.Center)
    )

    // Calcular posição central
    val totalHeight = mainTextLayout.size.height + valueTextLayout.size.height + 8f
    val mainTextY = center.y - totalHeight / 2
    val valueTextY = mainTextY + mainTextLayout.size.height + 8f

    // Desenhar texto principal
    translate(
        left = center.x - mainTextLayout.size.width / 2,
        top = mainTextY
    ) {
        drawText(mainTextLayout)
    }

    // Desenhar texto do valor
    translate(
        left = center.x - valueTextLayout.size.width / 2,
        top = valueTextY
    ) {
        drawText(valueTextLayout)
    }
}

private fun DrawScope.drawSegmentLabel(
    segment: DonutSegmentUnico,
    center: Offset,
    radius: Float,
    midAngle: Float,
    strokeWidth: Float,
    labelStyle: LabelStyleUnico,
    textMeasurer: TextMeasurer,
    totalValue: Float
) {
    // Converter ângulo para radianos
    val midAngleRad = (midAngle * PI / 180).toFloat()

    // Calcular a posição do label (no meio do arco, dentro da barra)
    val labelRadius = radius - (strokeWidth / 2) + labelStyle.verticalOffset

    // Posição do texto
    val textX = center.x + (1.3f * labelRadius * cos(midAngleRad))
    val textY = center.y + (1.3f * labelRadius * sin(midAngleRad))

    // Cor do texto baseada no contraste
    val textColor = if (labelStyle.autoTextColor) {
        getContrastColor(segment.color)
    } else {
        labelStyle.textColor
    }

    // Calcular porcentagem
    val percentage = (segment.value / totalValue * 100).toInt()

    // Preparar o texto
    val text = when (labelStyle.content) {
        LabelContentUnico.LABEL_ONLY -> segment.label
        LabelContentUnico.PERCENTAGE_ONLY -> "$percentage%"
        LabelContentUnico.BOTH -> "${segment.label}\n$percentage%"
    }

    val annotatedString = buildAnnotatedString {
        withStyle(
            SpanStyle(
                color = textColor,
                fontSize = labelStyle.fontSize.sp,
                fontWeight = labelStyle.fontWeight
            )
        ) {
            append(text)
        }
    }

    // Medir o texto
    val textLayoutResult = textMeasurer.measure(
        text = annotatedString,
        style = androidx.compose.ui.text.TextStyle(
            textAlign = TextAlign.Center
        )
    )

    // Desenhar o texto centralizado
    translate(left = textX - textLayoutResult.size.width / 2, top = textY - textLayoutResult.size.height / 2) {
        drawText(textLayoutResult)
    }
}

private fun getContrastColor(backgroundColor: Color): Color {
    val luminance = 0.299 * backgroundColor.red + 0.587 * backgroundColor.green + 0.114 * backgroundColor.blue
    return if (luminance > 0.5) Color.Black else Color.White
}

data class LabelStyleUnico(
    val fontSize: Float = 12f,
    val textColor: Color = Color.Black,
    val autoTextColor: Boolean = true,
    val fontWeight: FontWeight = FontWeight.Medium,
    val verticalOffset: Float = 0f, // Ajuste vertical dentro da barra
    val minSegmentPercentage: Float = 0.05f, // Mínimo 5% para mostrar label
    val content: LabelContentUnico = LabelContentUnico.PERCENTAGE_ONLY
)

enum class LabelContentUnico {
    LABEL_ONLY,
    PERCENTAGE_ONLY,
    BOTH
}

// Versão simplificada para uso rápido
@Composable
fun SimpleDonutChart(
    modifier: Modifier = Modifier,
    strokeWidth: Dp = 60.dp,
    holeRadiusRatio: Float = 0.4f
) {
    val segments = listOf(
        DonutSegmentUnico(40f, Color(0xFF4CAF50), "Alimentação"),
        DonutSegmentUnico(30f, Color(0xFF2196F3), "Transporte"),
        DonutSegmentUnico(20f, Color(0xFFFFC107), "Lazer"),
        DonutSegmentUnico(10f, Color(0xFFF44336), "Outros")
    )

    DonutChart(
        segments = segments,
        chartType = ChartType.DESPESA,
        modifier = modifier,
        strokeWidth = strokeWidth,
        holeRadiusRatio = holeRadiusRatio,
        showLabels = true,
        labelStyle = LabelStyleUnico(
            fontSize = 11f,
            content = LabelContentUnico.BOTH,
            verticalOffset = -2f
        )
    )
}

// Exemplo de uso com dados dinâmicos
@Composable
fun DonutChartWithTitle(
    title: String,
    segments: List<DonutSegmentUnico>,
    chartType: ChartType = ChartType.DESPESA,
    modifier: Modifier = Modifier,
    strokeWidth: Dp = 60.dp
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        DonutChart(
            segments = segments,
            chartType = chartType,
            modifier = Modifier.size(250.dp),
            strokeWidth = strokeWidth,
            showLabels = true,
            labelStyle = LabelStyleUnico(
                fontSize = 10f,
                content = LabelContentUnico.PERCENTAGE_ONLY
            )
        )
    }
}

////////////////////////////////////////////////////////////////////