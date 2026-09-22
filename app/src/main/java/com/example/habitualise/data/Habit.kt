package com.example.habitualise.data

data class Habit(
    val id: String = "",
    val userId: String = "",
    val name: String = "",
    val description: String = "",
    val completed: Boolean = false,
    val points: Int = 0
)