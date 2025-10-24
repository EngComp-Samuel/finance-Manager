
package br.com.engcomp.financemanager.screen.graficos

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable



import androidx.compose.ui.graphics.Color

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment

import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size

import androidx.compose.ui.graphics.Outline

import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawOutline
import androidx.compose.ui.graphics.drawscope.Stroke

import kotlin.math.max
import kotlin.math.min
import androidx.compose.ui.graphics.drawscope.DrawScope

data class DadosParaPizza(
    val label: String,
    val valor: Float
)



@SuppressLint("UnusedBoxWithConstraintsScope")
@Composable
fun PieChart(
    categories: DadosParaPizza,
    modifier: Modifier = Modifier
) {
    val total = categories.valor
    var selectedCategory by remember { mutableStateOf<String?>(null) }
    val colors = listOf(
        Color(0xFFFF5252), Color(0xFFFF9800), Color(0xFFFFEB3B),
        Color(0xFF4CAF50), Color(0xFF2196F3), Color(0xFF9C27B0)
    )

    val listaDadoPizza = listOf(
        DadosParaPizza("Alimentação", 4500f),
        DadosParaPizza("Lazer", 4500f)
    )

    BoxWithConstraints(modifier = modifier.aspectRatio(1f)) {
        val canvasSize = min(constraints.maxWidth, constraints.maxHeight)
        val radius = (canvasSize / 2) * 0.8f

        Canvas(modifier = Modifier.size(canvasSize.dp)) {
            var startAngle = -90f
            val center = Offset(size.width / 2, size.height / 2)

            listaDadoPizza.forEachIndexed {index, (categoria, value) ->
                val sweepAngle = (value / total) * 360f
                val isSelected = selectedCategory == categoria
                val scale = if (isSelected) 1.1f else 1f
                val scaledRadius = radius * scale

                drawArc(
                    color = colors[index % colors.size],
                    startAngle = startAngle,
                    sweepAngle = sweepAngle,
                    useCenter = true,
                    topLeft = Offset(
                        center.x - scaledRadius,
                        center.y - scaledRadius
                    ),
                    size = Size(scaledRadius * 2, scaledRadius * 2),
                    /*onDraw = {
                        if (isSelected) {
                            drawOutline(
                                outline = Outline.Rectangle(
                                    rect = Rect(
                                        offset = Offset(
                                            center.x - scaledRadius,
                                            center.y - scaledRadius
                                        ),
                                        size = Size(scaledRadius * 2, scaledRadius * 2)
                                    )
                                ),
                                color = Color.Black,
                                style = Stroke(width = 2.dp.toPx())
                            )
                        }
                    }*/
                )

                // Adiciona interatividade
                drawContext.canvas.nativeCanvas.apply {
                    val path = Path().apply {
                        addArc(
                            Rect(
                                center.x - radius,
                                center.y - radius,
                                center.x + radius,
                                center.y + radius
                            ),
                            startAngle,
                            sweepAngle
                        )
                        lineTo(center.x, center.y)
                        close()
                    }


               /* setOnTouchListener { _, event ->
                        if (event.action == MotionEvent.ACTION_DOWN) {
                            if (path.contains(event.x, event.y)) {
                                selectedCategory = category
                            } else {
                                selectedCategory = null
                            }
                            true
                        } else {
                            false
                        }
                    }*/

                }

                startAngle += sweepAngle
            }
        }

        // Legenda
        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(16.dp)
        ) {
            listaDadoPizza.forEachIndexed { index, (categoria, value) ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(vertical = 4.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(16.dp)
                            .background(colors[index % colors.size])
                            .border(
                                width = if (selectedCategory == categoria) 2.dp else 0.dp,
                                color = Color.Black
                            )
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "$categoria (${"%.1f".format((value / total) * 100)}%)",
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        }
    }
}
