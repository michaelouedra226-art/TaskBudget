package com.example.taskbudget.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ShoppingCart
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
import com.example.taskbudget.BudgetViewModel
import com.example.taskbudget.ui.components.*
import com.example.taskbudget.ui.theme.RoundedCornerShape

@OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)
@Composable
fun BudgetScreen(onBack: () -> Unit) {
    val vm: BudgetViewModel = viewModel()
    val txs by vm.transactions.collectAsState()
    val balance by vm.balance.collectAsState()
    var amount by remember { mutableStateOf("") }
    var desc by remember { mutableStateOf("") }
    var isIncome by remember { mutableStateOf(true) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mon budget", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold) },
                navigationIcon = { TextButton(onClick = onBack) { Icon(Icons.AutoMirrored.Filled.ArrowBack, "Retour") } },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.background)
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { pad ->
        Column(Modifier.padding(pad)) {
            Row(Modifier.padding(horizontal = 20.dp, top = 8.dp)) {
                Text("Solde", style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant)
                Spacer(Modifier.weight(1f))
                Text(String.format("%+.2f €", balance), style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold)
            }

            Row(Modifier.padding(horizontal = 20.dp, vertical = 12.dp)) {
                OutlinedTextField(value = amount, onValueChange = { amount = it },
                    label = { Text("Montant") }, singleLine = true,
                    shape = RoundedCornerShape(14.dp), modifier = Modifier.width(110.dp))
                Spacer(Modifier.width(8.dp))
                OutlinedTextField(value = desc, onValueChange = { desc = it },
                    label = { Text("Description") }, singleLine = true,
                    shape = RoundedCornerShape(14.dp), modifier = Modifier.weight(1f))
            }
            Row(Modifier.padding(horizontal = 20.dp, vertical = 8.dp)) {
                FilterChip(selected = isIncome, onClick = { isIncome = true },
                    label = { Text("Revenu") }, modifier = Modifier.weight(1f))
                Spacer(Modifier.width(8.dp))
                FilterChip(selected = !isIncome, onClick = { isIncome = false },
                    label = { Text("Dépense") }, modifier = Modifier.weight(1f))
                Spacer(Modifier.width(8.dp))
                FilledIconButton(onClick = {
                    amount.toDoubleOrNull()?.let { vm.add(it, if (isIncome) "income" else "expense", desc) }
                }) { Icon(Icons.Default.Add, "Ajouter") }
            }

            if (txs.isEmpty()) {
                EmptyState(
                    icon = { Icon(Icons.Default.ShoppingCart, null, modifier = Modifier.size(32.dp), tint = MaterialTheme.colorScheme.primary) },
                    title = "Aucun mouvement",
                    subtitle = "Enregistrez un revenu ou une dépense.")
            } else {
                LazyColumn(contentPadding = PaddingValues(horizontal = 20.dp, vertical = 8.dp)) {
                    items(txs, key = { it.id }) { t ->
                        Column(Modifier.padding(bottom = 10.dp)) { TxRow(tx = t) }
                    }
                }
            }
        }
    }
}
