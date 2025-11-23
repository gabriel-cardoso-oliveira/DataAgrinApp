package com.dataagrin.project.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
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
import com.dataagrin.project.model.ActivityLog
import com.dataagrin.project.viewmodel.ActivityViewModel
import org.koin.compose.koinInject
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

@Composable
fun ActivityLogScreen(viewModel: ActivityViewModel = koinInject()) {
    var isForm by remember { mutableStateOf(true) }
    val activities by viewModel.activities.collectAsState()

    Column(modifier = Modifier.padding(16.dp)) {
        Text("Registro de Atividades", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
        
        TabRow(selectedTabIndex = if (isForm) 0 else 1) {
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
                    Card(modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)) {
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

@OptIn(ExperimentalTime::class)
@Composable
fun ActivityForm(onSubmit: (ActivityLog) -> Unit) {
    var type by remember { mutableStateOf("") }
    var area by remember { mutableStateOf("") }
    var start by remember { mutableStateOf("") }
    var end by remember { mutableStateOf("") }
    var obs by remember { mutableStateOf("") }

    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        OutlinedTextField(value = type, onValueChange = { type = it }, label = { Text("Atividade (ex: Plantio)") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(value = area, onValueChange = { area = it }, label = { Text("Talhão") }, modifier = Modifier.fillMaxWidth())
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            OutlinedTextField(value = start, onValueChange = { start = it }, label = { Text("Início") }, modifier = Modifier.weight(1f))
            OutlinedTextField(value = end, onValueChange = { end = it }, label = { Text("Fim") }, modifier = Modifier.weight(1f))
        }
        OutlinedTextField(value = obs, onValueChange = { obs = it }, label = { Text("Observações") }, modifier = Modifier.fillMaxWidth())

        Button(
            onClick = {
                onSubmit(ActivityLog(Clock.System.now().toEpochMilliseconds().toString(), type, area, start, end, obs, Clock.System.now().toEpochMilliseconds()))
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF059669), contentColor = Color.White)
        ) {
            Icon(Icons.Default.Save, null)
            Spacer(Modifier.width(8.dp))
            Text("Salvar")
        }
    }
}
