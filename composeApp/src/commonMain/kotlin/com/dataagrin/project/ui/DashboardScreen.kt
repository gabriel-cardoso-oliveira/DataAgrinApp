package com.dataagrin.project.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.dataagrin.project.model.Task
import com.dataagrin.project.model.TaskStatus
import com.dataagrin.project.viewmodel.TaskViewModel
import org.koin.compose.koinInject

@Composable
fun DashboardScreen(viewModel: TaskViewModel = koinInject()) {
    val tasks by viewModel.tasks.collectAsState()
    val pending = tasks.count { it.status == TaskStatus.PENDING }
    val active = tasks.count { it.status == TaskStatus.IN_PROGRESS }

    var filter by remember { mutableStateOf<TaskStatus?>(null) }
    val filteredTasks = if (filter == null) tasks else tasks.filter { it.status == filter }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Tarefas de Hoje", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)

        Row(modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            SummaryCard("Pendente", pending, Color(0xFF059669))
            SummaryCard("Ativo", active, Color(0xFF2563EB))
        }

        // Filtros
        Row(modifier = Modifier.padding(bottom = 16.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            FilterChip(selected = filter == null, onClick = { filter = null }, label = { Text("Todos") })
            FilterChip(selected = filter == TaskStatus.PENDING, onClick = { filter = TaskStatus.PENDING }, label = { Text("Pendente") })
            FilterChip(selected = filter == TaskStatus.IN_PROGRESS, onClick = { filter = TaskStatus.IN_PROGRESS }, label = { Text("Andamento") })
        }

        LazyColumn {
            items(filteredTasks) { task ->
                TaskItem(task) { id, status -> viewModel.updateStatus(id, status) }
            }
        }
    }
}

@Composable
fun TaskItem(task: Task, onStatusChange: (String, TaskStatus) -> Unit) {
    Card(modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp), elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                Column {
                    Text(task.title, fontWeight = FontWeight.Bold)
                    Text("${task.area} • ${task.time}", style = MaterialTheme.typography.labelSmall, color = Color.Gray)
                }
                StatusBadge(task.status)
            }
            Spacer(modifier = Modifier.height(12.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                if (task.status != TaskStatus.IN_PROGRESS)
                    Button(onClick = { onStatusChange(task.id, TaskStatus.IN_PROGRESS) }) { Text("Iniciar") }
                if (task.status != TaskStatus.COMPLETED)
                    Button(onClick = { onStatusChange(task.id, TaskStatus.COMPLETED) }, colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF059669), contentColor = Color.White)) { Text("Concluir") }
            }
        }
    }
}

@Composable
fun SummaryCard(label: String, count: Int, color: Color) {
    Card(colors = CardDefaults.cardColors(containerColor = color, contentColor = Color.White), modifier = Modifier.width(150.dp)) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(label.uppercase(), style = MaterialTheme.typography.labelSmall)
            Text(count.toString(), style = MaterialTheme.typography.headlineLarge)
        }
    }
}

@Composable
fun StatusBadge(status: TaskStatus) {
    val color = when(status) {
        TaskStatus.COMPLETED -> Color.Green
        TaskStatus.IN_PROGRESS -> Color.Blue
        else -> Color.Gray
    }
    Text(status.name, color = color, style = MaterialTheme.typography.labelSmall)
}
