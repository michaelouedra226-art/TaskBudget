package com.example.taskbudget

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                val nav = rememberNavController()
                NavHost(nav, startDestination = "home") {
                    composable("home") { HomeScreen(nav) }
                    composable("tasks") { TasksScreen() }
                    composable("budget") { BudgetScreen() }
                }
            }
        }
    }
}

@Composable
fun HomeScreen(nav: NavHostController) {
    val vm: BudgetViewModel = viewModel()
    val balance by vm.balance.collectAsState()
    Scaffold(topBar = { TopAppBar(title = { Text("TaskBudget") }) }) { pad ->
        Column(Modifier.padding(pad).padding(24.dp)) {
            Card(Modifier.fillMaxWidth()) {
                Column(Modifier.padding(16.dp)) {
                    Text("Solde mensuel", style = MaterialTheme.typography.titleMedium)
                    Text(String.format("%.2f €", balance), style = MaterialTheme.typography.headlineMedium)
                }
            }
            Spacer(Modifier.height(16.dp))
            Button(onClick = { nav.navigate("tasks") }, Modifier.fillMaxWidth()) { Text("Tâches") }
            Spacer(Modifier.height(8.dp))
            Button(onClick = { nav.navigate("budget") }, Modifier.fillMaxWidth()) { Text("Budget") }
        }
    }
}

@Composable
fun TasksScreen() {
    val vm: TaskViewModel = viewModel()
    val tasks by vm.tasks.collectAsState()
    var title by remember { mutableStateOf("") }
    Scaffold(topBar = { TopAppBar(title = { Text("Tâches") }) }) { pad ->
        Column(Modifier.padding(pad)) {
            Row(Modifier.padding(16.dp)) {
                OutlinedTextField(value = title, onValueChange = { title = it }, label = { Text("Nouvelle tâche") }, modifier = Modifier.weight(1f))
                Spacer(Modifier.width(8.dp))
                Button(onClick = { vm.add(title, ""); title = "" }) { Text("Ajouter") }
            }
            LazyColumn {
                items(tasks, key = { it.id }) { t ->
                    ListItem(
                        headlineContent = { Text(t.title) },
                        supportingContent = { if (t.due.isNotBlank()) Text(t.due) },
                        trailingContent = {
                            Checkbox(checked = t.done, onCheckedChange = { vm.toggle(t) })
                        }
                    )
                    HorizontalDivider()
                }
            }
        }
    }
}

@Composable
fun BudgetScreen() {
    val vm: BudgetViewModel = viewModel()
    val txs by vm.transactions.collectAsState()
    val balance by vm.balance.collectAsState()
    var amount by remember { mutableStateOf("") }
    var desc by remember { mutableStateOf("") }
    var isIncome by remember { mutableStateOf(true) }
    Scaffold(topBar = { TopAppBar(title = { Text("Budget") }) }) { pad ->
        Column(Modifier.padding(pad)) {
            Text(String.format("Solde : %.2f €", balance), style = MaterialTheme.typography.headlineSmall, modifier = Modifier.padding(16.dp))
            Row(Modifier.padding(16.dp)) {
                OutlinedTextField(value = amount, onValueChange = { amount = it }, label = { Text("Montant") }, modifier = Modifier.weight(1f))
                Spacer(Modifier.width(8.dp))
                OutlinedTextField(value = desc, onValueChange = { desc = it }, label = { Text("Description") }, modifier = Modifier.weight(1f))
            }
            Row(Modifier.padding(horizontal = 16.dp)) {
                FilterChip(selected = isIncome, onClick = { isIncome = true }, label = { Text("Revenu") })
                Spacer(Modifier.width(8.dp))
                FilterChip(selected = !isIncome, onClick = { isIncome = false }, label = { Text("Dépense") })
                Spacer(Modifier.weight(1f))
                Button(onClick = {
                    amount.toDoubleOrNull()?.let { vm.add(it, if (isIncome) "income" else "expense", desc); amount = ""; desc = "" }
                }) { Text("Ajouter") }
            }
            LazyColumn {
                items(txs, key = { it.id }) { t ->
                    ListItem(
                        headlineContent = { Text(t.description.ifBlank { t.type }) },
                        supportingContent = { Text(t.date) },
                        trailingContent = { Text(String.format("%.2f €", t.amount), color = if (t.type == "income") MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error) }
                    )
                    HorizontalDivider()
                }
            }
        }
    }
}
