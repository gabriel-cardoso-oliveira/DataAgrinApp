package com.dataagrin.project.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
            SummaryCard("Pendente", pending, MaterialTheme.colorScheme.primary, modifier = Modifier.weight(1f))
            SummaryCard("Ativo", active, MaterialTheme.colorScheme.secondary, modifier = Modifier.weight(1f))
        }

        Row(modifier = Modifier.padding(bottom = 16.dp).horizontalScroll(rememberScrollState()), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            TaskFilterChip(text = "Todos", selected = filter == null) { filter = null }
            TaskFilterChip(text = "Pendente", selected = filter == TaskStatus.PENDING) { filter = TaskStatus.PENDING }
            TaskFilterChip(text = "Andamento", selected = filter == TaskStatus.IN_PROGRESS) { filter = TaskStatus.IN_PROGRESS }
            TaskFilterChip(text = "Concluído", selected = filter == TaskStatus.COMPLETED) { filter = TaskStatus.COMPLETED }
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
    Card(
        modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.onPrimaryContainer),
    ) {
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
                if (task.status != TaskStatus.IN_PROGRESS) {
                    ActionButton(
                        text = "Iniciar",
                        onClick = { onStatusChange(task.id, TaskStatus.IN_PROGRESS) },
                        containerColor = MaterialTheme.colorScheme.secondaryContainer,
                        contentColor = MaterialTheme.colorScheme.secondary,
                        modifier = Modifier.weight(1f)
                    )
                }
                if (task.status != TaskStatus.COMPLETED) {
                    ActionButton(
                        text = "Concluir",
                        onClick = { onStatusChange(task.id, TaskStatus.COMPLETED) },
                        containerColor = MaterialTheme.colorScheme.onPrimary,
                        contentColor = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}
