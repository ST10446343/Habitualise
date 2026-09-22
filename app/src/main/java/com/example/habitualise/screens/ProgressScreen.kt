package com.example.habitualise.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.habitualise.data.Habit

@Composable
fun ProgressScreen(
    habits: List<Habit>,
    onBack: () -> Unit
) {
    val points = habits.sumOf { it.points }

    Column(
        Modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {
        TextButton(onClick = onBack) {
            Text("← Back")
        }

        Text("Your Progress")

        Spacer(Modifier.height(20.dp))

        Text("Habits: ${habits.size}")

        Spacer(Modifier.height(8.dp))

        Text("Points: $points")

        Spacer(Modifier.height(8.dp))

        Text("Keep going!")
    }
}