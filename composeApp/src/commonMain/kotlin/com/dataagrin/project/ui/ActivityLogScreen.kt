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
            ActivityForm { log ->
                viewModel.addActivity(log)
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
fun ActivityForm(onSubmit: (ActivityLog) -> Unit) {
    var type by remember { mutableStateOf("") }
    var area by remember { mutableStateOf("") }
    var start by remember { mutableStateOf("") }
    var end by remember { mutableStateOf("") }
    var obs by remember { mutableStateOf("") }

    var showTimePicker by remember { mutableStateOf(false) }
    var isSelectingStart by remember { mutableStateOf(true) }

    fun openTimePicker(isStart: Boolean) {
        isSelectingStart = isStart
        showTimePicker = true
    }

    if (showTimePicker) {
        val currentTimeStr = if (isSelectingStart) start else end
        val (initHour, initMinute) = if (currentTimeStr.contains(":")) {
            val parts = currentTimeStr.split(":")
            parts[0].toInt() to parts[1].toInt()
        } else {
            12 to 0
        }

        KmpTimePickerDialog(
            onDismiss = { showTimePicker = false },
            onConfirm = { time ->
                if (isSelectingStart) start = time else end = time
                showTimePicker = false
            },
            initialHour = initHour,
            initialMinute = initMinute
        )
    }

    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        StandardTextField(value = type, onValueChange = { type = it }, label = "Atividade")
        StandardTextField(value = area, onValueChange = { area = it }, label = "Talhão")

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            TimePickerField(value = start, label = "Início", onClick = { openTimePicker(true) }, modifier = Modifier.weight(1f))
            TimePickerField(value = end, label = "Fim", onClick = { openTimePicker(false) }, modifier = Modifier.weight(1f))
        }

        StandardTextField(value = obs, onValueChange = { obs = it }, label = "Observações")

        Spacer(modifier = Modifier.height(16.dp))

        ActionButton(
            text = "Salvar",
            onClick = {
                onSubmit(ActivityLog(
                    Clock.System.now().toEpochMilliseconds().toString(),
                    type, area, start, end, obs,
                    Clock.System.now().toEpochMilliseconds()
                ))
            },
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary,
            modifier = Modifier.fillMaxWidth()
        )

    }
}
