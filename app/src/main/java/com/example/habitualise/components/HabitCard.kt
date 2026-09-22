package com.example.habitualise.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.habitualise.data.Habit

@Composable
fun HabitCard(
    habit: Habit,
    onToggle: (Habit) -> Unit,
    onDelete: (Habit) -> Unit
) {
    Card {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = habit.completed,
                onCheckedChange = {
                    onToggle(habit)
                }
            )

            Column(
                Modifier.weight(1f)
            ) {
                Text(habit.name)

                if (habit.description.isNotBlank()) {
                    Text(habit.description)
                }

                Text("${habit.points} points")
            }

            TextButton(
                onClick = {
                    onDelete(habit)
                }
            ) {
                Text("Delete")
            }
        }
    }
}