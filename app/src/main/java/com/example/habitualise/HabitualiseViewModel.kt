package com.example.habitualise

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.habitualise.api.Quote
import com.example.habitualise.api.QuoteRepository
import com.example.habitualise.auth.AuthRepository
import com.example.habitualise.data.Habit
import com.example.habitualise.data.HabitLogic
import com.example.habitualise.data.HabitRepository
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import kotlinx.coroutines.launch
import java.util.UUID

class HabitualiseViewModel(
    private val auth: AuthRepository,
    private val habitsRepository: HabitRepository,
    private val quotes: QuoteRepository
) : ViewModel() {

    var user: FirebaseUser? by mutableStateOf(auth.currentUser)
        private set

    var habits: List<Habit> by mutableStateOf(emptyList())
        private set

    var quote: Quote by mutableStateOf(
        Quote(
            "Small steps every day become big results.",
            "Habitualise"
        )
    )
        private set

    var error: String by mutableStateOf("")
        private set

    fun register(
        name: String,
        email: String,
        password: String,
        confirmPassword: String
    ) {
        if (name.isBlank() || email.isBlank() || password.isBlank() || confirmPassword.isBlank()) {
            error = "Please complete all fields."
            return
        }

        if (!email.contains("@")) {
            error = "Please enter a valid email."
            return
        }

        if (password.length < 6) {
            error = "Password must be at least 6 characters."
            return
        }

        if (password != confirmPassword) {
            error = "Passwords do not match."
            return
        }

        auth.register(
            name.trim(),
            email.trim(),
            password,
            success = {
                user = auth.currentUser
                loadHabits()
                loadQuote()
            },
            failure = {
                error = it
            }
        )
    }

    fun login(
        email: String,
        password: String
    ) {
        if (email.isBlank() || password.isBlank()) {
            error = "Please enter your email and password."
            return
        }

        auth.login(
            email.trim(),
            password,
            success = {
                user = auth.currentUser
                loadHabits()
                loadQuote()
            },
            failure = {
                error = it
            }
        )
    }

    fun logout() {
        auth.logout()
        user = null
        habits = emptyList()
    }

    fun clearError() {
        error = ""
    }

    fun loadHabits() {
        val current = user ?: return

        habitsRepository.getHabits(
            current.uid,
            success = {
                habits = it
            },
            failure = {
                error = it
            }
        )
    }

    fun addHabit(
        name: String,
        description: String,
        onSaved: () -> Unit
    ) {
        if (name.isBlank()) {
            error = "Please enter a habit name."
            return
        }

        val current = user ?: return

        val habit = Habit(
            id = UUID.randomUUID().toString(),
            userId = current.uid,
            name = name.trim(),
            description = description.trim()
        )

        habitsRepository.addHabit(
            habit,
            success = {
                habits = habits + habit
                onSaved()
            },
            failure = {
                error = it
            }
        )
    }

    fun toggleHabit(habit: Habit) {
        val updated = HabitLogic.completeHabit(habit)

        habitsRepository.updateHabit(
            updated,
            success = {
                habits = habits.map {
                    if (it.id == updated.id) updated else it
                }
            },
            failure = {
                error = it
            }
        )
    }

    fun deleteHabit(habit: Habit) {
        habitsRepository.deleteHabit(
            habit,
            success = {
                habits = habits.filter { it.id != habit.id }
            },
            failure = {
                error = it
            }
        )
    }

    fun loadQuote() {
        viewModelScope.launch {
            quotes.getRandomQuote()
                .onSuccess {
                    quote = it
                }
                .onFailure {
                    error = "Could not load a quote. Check your Internet connection."
                }
        }
    }

    fun updateName(name: String) {
        if (name.isBlank()) {
            error = "Name cannot be empty."
            return
        }

        val current = auth.currentUser ?: return

        val update = UserProfileChangeRequest.Builder()
            .setDisplayName(name.trim())
            .build()

        current.updateProfile(update)
            .addOnSuccessListener {
                user = auth.currentUser
            }
            .addOnFailureListener {
                error = it.message ?: "Could not update name."
            }
    }
}