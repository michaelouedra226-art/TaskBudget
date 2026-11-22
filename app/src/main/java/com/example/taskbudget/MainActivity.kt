package com.example.taskbudget

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.taskbudget.ui.screens.*
import com.example.taskbudget.ui.theme.TaskBudgetTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TaskBudgetTheme {
                val nav = rememberNavController()
                Surface(Modifier) {
                    NavHost(nav, startDestination = "home") {
                        composable("home") { DashboardScreen({ nav.navigate("tasks") }, { nav.navigate("budget") }) }
                        composable("tasks") { TasksScreen({ nav.pop() }) }
                        composable("budget") { BudgetScreen({ nav.pop() }) }
                    }
                }
            }
        }
    }
}
