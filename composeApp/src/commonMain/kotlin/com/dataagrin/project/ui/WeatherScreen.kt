package com.dataagrin.project.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.dataagrin.project.viewmodel.WeatherViewModel
import org.koin.compose.koinInject
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.sp
import com.dataagrin.project.utils.getWeatherIcon
import kotlin.math.roundToInt

@Composable
fun WeatherScreen(viewModel: WeatherViewModel = koinInject()) {
    val state by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) { viewModel.loadWeather() }

    Column(modifier = Modifier.padding(16.dp)) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Clima no Campo", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
            IconButton(onClick = { viewModel.loadWeather() }) { Icon(Icons.Default.Refresh, "Atualizar") }
        }

        if (state.isLoading) LinearProgressIndicator(modifier = Modifier.fillMaxWidth())

        state.weather?.let { weather ->
            Card(
                modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondary, contentColor = Color.White),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(modifier = Modifier.padding(24.dp)) {
                    Row(horizontalArrangement = Arrangement.End, modifier = Modifier.fillMaxWidth()) {
                        Surface(
                            color = (if(state.isCached) MaterialTheme.colorScheme.onSecondary else MaterialTheme.colorScheme.onPrimary).copy(alpha = 0.2f),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Text(
                                if(state.isCached) "OFFLINE" else "ONLINE",
                                style = MaterialTheme.typography.labelSmall,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        val weatherIcon = getWeatherIcon(weather.weatherCode)
                        Icon(
                            imageVector = weatherIcon,
                            contentDescription = "Weather condition",
                            modifier = Modifier.size(64.dp))
                        Spacer(Modifier.width(16.dp))
                        Column {
                            Text("${weather.temperature}°", style = MaterialTheme.typography.displayMedium)
                            Text("Umidade: ${weather.humidity}%" )
                        }
                    }
                }
            }

            // Simple Chart (Canvas)
            Text("Previsão (24h)", fontWeight = FontWeight.Bold, modifier = Modifier.padding(bottom = 8.dp))
            WeatherChart(weather.hourlyTemp)
        } ?: run {
            if(!state.isLoading) Text("Sem dados de clima disponíveis.", color = Color.Gray)
        }
    }
}

@Composable
fun WeatherChart(tempString: String) {
    val temps = remember(tempString) { tempString.split(",").mapNotNull { it.toDoubleOrNull() } }
    if (temps.isEmpty()) return

    val chartColor = MaterialTheme.colorScheme.secondary
    val gridColor = Color.LightGray.copy(alpha = 0.5f)
    val textColor = Color.Gray

    val textMeasurer = rememberTextMeasurer()

    Canvas(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .padding(top = 16.dp, bottom = 16.dp, end = 16.dp)
    ) {
        val paddingBottom = 60f
        val paddingLeft = 60f

        val width = size.width - paddingLeft
        val height = size.height - paddingBottom

        val maxData = (temps.maxOrNull() ?: 30.0).coerceAtLeast(28.0) + 2
        val minData = (temps.minOrNull() ?: 10.0).coerceAtMost(10.0) - 2
        val range = maxData - minData

        val widthPerPoint = width / (temps.size - 1).coerceAtLeast(1)

        val steps = 4
        for (i in 0..steps) {
            val priceRatio = i.toFloat() / steps
            val yPos = height - (priceRatio * height)
            val tempLabel = minData + (range * priceRatio)

            drawLine(
                color = gridColor,
                start = Offset(paddingLeft, yPos),
                end = Offset(size.width, yPos),
                pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f),
                strokeWidth = 1.dp.toPx()
            )

            drawText(
                textMeasurer = textMeasurer,
                text = "${tempLabel.roundToInt()}°",
                topLeft = Offset(0f, yPos - 20f),
                style = TextStyle(color = textColor, fontSize = 12.sp)
            )
        }

        val strokePath = Path()
        val fillPath = Path()

        fun getOffset(index: Int, value: Double): Offset {
            val x = paddingLeft + (index * widthPerPoint)
            val y = height - ((value - minData) / range * height).toFloat()
            return Offset(x, y)
        }

        var previousPoint = getOffset(0, temps[0])
        strokePath.moveTo(previousPoint.x, previousPoint.y)
        fillPath.moveTo(previousPoint.x, previousPoint.y)

        for (i in 1 until temps.size) {
            val currentPoint = getOffset(i, temps[i])

            val controlPoint1 = Offset(previousPoint.x + (currentPoint.x - previousPoint.x) / 2, previousPoint.y)
            val controlPoint2 = Offset(previousPoint.x + (currentPoint.x - previousPoint.x) / 2, currentPoint.y)

            strokePath.cubicTo(
                controlPoint1.x, controlPoint1.y,
                controlPoint2.x, controlPoint2.y,
                currentPoint.x, currentPoint.y
            )

            previousPoint = currentPoint
        }

        fillPath.addPath(strokePath)
        fillPath.lineTo(previousPoint.x, height)
        fillPath.lineTo(paddingLeft, height)
        fillPath.close()

        drawPath(
            path = fillPath,
            brush = Brush.verticalGradient(
                colors = listOf(
                    chartColor.copy(alpha = 0.3f),
                    Color.Transparent
                ),
                startY = 0f,
                endY = height
            )
        )

        drawPath(
            path = strokePath,
            color = chartColor,
            style = Stroke(width = 3.dp.toPx())
        )

        val labelInterval = (temps.size / 5).coerceAtLeast(1)

        temps.forEachIndexed { index, _ ->
            if (index % labelInterval == 0) {
                val xPos = paddingLeft + (index * widthPerPoint)
                val hour = (index % 24)
                val timeLabel = "${hour.toString().padStart(2, '0')}:00"

                drawText(
                    textMeasurer = textMeasurer,
                    text = timeLabel,
                    topLeft = Offset(xPos - 15f, height + 10f),
                    style = TextStyle(color = textColor, fontSize = 12.sp)
                )
            }
        }
    }
}
