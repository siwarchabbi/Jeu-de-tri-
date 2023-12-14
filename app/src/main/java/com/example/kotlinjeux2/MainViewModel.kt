package com.example.kotlinjeux2

import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import kotlin.random.Random

class MainViewModel :ViewModel() {

    var isCurrentlyDragging by mutableStateOf(false)
        private set

    var items by mutableStateOf(emptyList<PersonUiItem>())
        private set

    var addedPersons = mutableStateListOf<PersonUiItem>()
        private set

    init {
        val randomNumbers = List(5) { Random.nextInt(100) + 1 }

        val items = randomNumbers.map { number ->
            PersonUiItem("$number", "$number", when (number) {
                1 -> Color.Gray
                2 -> Color.Blue
                3 -> Color.Green
                4 -> Color.Cyan
                5 -> Color.Magenta
                else -> Color.Black
            })
        }

        this.items = items
    }

    fun startDragging(){
        isCurrentlyDragging = true
    }
    fun stopDragging(){
        isCurrentlyDragging = false
    }

    fun addPerson(personUiItem: PersonUiItem){
        println("Added number")
        addedPersons.add(personUiItem)
    }

}