package com.example.habitualise.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.habitualise.api.Quote
import com.example.habitualise.components.HabitCard
import com.example.habitualise.data.Habit

@Composable
fun HomeScreen(
    name: String,
    habits: List<Habit>,
    quote: Quote,
    onToggle: (Habit) -> Unit,
    onDelete: (Habit) -> Unit,
    onQuote: () -> Unit,
    onAdd: () -> Unit,
    onProgress: () -> Unit,
    onSettings: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Welcome, $name")

            Spacer(Modifier.weight(1f))

            OutlinedButton(onClick = onSettings) {
                Text("Settings")
            }
        }

        Spacer(Modifier.height(16.dp))

        Card(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text("\"${quote.content}\"")
                Text("- ${quote.author}")

                Spacer(Modifier.height(8.dp))

                Button(onClick = onQuote) {
                    Text("New quote")
                }
            }
        }

        Spacer(Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            Button(onClick = onAdd) {
                Text("Adds a habit")
            }

            Spacer(Modifier.weight(1f))

            OutlinedButton(onClick = onProgress) {
                Text("Progress")
            }
        }

        Spacer(Modifier.height(16.dp))

        Text("Your Habits")

        Spacer(Modifier.height(8.dp))

        LazyColumn {
            items(habits) { habit ->
                HabitCard(
                   habit = habit,
                    onToggle = onToggle,
                    onDelete = onDelete
                )
            }
        }
    }
}
