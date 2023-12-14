package com.example.kotlinjeux2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*

import androidx.compose.ui.Modifier

import androidx.compose.ui.graphics.Color

import com.example.kotlinjeux2.ui.theme.Kotlinjeux2Theme

class MainActivity : ComponentActivity() {
    private val viewModel = MainViewModel()

    // Instantiate your DatabaseHelper here
    private val databaseHelper = DatabaseHelper(context = this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Kotlinjeux2Theme {
                DragableScreen(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(0.8f))
                ) {
                    MainScreen(viewModel, databaseHelper = databaseHelper)
                }
            }
        }
    }
}

