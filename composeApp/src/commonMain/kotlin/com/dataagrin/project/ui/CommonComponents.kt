package com.dataagrin.project.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.background
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.dataagrin.project.model.TaskStatus

@Composable
fun SummaryCard(label: String, count: Int, color: Color, modifier: Modifier = Modifier) {
    Card(
        colors = CardDefaults.cardColors(containerColor = color, contentColor = Color.White),
        modifier = modifier
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(label.uppercase(), style = MaterialTheme.typography.labelSmall)
            Text(count.toString(), style = MaterialTheme.typography.headlineLarge)
        }
    }
}

@Composable
fun StatusBadge(status: TaskStatus) {
    val color = when(status) {
        TaskStatus.COMPLETED -> Color(0XFF16a34a)
        TaskStatus.IN_PROGRESS -> Color(0xFF2563eb)
        else -> Color.Gray
    }
    val text = when(status) {
        TaskStatus.COMPLETED -> "Concluido"
        TaskStatus.IN_PROGRESS -> "Em andamento"
        TaskStatus.PENDING -> "Pendente"
    }
    Text(
        text = text,
        color = color,
        style = MaterialTheme.typography.labelSmall,
        modifier = Modifier
            .background(color.copy(alpha = 0.1f), RoundedCornerShape(10.dp))
            .padding(horizontal = 6.dp, vertical = 2.dp)
    )
}

@Composable
fun TaskFilterChip(text: String, selected: Boolean, onClick: () -> Unit) {
    FilterChip(
        selected = selected,
        onClick = onClick,
        label = { Text(text) },
        shape = RoundedCornerShape(10.dp),
        colors = FilterChipDefaults.filterChipColors(
            selectedContainerColor = Color.Black,
            selectedLabelColor = Color.White
        )
    )
}

@Composable
fun ActionButton(
    text: String,
    onClick: () -> Unit,
    containerColor: Color,
    contentColor: Color,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        modifier = modifier,
        shape = RoundedCornerShape(10.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = containerColor,
            contentColor = contentColor
        )
    ) {
        Text(text)
    }
}
