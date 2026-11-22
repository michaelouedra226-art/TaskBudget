package com.example.taskbudget.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Checklist
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.taskbudget.BudgetViewModel
import com.example.taskbudget.TaskViewModel
import com.example.taskbudget.ui.components.BalanceCard
import com.example.taskbudget.ui.components.SectionHeader
import com.example.taskbudget.ui.theme.*

@Composable
fun DashboardScreen(onTasks: () -> Unit, onBudget: () -> Unit) {
    val bvm: BudgetViewModel = viewModel()
    val tvm: TaskViewModel = viewModel()
    val balance by bvm.balance.collectAsState()
    val tasks by tvm.tasks.collectAsState()
    val txCount by bvm.transactions.collectAsState()
    val doneCount = tasks.count { it.done }

    Column(Modifier
        .fillMaxSize()
        .padding(horizontal = 20.dp)) {

        Row(Modifier.padding(top = 16.dp, bottom = 4.dp), verticalAlignment = Alignment.CenterVertically) {
            Text("Bonjour 👋", style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
        Text("TaskBudget", style = MaterialTheme.typography.displaySmall,
            modifier = Modifier.padding(bottom = 16.dp))

        BalanceCard(amount = balance)
        Spacer(Modifier.height(24.dp))

        SectionHeader("Aperçu")
        Row(Modifier.fillMaxWidth()) {
            StatCard("Tâches faites", "$doneCount/${tasks.size}", Icons.Default.Checklist)
            Spacer(Modifier.width(12.dp))
            StatCard("Mouvements", "${txCount.size}", Icons.Default.ShoppingCart)
        }
        Spacer(Modifier.height(24.dp))

        SectionHeader("Raccourcis")
        Row(Modifier.fillMaxWidth()) {
            Button(onClick = onTasks, shape = RoundedCornerShape(18.dp),
                modifier = Modifier.weight(1f).height(56.dp)) {
                Icon(Icons.Default.Checklist, null)
                Spacer(Modifier.width(8.dp)); Text("Tâches")
            }
            Spacer(Modifier.width(12.dp))
            Button(onClick = onBudget, shape = RoundedCornerShape(18.dp),
                modifier = Modifier.weight(1f).height(56.dp)) {
                Icon(Icons.Default.ShoppingCart, null)
                Spacer(Modifier.width(8.dp)); Text("Budget")
            }
        }
        Spacer(Modifier.height(16.dp))
    }
}

@Composable
private fun StatCard(label: String, value: String, icon: androidx.compose.ui.graphics.vector.ImageVector) {
    Card(Modifier.weight(1f), shape = RoundedCornerShape(18.dp)) {
        Column(Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(icon, contentDescription = null, tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(18.dp))
                Spacer(Modifier.width(8.dp))
                Text(label, style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            Spacer(Modifier.height(8.dp))
            Text(value, style = MaterialTheme.typography.headlineSmall)
        }
    }
}
