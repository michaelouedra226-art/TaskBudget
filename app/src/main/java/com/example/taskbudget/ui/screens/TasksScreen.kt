package com.example.taskbudget.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AddTask
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.taskbudget.TaskViewModel
import com.example.taskbudget.ui.components.*

@OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)
@Composable
fun TasksScreen(onBack: () -> Unit) {
    val vm: TaskViewModel = viewModel()
    val tasks by vm.tasks.collectAsState()
    var title by remember { mutableStateOf("") }
    var due by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mes tâches", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold) },
                navigationIcon = { TextButton(onClick = onBack) { Icon(Icons.AutoMirrored.Filled.ArrowBack, "Retour") } },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.background)
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { pad ->
        Column(Modifier.padding(pad)) {
            Row(Modifier.padding(horizontal = 20.dp, vertical = 12.dp)) {
                OutlinedTextField(
                    value = title, onValueChange = { title = it },
                    label = { Text("Nouvelle tâche") },
                    singleLine = true,
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier.weight(1f))
                Spacer(Modifier.width(8.dp))
                OutlinedTextField(
                    value = due, onValueChange = { due = it },
                    label = { Text("Échéance") },
                    singleLine = true,
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier.width(130.dp))
                Spacer(Modifier.width(8.dp))
                FilledIconButton(onClick = {
                    vm.add(title.trim(), due.trim()); title = ""; due = ""
                }) { Icon(Icons.Default.AddTask, "Ajouter") }
            }

            if (tasks.isEmpty()) {
                EmptyState(
                    icon = { Icon(Icons.Default.AddTask, null, modifier = Modifier.size(32.dp), tint = MaterialTheme.colorScheme.primary) },
                    title = "Rien à faire (pour l'instant)",
                    subtitle = "Ajoutez votre première tâche ci-dessus.")
            } else {
                LazyColumn(contentPadding = PaddingValues(horizontal = 20.dp, vertical = 8.dp)) {
                    items(tasks, key = { it.id }) { t ->
                        Column(Modifier.padding(bottom = 10.dp)) {
                            TaskRow(task = t, onToggle = { vm.toggle(t) }, onDelete = { vm.delete(t) })
                        }
                    }
                }
            }
        }
    }
}
