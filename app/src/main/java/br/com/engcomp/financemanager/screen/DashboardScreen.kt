package br.com.engcomp.financemanager.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import br.com.engcomp.financemanager.screen.graficos.BarChart
import br.com.engcomp.financemanager.screen.graficos.BarChartWithLegends
import br.com.engcomp.financemanager.screen.graficos.DadosBarraComLegenda
import br.com.engcomp.financemanager.screen.graficos.DadosParaPizza
import br.com.engcomp.financemanager.screen.graficos.LineChart
import br.com.engcomp.financemanager.screen.graficos.PieChart

import androidx.compose.foundation.Canvas

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.min
import kotlin.math.min


import androidx.compose.foundation.layout.Row

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items

import androidx.compose.ui.text.font.FontWeight


//import br.com.engcomp.financemanager.screen.graficos.PieChart

@Composable
fun DashBoardScreen(){
    /*Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center) {
        Text("Dashboard")
    }*/
    AnalyticsScreen()
}

@Composable
fun AnalyticsScreen() {
    val sampleIncome = 4500f
    val sampleExpenses = 3800f

    val categories = mapOf(
        "Alimentação" to 1200f,
        "Moradia" to 1500f,
        "Transporte" to 500f,
        "Lazer" to 300f,
        "Outros" to 300f
    )

    val monthlyData = listOf(
        "Jan" to 3200f,
        "Fev" to 2900f,
        "Mar" to 3500f,
        "Abr" to 4100f,
        "Mai" to 3800f,
        "Jun" to 4500f
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(top = 150.dp, start = 5.dp, end = 5.dp, bottom = 50.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        Text(
            text = "Resumo Financeiro",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        Card {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Receitas vs Despesas",
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(8.dp))
                BarChart(
                    income = sampleIncome,
                    expenses = sampleExpenses,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp)
                )
            }
        }

        val dadosPizza = DadosParaPizza("A", 100f)
        Card {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Distribuição de Gastos",
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(8.dp))
                PieChart(
                    categories = dadosPizza,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(350.dp)
                )
            }
        }

        Card {
            Column(modifier = Modifier.padding(20.dp)) {
                Text(
                    text = "Evolução Mensal",
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(8.dp))
                LineChart(
                    monthlyData = monthlyData,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(250.dp)
                )
            }
        }

        val dados = DadosBarraComLegenda("Receitas", 4500f)


        Text("Desempenho Mensal", style = MaterialTheme.typography.headlineSmall)
        Spacer(modifier = Modifier.height(16.dp))
        BarChartWithLegends(
            data = dados,
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
        )

        Card {
            Column(
                modifier = Modifier.padding(16.dp).fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center) {
                SimpleNestedDonutChart(
                    modifier = Modifier.size(300.dp).padding(16.dp),
                    holeRadiusRatio = 0.3f
                )
            }
        }

        val outerRingSegments = listOf(
            DonutSegment(40f, Color(0xFF4CAF50), 25.dp), // Verde
            DonutSegment(30f, Color(0xFF2196F3), 25.dp, "BLUE"), // Azul
            DonutSegment(20f, Color(0xFFFFC107), 25.dp, "YELLOW"), // Amarelo
            DonutSegment(10f, Color(0xFFF44336), 25.dp, "RED")  // Vermelho
        )

        // Dados para o anel do meio
        val middleRingSegments = listOf(
            DonutSegment(50f, Color(0xFF8BC34A), 20.dp), // Verde claro
            DonutSegment(25f, Color(0xFF03A9F4), 20.dp), // Azul claro
            DonutSegment(15f, Color(0xFFFF9800), 20.dp), // Laranja
            DonutSegment(10f, Color(0xFFE91E63), 20.dp)  // Rosa
        )

        // Dados para o anel interno
        val innerRingSegments = listOf(
            DonutSegment(60f, Color(0xFFCDDC39), 15.dp), // Lima
            DonutSegment(25f, Color(0xFF00BCD4), 15.dp), // Ciano
            DonutSegment(15f, Color(0xFFFF5722), 15.dp)  // Laranja escuro
        )

        val allSegments = listOf(outerRingSegments, middleRingSegments, innerRingSegments)

        NestedDonutChartWithLegend(
            segments = allSegments,
            modifier = Modifier.size(300.dp),
            legendPosition = LegendPosition.Bottom
        )

    }
}

//CRIANDO GRAFICO DE DONUT - UM DENTRO DO OUTRO - INICIO

data class DonutSegment(
    val value: Float,
    val color: Color,
    val strokeWidth: Dp = 20.dp,
    val label: String? = null
)

@Composable
fun NestedDonutChart(
    segments: List<List<DonutSegment>>,
    modifier: Modifier = Modifier,
    holeRadiusRatio: Float = 0.4f
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.size(min(600.dp, 600.dp))) {
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

                drawDonutRing(
                    segments = ringSegments,
                    center = center,
                    radius = currentRingRadius,
                    strokeWidth = strokeWidth
                )
            }
        }
    }
}

private fun DrawScope.drawDonutRing(
    segments: List<DonutSegment>,
    center: Offset,
    radius: Float,
    strokeWidth: Float
) {
    val totalValue = segments.sumOf { it.value.toDouble() }.toFloat()
    var startAngle = -90f // Começar do topo (12 horas)

    segments.forEach { segment ->
        val sweepAngle = (segment.value / totalValue) * 360f

        drawArc(
            color = segment.color,
            startAngle = startAngle,
            sweepAngle = sweepAngle,
            useCenter = false,
            topLeft = Offset(center.x - radius, center.y - radius),
            size = Size(radius * 2, radius * 2),
            style = Stroke(width = strokeWidth)
        )

        startAngle += sweepAngle
    }
}



// Versão alternativa com configuração mais simples
@Composable
fun SimpleNestedDonutChart(
    modifier: Modifier = Modifier,
    holeRadiusRatio: Float = 0.4f
) {
    val rings = listOf(
        listOf(
            DonutSegment(35f, Color(0xFF4CAF50), 30.dp, "GREEN"),
            DonutSegment(25f, Color(0xFF2196F3), 30.dp, "BLUE"),
            DonutSegment(20f, Color(0xFFFFC107), 30.dp, "YELLOW"),
            DonutSegment(20f, Color(0xFFF44336), 30.dp, "RED")
        ),
        listOf(
            DonutSegment(40f, Color(0xFF8BC34A), 25.dp),
            DonutSegment(30f, Color(0xFF03A9F4), 25.dp),
            DonutSegment(30f, Color(0xFFFF9800), 25.dp)
        ),
        listOf(
            DonutSegment(60f, Color(0xFFCDDC39), 20.dp),
            DonutSegment(40f, Color(0xFF00BCD4), 20.dp)
        )
    )

    NestedDonutChart(
        segments = rings,
        modifier = modifier,
        holeRadiusRatio = holeRadiusRatio
    )
}


//CRIANDO GRAFICO DE DONUT - UM DENTRO DO OUTRO - FIM

//GRAFICO DONUT COM LEGENDA - INICIO

data class DonutSegmentWithLegend(
    val value: Float,
    val color: Color,
    val label: String, // Novo campo para a legenda
    val strokeWidth: Dp = 20.dp
)

data class LegendItem(
    val color: Color,
    val label: String?,
    val value: Float,
    val percentage: String
)

@Composable
fun NestedDonutChartWithLegend(
    segments: List<List<DonutSegment>>,
    modifier: Modifier = Modifier,
    holeRadiusRatio: Float = 0.4f,
    showLegend: Boolean = true,
    legendPosition: LegendPosition = LegendPosition.Bottom,
    chartToLegendRatio: Float = 0.7f // Proporção do gráfico vs legenda
) {
    // Calcular porcentagens para cada segmento
    val segmentsWithPercentages = segments.map { ringSegments ->
        val totalValue = ringSegments.sumOf { it.value.toDouble() }.toFloat()
        ringSegments.map { segment ->
            val percentage = (segment.value / totalValue * 100).let {
                if (it.isNaN()) 0f else it
            }
            segment to percentage
        }
    }

    when (legendPosition) {
        LegendPosition.Bottom -> {
            Column(
                modifier = modifier,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Gráfico
                Box(
                    modifier = Modifier
                        .weight(chartToLegendRatio)
                        .fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    NestedDonutChartWithLegend(
                        segments = segments,
                        modifier = Modifier.fillMaxWidth(),
                        holeRadiusRatio = holeRadiusRatio
                    )
                }

                // Legenda
                if (showLegend) {
                    LegendView(
                        segmentsWithPercentages = segmentsWithPercentages,
                        modifier = Modifier
                            .weight(1 - chartToLegendRatio)
                            .fillMaxWidth()
                            .padding(16.dp)
                    )
                }
            }
        }

        LegendPosition.Right -> {
            Row(
                modifier = modifier
            ) {
                // Gráfico
                Box(
                    modifier = Modifier
                        .weight(chartToLegendRatio)
                        .fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    NestedDonutChart(
                        segments = segments,
                        modifier = Modifier.fillMaxWidth(),
                        holeRadiusRatio = holeRadiusRatio
                    )
                }

                // Legenda
                if (showLegend) {
                    LegendView(
                        segmentsWithPercentages = segmentsWithPercentages,
                        modifier = Modifier
                            .weight(1 - chartToLegendRatio)
                            .padding(16.dp)
                    )
                }
            }
        }

        LegendPosition.None -> {
            NestedDonutChartWithLegend(
                segments = segments,
                modifier = modifier,
                holeRadiusRatio = holeRadiusRatio
            )
        }
    }
}

@Composable
fun LegendView(
    segmentsWithPercentages: List<List<Pair<DonutSegment, Float>>>,
    modifier: Modifier = Modifier
) {
    // Achatar todos os segmentos em uma única lista
    val allLegendItems = segmentsWithPercentages.flatMapIndexed { ringIndex, ringSegments ->
        ringSegments.map { (segment, percentage) ->
            LegendItem(
                color = segment.color,
                label = segment.label,
                value = segment.value,
                percentage = "%.1f%%".format(percentage)
            )
        }
    }

    LazyColumn(
        modifier = modifier
    ) {
        items(allLegendItems) { item ->
            LegendItemRow(
                legendItem = item,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
            )
        }
    }
}

@Composable
fun LegendItemRow(
    legendItem: LegendItem,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Círculo de cor
            Box(
                modifier = Modifier
                    .size(16.dp)
                    .padding(2.dp)
            ) {
                Canvas(modifier = Modifier.size(16.dp)) {
                    drawCircle(
                        color = legendItem.color,
                        radius = size.minDimension / 2
                    )
                }
            }

            // Texto da legenda
            legendItem.label?.let {
                Text(
                    text = it,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Normal
                )
            }
        }

        // Porcentagem
        Text(
            text = legendItem.percentage,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
fun NestedDonutChartWithLegend(
    segments: List<List<DonutSegment>>,
    modifier: Modifier = Modifier,
    holeRadiusRatio: Float = 0.4f
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.size(min(400.dp, 400.dp))) {
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

                drawDonutRingWithLegend(
                    segments = ringSegments,
                    center = center,
                    radius = currentRingRadius,
                    strokeWidth = strokeWidth
                )
            }
        }
    }
}

private fun DrawScope.drawDonutRingWithLegend(
    segments: List<DonutSegment>,
    center: Offset,
    radius: Float,
    strokeWidth: Float
) {
    val totalValue = segments.sumOf { it.value.toDouble() }.toFloat()
    var startAngle = -90f // Começar do topo (12 horas)

    segments.forEach { segment ->
        val sweepAngle = (segment.value / totalValue) * 360f

        drawArc(
            color = segment.color,
            startAngle = startAngle,
            sweepAngle = sweepAngle,
            useCenter = false,
            topLeft = Offset(center.x - radius, center.y - radius),
            size = Size(radius * 2, radius * 2),
            style = Stroke(width = strokeWidth)
        )

        startAngle += sweepAngle
    }
}

enum class LegendPosition {
    Bottom, Right, None
}

//GRAFICO DONUT COM LEGENDA - FIM
