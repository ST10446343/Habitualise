package com.example.habitualise.data

object HabitLogic {

    fun completeHabit(
        habit: Habit
    ): Habit {
        return habit.copy(
            completed = !habit.completed,
            points = if (!habit.completed)
                habit.points + 10
            else
                maxOf(0, habit.points - 10)
        )
    }
}