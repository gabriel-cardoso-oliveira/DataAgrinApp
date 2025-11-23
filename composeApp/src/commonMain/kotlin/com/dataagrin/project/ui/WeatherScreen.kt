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
import androidx.compose.material.icons.filled.Cloud
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

@Composable
fun WeatherScreen(viewModel: WeatherViewModel = koinInject()) {
    val state by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) { viewModel.loadWeather() }

    Column(modifier = Modifier.padding(16.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
            Text("Clima no Campo", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
            IconButton(onClick = { viewModel.loadWeather() }) { Icon(Icons.Default.Refresh, "Atualizar") }
        }

        if (state.isLoading) LinearProgressIndicator(modifier = Modifier.fillMaxWidth())

        state.weather?.let { weather ->
            Card(
                modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF2563EB), contentColor = Color.White),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(modifier = Modifier.padding(24.dp)) {
                    Row(horizontalArrangement = Arrangement.End, modifier = Modifier.fillMaxWidth()) {
                        // Cached Indicator
                        Surface(color = (if(state.isCached) Color.Gray else Color.Green).copy(alpha = 0.2f), shape = RoundedCornerShape(4.dp)) {
                            Text(if(state.isCached) "OFFLINE CACHE" else "LIVE", style = MaterialTheme.typography.labelSmall, modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp))
                        }
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Cloud, null, modifier = Modifier.size(64.dp))
                        Spacer(Modifier.width(16.dp))
                        Column {
                            Text("${weather.temperature}°", style = MaterialTheme.typography.displayMedium)
                            Text("Umidade: ${weather.humidity}%")
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

    Canvas(modifier = Modifier.fillMaxWidth().height(150.dp).padding(top = 16.dp)) {
        val max = temps.maxOrNull() ?: 100.0
        val min = temps.minOrNull() ?: 0.0
        val range = max - min
        val widthPerPoint = size.width / (temps.size - 1)

        val path = Path()
        temps.forEachIndexed { i, temp ->
            val x = i * widthPerPoint
            val y = size.height - ((temp - min) / range * size.height).toFloat()
            if (i == 0) path.moveTo(x, y) else path.lineTo(x, y)
        }

        drawPath(
            path = path,
            color = Color(0xFF2563EB),
            style = Stroke(width = 3.dp.toPx())
        )

        // Fill gradient
        path.lineTo(size.width, size.height)
        path.lineTo(0f, size.height)
        path.close()
        drawPath(
            path = path,
            brush = Brush.verticalGradient(colors = listOf(Color(0xFF2563EB).copy(alpha = 0.3f), Color.Transparent))
        )
    }
}
