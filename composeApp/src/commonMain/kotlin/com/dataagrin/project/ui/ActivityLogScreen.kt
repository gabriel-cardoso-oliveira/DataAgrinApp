package com.dataagrin.project.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SecondaryTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.dataagrin.project.model.ActivityLog
import com.dataagrin.project.viewmodel.ActivityViewModel
import org.koin.compose.koinInject
import kotlin.time.Clock
import kotlin.time.ExperimentalTime
import androidx.compose.material3.*

@Composable
fun ActivityLogScreen(viewModel: ActivityViewModel = koinInject()) {
    var isForm by remember { mutableStateOf(true) }
    val activities by viewModel.activities.collectAsState()

    Column(modifier = Modifier.padding(16.dp)) {
        Text("Registro de Atividades", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)

        Spacer(modifier = Modifier.height(16.dp))

        SecondaryTabRow(
            selectedTabIndex = if (isForm) 0 else 1,
            containerColor = MaterialTheme.colorScheme.onPrimary,
            contentColor = MaterialTheme.colorScheme.primary,
            indicator = {
                HorizontalDivider(
                    modifier = Modifier.tabIndicatorOffset(if (isForm) 0 else 1),
                    color = MaterialTheme.colorScheme.primary,
                    thickness = 2.dp
                )
            }
        ) {
            Tab(selected = isForm, onClick = { isForm = true }, text = { Text("Novo") })
            Tab(selected = !isForm, onClick = { isForm = false }, text = { Text("Histórico") })
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (isForm) {
            ActivityForm { type, area, start, end, obs ->
                viewModel.addTaskFromActivity(type, area, start, end, obs)
                isForm = false
            }
        } else {
            LazyColumn {
                items(activities) { act ->
                    Card(
                        modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.onPrimaryContainer)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(act.type, fontWeight = FontWeight.Bold)
                            Text("Área: ${act.area}")
                            Text("${act.startTime} - ${act.endTime}", style = MaterialTheme.typography.labelSmall)
                            if (act.observations.isNotEmpty()) Text("\"${act.observations}\"", style = MaterialTheme.typography.bodyMedium, fontStyle = androidx.compose.ui.text.font.FontStyle.Italic)
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun KmpTimePickerDialog(
    onDismiss: () -> Unit,
    onConfirm: (String) -> Unit,
    initialHour: Int = 0,
    initialMinute: Int = 0
) {
    val timePickerState = rememberTimePickerState(
        initialHour = initialHour,
        initialMinute = initialMinute,
        is24Hour = true
    )

    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = {
                val hour = timePickerState.hour.toString().padStart(2, '0')
                val minute = timePickerState.minute.toString().padStart(2, '0')
                onConfirm("$hour:$minute")
            }) {
                Text("OK")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        },
        text = {
            TimePicker(state = timePickerState)
        }
    )
}

@OptIn(ExperimentalTime::class)
@Composable
fun ActivityForm(onSubmit: (String, String, String, String, String) -> Unit) {
    var type by remember { mutableStateOf("") }
    var area by remember { mutableStateOf("") }
    var start by remember { mutableStateOf("") }
    var end by remember { mutableStateOf("") }
    var obs by remember { mutableStateOf("") }

    var showTimePickerFor by remember { mutableStateOf<String?>(null) }

    if (showTimePickerFor != null) {
        val timeToEdit = if (showTimePickerFor == "start") start else end
        val (initHour, initMinute) = if (timeToEdit.contains(":")) {
            val parts = timeToEdit.split(":")
            parts[0].toInt() to parts[1].toInt()
        } else {
            12 to 0
        }

        KmpTimePickerDialog(
            onDismiss = { showTimePickerFor = null },
            onConfirm = { time ->
                if (showTimePickerFor == "start") {
                    start = time
                } else {
                    end = time
                }
                showTimePickerFor = null
            },
            initialHour = initHour,
            initialMinute = initMinute
        )
    }

    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        StandardTextField(value = type, onValueChange = { type = it }, label = "Atividade")
        StandardTextField(value = area, onValueChange = { area = it }, label = "Talhão")

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            TimePickerField(value = start, label = "Início", onClick = { showTimePickerFor = "start" }, modifier = Modifier.weight(1f))
            TimePickerField(value = end, label = "Fim", onClick = { showTimePickerFor = "end" }, modifier = Modifier.weight(1f))
        }

        StandardTextField(value = obs, onValueChange = { obs = it }, label = "Observações")

        Spacer(modifier = Modifier.height(16.dp))

        ActionButton(
            text = "Salvar",
            onClick = {
                onSubmit(type, area, start, end, obs)
            },
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary,
            modifier = Modifier.fillMaxWidth()
        )

    }
}