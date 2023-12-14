package com.example.kotlinjeux2


import android.R
import android.app.AlertDialog
import android.util.Log
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn

import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*

import androidx.compose.runtime.Composable


@Composable
fun MainScreen(
    mainViewModel: MainViewModel, databaseHelper: DatabaseHelper
) {

    val screenWidth = LocalConfiguration.current.screenWidthDp


    val startTime = remember { mutableStateOf(0L) } // Store the start time when the button is clicked
    val elapsedTime = remember { mutableStateOf(0L) } // Store the elapsed time

    var showDialog by remember { mutableStateOf(false) }



    Column(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(50.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            mainViewModel.items.forEach { person ->
                DragTarget(
                    dataToDrop = person,
                    viewModel = mainViewModel
                ) {
                    Box(
                        modifier = Modifier
                            .size(Dp(screenWidth / 5f))
                            .clip(RoundedCornerShape(15.dp))
                            .shadow(5.dp, RoundedCornerShape(15.dp)),
                        contentAlignment = Alignment.Center,
                    ) {
                        Text(
                            text = person.name,
                            style = MaterialTheme.typography.body1,
                            color = Color.White,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }
        }

        AnimatedVisibility(
            mainViewModel.isCurrentlyDragging,
            enter = slideInHorizontally(initialOffsetX = { it })
        ) {
            DropItem<PersonUiItem>(

                modifier = Modifier
                    .size(Dp(screenWidth / 3.5f))
            ) { isInBound, personItem ->
                if (personItem != null) {
                    startTime.value = System.currentTimeMillis() // Capture the start time when the button is clicked

                    if (personItem != null) {
                        LaunchedEffect(key1 = personItem) {

                            mainViewModel.addPerson(personItem)
                        }
                    }
                }

                if (isInBound) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .border(
                                1.dp,
                                color = Color.Red,
                                shape = RoundedCornerShape(15.dp)
                            )
                            .background(Color.Gray.copy(0.5f), RoundedCornerShape(15.dp))
                        ,
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Add Number",
                            style = MaterialTheme.typography.body1,
                            color = Color.Black
                        )
                    }
                } else {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .border(
                                1.dp,
                                color = Color.White,
                                shape = RoundedCornerShape(15.dp)
                            )
                            .background(
                                Color.Black.copy(0.5f),
                                RoundedCornerShape(15.dp)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Add Number",
                            style = MaterialTheme.typography.body1,
                            color = Color.White
                        )
                    }
                }




            }
        }
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 30.dp)
            ,
            contentAlignment = Alignment.Center
        ){
            Column( modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally) {
                Button(
                    onClick = {
                        val addedPersonNames = mainViewModel.addedPersons.map { it.name }
                        elapsedTime.value = System.currentTimeMillis() - startTime.value // Calculate the elapsed time

                        if (isAscendingOrder(addedPersonNames)) {
                            Log.d("OrderCheck", "Numbers are in ascending order, Score: ${elapsedTime.value}")

                            // Save the score to the database
                            val playerName = "siwar" // Replace with the actual player name
                            val time = elapsedTime.value.toInt()
                            databaseHelper.saveScore(playerName, time)

                            // Optionally, retrieve and print the top 10 scores from the database
                            val topScores = databaseHelper.getTopScores(10)
                            Log.d("TopScores", "Top 10 Scores: $topScores")
                        } else {
                            Log.d("OrderCheck", "Numbers are not in ascending order")
                        }
                    },
                    enabled = true
                ) {
                    Text(
                        text = "Check Order",
                        style = MaterialTheme.typography.body1,
                        color = Color.White
                    )
                }
                Button(
                    onClick = {
                        showDialog = true
                    }
                ) {
                    Text(
                        text = "View table score",
                        style = MaterialTheme.typography.body1,
                        color = Color.White
                    )
                }

                if (showDialog) {
                    AlertDialog(
                        onDismissRequest = {
                            // Handle dismiss if needed
                            showDialog = false
                        },
                        title = {
                            Text(text = "Top 10 Scores")
                        },
                        text = {
                            // Fetch data from the database and display it here
                            val topScores = databaseHelper.getTopScores(10)
                            LazyColumn {
                                items(topScores) { score ->
                                    Text(text = score.toString())
                                }
                            }
                        },
                        confirmButton = {
                            // Dismiss the dialog when the confirm button is clicked
                            TextButton(
                                onClick = {
                                    showDialog = false
                                }
                            ) {
                                Text("OK")
                            }
                        }
                    )
                }

            }


            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
                    .padding(bottom = 100.dp)
                ,
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ){
                Text(
                    text = "List Numbers ",
                    color = Color.White,
                    style = MaterialTheme.typography.h6,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Start
                )
                mainViewModel.addedPersons.forEach { person ->
                    Text(
                        text = person.name,
                        color = Color.White,
                        style = MaterialTheme.typography.h6,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

    }

}

private fun isAscendingOrder(numbers: List<String>): Boolean {
    if (numbers.isEmpty()) return true

    for (i in 1 until numbers.size) {
        val num1 = numbers[i - 1].toInt()
        val num2 = numbers[i].toInt()

        if (num1 > num2) return false
    }

    return true
}
