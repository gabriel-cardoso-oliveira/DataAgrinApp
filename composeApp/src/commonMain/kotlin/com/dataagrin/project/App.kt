package com.dataagrin.project

import SetStatusBar
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.dataagrin.project.di.appModule
import com.dataagrin.project.ui.ActivityLogScreen
import com.dataagrin.project.ui.DashboardScreen
import com.dataagrin.project.ui.WeatherScreen
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.KoinApplication

@Composable
fun App() {
    KoinApplication(application = {
        modules(appModule)
    }) {
        SetStatusBar(isDarkIcons = true)

        MaterialTheme {
            val navController = rememberNavController()
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentRoute = navBackStackEntry?.destination?.route

            MainScreen(
                currentRoute = currentRoute,
                onNavigate = {
                    navController.navigate(it) {
                        popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                content = {
                    NavHost(navController, startDestination = "dashboard", modifier = it) {
                        composable("dashboard") { DashboardScreen() }
                        composable("activities") { ActivityLogScreen() }
                        composable("weather") { WeatherScreen() }
                    }
                }
            )
        }
    }
}

@Composable
fun MainScreen(
    currentRoute: String?,
    onNavigate: (String) -> Unit,
    content: @Composable (Modifier) -> Unit
) {
    val items = listOf(
        "dashboard" to Icons.Default.Home,
        "activities" to Icons.Default.List,
        "weather" to Icons.Default.WbSunny
    )
    val labels = mapOf(
        "dashboard" to "Tarefas",
        "activities" to "Atividades",
        "weather" to "Clima"
    )

    Scaffold(
        containerColor = Color( 0xFFf9fafb),
        bottomBar = {
            NavigationBar(
                containerColor = Color(0xFFf0f2f5)
            ) {
                items.forEach { (route, icon) ->
                    val label = labels[route] ?: ""
                    NavigationBarItem(
                        selected = currentRoute == route,
                        onClick = { onNavigate(route) },
                        colors = NavigationBarItemDefaults.colors(
                            indicatorColor = Color(0xFFe0e6eb)
                        ),
                        icon = { Icon(icon, contentDescription = label) },
                        label = { Text(label) }
                    )
                }
            }
        }
    ) { innerPadding ->
        content(Modifier.padding(innerPadding))
    }
}

@Preview
@Composable
fun AppPreview() {
    MaterialTheme {
        var currentRoute by remember { mutableStateOf("dashboard") }
        MainScreen(
            currentRoute = currentRoute,
            onNavigate = { currentRoute = it },
            content = { modifier ->
                Box(modifier = modifier)
            }
        )
    }
}
