package br.com.engcomp.financemanager.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

import androidx.compose.foundation.Canvas

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.min
import androidx.compose.ui.unit.sp
import kotlin.math.cos
import kotlin.math.min
import kotlin.math.sin
import androidx.compose.ui.text.style.TextAlign
import br.com.engcomp.financemanager.screen.componentes.CardsPainel
import br.com.engcomp.financemanager.screen.componentes.CardsPainelInfoApp

import kotlin.math.PI

data class DonutSegmentWithHome(
    val value: Float,
    val color: Color,
    val label: String,
    val strokeWidth: Dp = 20.dp
)

@Composable
fun NestedDonutChartWithInnerLabels(
    segments: List<List<DonutSegmentWithHome>>,
    modifier: Modifier = Modifier,
    holeRadiusRatio: Float = 0.4f,
    showLabels: Boolean = true,
    labelStyle: LabelStyle = LabelStyle()
) {
    val textMeasurer = rememberTextMeasurer()

    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val canvasSize = min(size.width, size.height)
            val center = Offset(size.width / 2, size.height / 2)

            // Calcular o raio máximo baseado no número de anéis
            val maxRadius = canvasSize / 2
            val holeRadius = maxRadius * holeRadiusRatio

            // Espaço disponível para os anéis (raio máximo - raio do buraco)
            val availableSpace = maxRadius - holeRadius

            // Calcular a espessura de cada anel
            val ringThickness = availableSpace / segments.size

            // Desenhar cada anel de fora para dentro
            segments.forEachIndexed { ringIndex, ringSegments ->
                val currentRingRadius = maxRadius - (ringIndex * ringThickness)
                val strokeWidth = ringThickness

                drawDonutRingWithLabels(
                    segments = ringSegments,
                    center = center,
                    radius = currentRingRadius,
                    strokeWidth = strokeWidth,
                    showLabels = showLabels,
                    labelStyle = labelStyle,
                    ringIndex = ringIndex,
                    totalRings = segments.size,
                    textMeasurer = textMeasurer
                )
            }
        }
    }
}

private fun DrawScope.drawDonutRingWithLabels(
    segments: List<DonutSegmentWithHome>,
    center: Offset,
    radius: Float,
    strokeWidth: Float,
    showLabels: Boolean,
    labelStyle: LabelStyle,
    ringIndex: Int,
    totalRings: Int,
    textMeasurer: TextMeasurer
) {
    val totalValue = segments.sumOf { it.value.toDouble() }.toFloat()
    var startAngle = -90f // Começar do topo (12 horas)

    segments.forEach { segment ->
        val sweepAngle = (segment.value / totalValue) * 360f
        val midAngle = startAngle + (sweepAngle / 2)

        // Desenhar o arco
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
                ringIndex = ringIndex,
                totalRings = totalRings,
                textMeasurer = textMeasurer
            )
        }

        startAngle += sweepAngle
    }
}

private fun DrawScope.drawSegmentLabel(
    segment: DonutSegmentWithHome,
    center: Offset,
    radius: Float,
    midAngle: Float,
    strokeWidth: Float,
    labelStyle: LabelStyle,
    ringIndex: Int,
    totalRings: Int,
    textMeasurer: TextMeasurer
) {
    // Converter ângulo para radianos
    val midAngleRad = (midAngle * PI / 180).toFloat()

    // Calcular a posição do label (no meio do arco, dentro da barra)
    val labelRadius = radius + (strokeWidth/2) - labelStyle.verticalOffset - 60

    // Posição do texto
    val textX = center.x + (labelRadius * cos(midAngleRad))
    val textY = center.y + (labelRadius * sin(midAngleRad))

    // Cor do texto baseada no contraste
    val textColor = if (labelStyle.autoTextColor) {
        getContrastColor(segment.color)
    } else {
        labelStyle.textColor
    }

    // Tamanho da fonte baseado no índice do anel (anéis internos têm fonte menor)
    val fontSize = when (labelStyle.fontScaling) {
        FontScaling.UNIFORM -> labelStyle.fontSize
        FontScaling.PROPORTIONAL -> {
            val scaleFactor = 1.0f - (ringIndex.toFloat() / totalRings.toFloat()) * 0.4f
            labelStyle.fontSize * scaleFactor
        }
    }

    // Preparar o texto
    val percentage = (segment.value * 100).toInt()
    val text = when (labelStyle.content) {
        LabelContent.LABEL_ONLY -> segment.label
        LabelContent.PERCENTAGE_ONLY -> "$percentage%"
        LabelContent.BOTH -> "${segment.label}\n$percentage%"
    }

    val annotatedString = buildAnnotatedString {
        withStyle(
            SpanStyle(
                color = textColor,
                fontSize = fontSize.sp,
                fontWeight = labelStyle.fontWeight
            )
        ) {
            append(text)
        }
    }

    // Medir o texto
    val textLayoutResult = textMeasurer.measure(
        text = annotatedString,
        style = TextStyle(
            textAlign = TextAlign.Center
        )
    )

    // Desenhar o texto centralizado
    translate(left = textX - textLayoutResult.size.width, top = textY - textLayoutResult.size.height) {
        drawText(textLayoutResult)
    }
}

private fun getContrastColor(backgroundColor: Color): Color {
    val luminance = 0.299 * backgroundColor.red + 0.587 * backgroundColor.green + 0.114 * backgroundColor.blue
    return if (luminance > 0.5) Color.Black else Color.White
}

data class LabelStyle(
    val fontSize: Float = 11f,
    val textColor: Color = Color.Black,
    val autoTextColor: Boolean = true,
    val fontWeight: FontWeight = FontWeight.Medium,
    val verticalOffset: Float = 0f, // Ajuste vertical dentro da barra
    val minSegmentPercentage: Float = 0.05f, // Mínimo 5% para mostrar label
    val content: LabelContent = LabelContent.BOTH,
    val fontScaling: FontScaling = FontScaling.PROPORTIONAL
)

enum class LabelContent {
    LABEL_ONLY,
    PERCENTAGE_ONLY,
    BOTH
}

enum class FontScaling {
    UNIFORM,
    PROPORTIONAL
}

// Versão simplificada para uso rápido
@Composable
fun SimpleNestedDonutChartWithInnerLabels(
    modifier: Modifier = Modifier,
    holeRadiusRatio: Float = 0.4f
) {
    val rings = listOf(
        listOf(
            DonutSegmentWithHome(0.35f, Color(0xFF4CAF50), "Alimentação", 10.dp),
            DonutSegmentWithHome(0.25f, Color(0xFF2196F3), "Transporte", 10.dp),
            DonutSegmentWithHome(0.20f, Color(0xFFFFC107), "Lazer", 10.dp),
            DonutSegmentWithHome(0.20f, Color(0xFFF44336), "Outros", 10.dp)
        ),
        listOf(
            DonutSegmentWithHome(0.40f, Color(0xFF8BC34A), "Salário", 25.dp),
            DonutSegmentWithHome(0.30f, Color(0xFF03A9F4), "Investimentos", 25.dp),
            DonutSegmentWithHome(0.30f, Color(0xFFFF9800), "Extra", 25.dp)
        ),
        listOf(
            DonutSegmentWithHome(0.60f, Color(0xFFCDDC39), "Pessoal", 20.dp),
            DonutSegmentWithHome(0.40f, Color(0xFF00BCD4), "Família", 20.dp)
        )
    )

    NestedDonutChartWithInnerLabels(
        segments = rings,
        modifier = modifier,
        holeRadiusRatio = holeRadiusRatio,
        showLabels = true,
        labelStyle = LabelStyle(
            fontSize = 10f,
            content = LabelContent.BOTH,
            verticalOffset = -5f // Ajuste para centralizar melhor
        )
    )
}


@Composable
fun HomeScreen(){
    Column(
        modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center) {

        CardsPainelInfoApp(
            texto = "Inf dos contatos aqui"
        )
        Row(modifier = Modifier.fillMaxWidth(0.9f).padding(bottom = 10.dp)){
            CardsPainel(modifier = Modifier.padding(10.dp), valor = 100f, label = "Receitas")
            CardsPainel(modifier = Modifier.padding(10.dp), valor = -100f, label = "Despesas")
            CardsPainel(modifier = Modifier.padding(10.dp), valor = 100f, label = "Saldo")
        }
        Row(modifier = Modifier.fillMaxWidth(0.9f).padding(bottom = 50.dp)){
            CardsPainel(modifier = Modifier.padding(10.dp), valor = 100f, label = "Receitas")
            CardsPainel(modifier = Modifier.padding(10.dp), valor = -100f, label = "Despesas")
            CardsPainel(modifier = Modifier.padding(10.dp), valor = 100f, label = "Saldo")
        }

        SimpleNestedDonutChartWithInnerLabels(
            modifier = Modifier.size(300.dp).padding(bottom = 10.dp),
            holeRadiusRatio = 0.3f
        )
    }
}

@Composable
fun PainelIndicadores(){

}



