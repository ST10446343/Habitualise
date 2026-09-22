package com.example.habitualise

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.habitualise.ui.HabitualiseApp
import com.example.habitualise.ui.theme.HabitualiseTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            HabitualiseTheme {
                HabitualiseApp()
            }
        }
    }
}