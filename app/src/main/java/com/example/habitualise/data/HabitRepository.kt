package com.example.habitualise.data

import com.google.firebase.firestore.FirebaseFirestore

class HabitRepository(
    private val database: FirebaseFirestore
) {

    private fun collection(userId: String) =
        database
            .collection("users")
            .document(userId)
            .collection("habits")

    fun getHabits(
        userId: String,
        success: (List<Habit>) -> Unit,
        failure: (String) -> Unit
    ) {
        collection(userId)
            .get()
            .addOnSuccessListener { result ->
                success(
                    result.toObjects(Habit::class.java)
                )
            }
            .addOnFailureListener {
                failure(it.message ?: "Could not load habits.")
            }
    }

    fun addHabit(
        habit: Habit,
        success: () -> Unit,
        failure: (String) -> Unit
    ) {
        collection(habit.userId)
            .document(habit.id)
            .set(habit)
            .addOnSuccessListener {
                success()
            }
            .addOnFailureListener {
                failure(it.message ?: "Could not save habit.")
            }
    }

    fun updateHabit(
        habit: Habit,
        success: () -> Unit,
        failure: (String) -> Unit
    ) {
        collection(habit.userId)
            .document(habit.id)
            .set(habit)
            .addOnSuccessListener {
                success()
            }
            .addOnFailureListener {
                failure(it.message ?: "Could not update habit.")
            }
    }

    fun deleteHabit(
        habit: Habit,
        success: () -> Unit,
        failure: (String) -> Unit
    ) {
        collection(habit.userId)
            .document(habit.id)
            .delete()
            .addOnSuccessListener {
                success()
            }
            .addOnFailureListener {
                failure(it.message ?: "Could not delete habit.")
            }
    }
}