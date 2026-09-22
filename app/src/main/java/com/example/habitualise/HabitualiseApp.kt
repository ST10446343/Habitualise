package com.example.habitualise.ui

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.habitualise.HabitualiseViewModel
import com.example.habitualise.api.QuoteRepository
import com.example.habitualise.auth.AuthRepository
import com.example.habitualise.data.HabitRepository
import com.example.habitualise.screens.AddHabitScreen
import com.example.habitualise.screens.HomeScreen
import com.example.habitualise.screens.LoginScreen
import com.example.habitualise.screens.ProgressScreen
import com.example.habitualise.screens.RegisterScreen
import com.example.habitualise.screens.SettingsScreen
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

enum class Screen {
    LOGIN,
    REGISTER,
    HOME,
    ADD,
    PROGRESS,
    SETTINGS
}

@Composable
fun HabitualiseApp() {
    val context = LocalContext.current
    val viewModel: HabitualiseViewModel = viewModel(
        factory = HabitualiseFactory(context)
    )

    var screen by remember {
        mutableStateOf(
            if (viewModel.user == null) Screen.LOGIN else Screen.HOME
        )
    }

    if (viewModel.user == null && screen != Screen.REGISTER) {
        screen = Screen.LOGIN
    }

    if (viewModel.user != null && (screen == Screen.LOGIN || screen == Screen.REGISTER)) {
        screen = Screen.HOME
    }

    if (viewModel.error.isNotEmpty()) {
        AlertDialog(
            onDismissRequest = { viewModel.clearError() },
            title = { Text("Habitualise") },
            text = { Text(viewModel.error) },
            confirmButton = {
                Button(onClick = { viewModel.clearError() }) {
                    Text("OK")
                }
            }
        )
    }

    when (screen) {
        Screen.LOGIN -> {
            LoginScreen(
                onLogin = { email, password ->
                    viewModel.login(email, password)
                },
                onRegister = {
                    screen = Screen.REGISTER
                }
            )
        }

        Screen.REGISTER -> {
            RegisterScreen(
                onRegister = { name, email, password, confirm ->
                    viewModel.register(name, email, password, confirm)
                },
                onBack = {
                    screen = Screen.LOGIN
                }
            )
        }

        Screen.HOME -> {
            HomeScreen(
                name = viewModel.user?.displayName ?: "User",
                habits = viewModel.habits,
                quote = viewModel.quote,
                onToggle = viewModel::toggleHabit,
                onDelete = viewModel::deleteHabit,
                onQuote = viewModel::loadQuote,
                onAdd = { screen = Screen.ADD },
                onProgress = { screen = Screen.PROGRESS },
                onSettings = { screen = Screen.SETTINGS }
            )
        }

        Screen.ADD -> {
            AddHabitScreen(
                onSave = { name, description ->
                    viewModel.addHabit(name, description) {
                        screen = Screen.HOME
                    }
                },
                onBack = { screen = Screen.HOME }
            )
        }

        Screen.PROGRESS -> {
            ProgressScreen(
                habits = viewModel.habits,
                onBack = { screen = Screen.HOME }
            )
        }

        Screen.SETTINGS -> {
            SettingsScreen(
                name = viewModel.user?.displayName ?: "",
                email = viewModel.user?.email ?: "",
                onSaveName = { newName ->
                    viewModel.updateName(newName)
                },
                onLogout = { viewModel.logout() },
                onBack = { screen = Screen.HOME }
            )
        }
    }
}

class HabitualiseFactory(
    context: android.content.Context
) : androidx.lifecycle.ViewModelProvider.Factory {

    private val auth = AuthRepository(
        FirebaseAuth.getInstance()
    )

    private val habits = HabitRepository(
        FirebaseFirestore.getInstance()
    )

    private val quotes = QuoteRepository()

    override fun <T : androidx.lifecycle.ViewModel> create(
        modelClass: Class<T>
    ): T {
        return HabitualiseViewModel(
            auth,
            habits,
            quotes
        ) as T
    }
}