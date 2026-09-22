package com.example.habitualise.auth

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest

class AuthRepository(
    private val auth: FirebaseAuth
) {

    val currentUser
        get() = auth.currentUser

    fun register(
        name: String,
        email: String,
        password: String,
        success: () -> Unit,
        failure: (String) -> Unit
    ) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                val user = auth.currentUser
                if (user == null) {
                    failure("Registration failed.")
                    return@addOnSuccessListener
                }

                val update = UserProfileChangeRequest.Builder()
                    .setDisplayName(name)
                    .build()

                user.updateProfile(update)
                    .addOnSuccessListener {
                        success()
                    }
                    .addOnFailureListener {
                        failure(it.message ?: "Could not save profile.")
                    }
            }
            .addOnFailureListener {
                failure(it.message ?: "Registration failed.")
            }
    }

    fun login(
        email: String,
        password: String,
        success: () -> Unit,
        failure: (String) -> Unit
    ) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                success()
            }
            .addOnFailureListener {
                failure(it.message ?: "Login failed.")
            }
    }

    fun logout() {
        auth.signOut()
    }
}