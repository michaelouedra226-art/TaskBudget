package com.example.taskbudget

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.taskbudget.data.AppDatabase
import com.example.taskbudget.data.Task
import com.example.taskbudget.data.Transaction
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class TaskViewModel(app: Application) : AndroidViewModel(app) {
    private val db = AppDatabase.get(app)
    val tasks: StateFlow<List<Task>> = db.taskDao().getAll()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun add(title: String, due: String) = viewModelScope.launch {
        if (title.isNotBlank()) db.taskDao().insert(Task(title = title, due = due))
    }
    fun toggle(task: Task) = viewModelScope.launch {
        db.taskDao().update(task.copy(done = !task.done))
    }
    fun delete(task: Task) = viewModelScope.launch { db.taskDao().delete(task) }
}

class BudgetViewModel(app: Application) : AndroidViewModel(app) {
    private val db = AppDatabase.get(app)
    val transactions = db.transactionDao().getAll()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    val balance = db.transactionDao().balance()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0.0)

    fun add(amount: Double, type: String, desc: String) = viewModelScope.launch {
        db.transactionDao().insert(Transaction(amount = amount, type = type, description = desc, date = "aujourd'hui"))
    }
    fun delete(tx: Transaction) = viewModelScope.launch { db.transactionDao().delete(tx) }
}
