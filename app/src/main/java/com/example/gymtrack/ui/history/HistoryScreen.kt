package com.example.gymtrack.ui.history

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.gymtrack.R
import com.example.gymtrack.data.local.entity.WorkoutEntity
import com.example.gymtrack.ui.components.BottomNavTab
import com.example.gymtrack.ui.components.BottomNavigationBar
import com.example.gymtrack.ui.components.WorkoutTile
import com.example.gymtrack.ui.theme.*

// Wrapper łączący ViewModel z UI tego pasku zadan na dole i przekierowania
@Composable
fun HistoryScreen(
    viewModel: HistoryViewModel,
    onProfileClick: () -> Unit = {}
) {
    val workouts by viewModel.workouts.collectAsStateWithLifecycle()
    HistoryScreenContent(
        workouts = workouts,
        onAddWorkout = { title -> viewModel.addWorkout(title) },
        onProfileClick = onProfileClick
    )
}

@Composable
fun HistoryScreenContent(
    workouts: List<WorkoutEntity> = emptyList(),
    onAddWorkout: (String) -> Unit = {},
    onProfileClick: () -> Unit = {}
) {
    var showAddDialog by remember { mutableStateOf(false) }
    var newWorkoutTitle by remember { mutableStateOf("") }

    Scaffold(
        bottomBar = {
            BottomNavigationBar(
                selectedTab = BottomNavTab.HISTORY,
                onTabSelected = { tab ->
                    when (tab) {
                        BottomNavTab.NEW_TRAINING -> showAddDialog = true
                        BottomNavTab.PROFILE -> onProfileClick()
                        else -> {}
                    }
                }
            )
        },
        containerColor = Black
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Black)
                .padding(innerPadding)
                .padding(horizontal = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            // Logo
            Image(
                painter = painterResource(id = R.drawable.gymtrack_logo_white),
                contentDescription = "GymTrack Logo",
                modifier = Modifier
                    .width(290.dp)
                    .height(250.dp)
            )

            Spacer(modifier = Modifier.height(0.dp))

            // Workout list or empty state
            if (workouts.isEmpty()) {
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = "None trainings",
                    color = Silver,
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Medium,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.weight(1f))
            } else {
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(workouts) { workout ->
                        WorkoutTile(
                            title = workout.title,
                            dateMillis = workout.date,
                            isCompleted = workout.isCompleted
                        )
                    }
                    item { Spacer(modifier = Modifier.height(8.dp)) }
                }
            }
        }
    }

    // Dialog to add new training
    if (showAddDialog) {
        AlertDialog(
            onDismissRequest = {
                showAddDialog = false
                newWorkoutTitle = ""
            },
            containerColor = CarbonBlack,
            title = {
                Text(
                    text = "New Training",
                    color = White,
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Column {
                    Text(
                        text = "Enter training name:",
                        color = Silver,
                        fontSize = 14.sp
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    OutlinedTextField(
                        value = newWorkoutTitle,
                        onValueChange = { newWorkoutTitle = it },
                        placeholder = {
                            Text(
                                text = "e.g. Friday - back",
                                color = Silver
                            )
                        },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = MahoganyRed,
                            unfocusedBorderColor = Silver,
                            focusedTextColor = White,
                            unfocusedTextColor = White,
                            cursorColor = MahoganyRed
                        ),
                        singleLine = true
                    )
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        if (newWorkoutTitle.isNotBlank()) {
                            onAddWorkout(newWorkoutTitle.trim())
                            newWorkoutTitle = ""
                            showAddDialog = false
                        }
                    }
                ) {
                    Text(text = "Add", color = MahoganyRed, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        showAddDialog = false
                        newWorkoutTitle = ""
                    }
                ) {
                    Text(text = "Cancel", color = Silver)
                }
            }
        )
    }
}

// Preview — pusty ekran (None training)
@Preview(showBackground = true, name = "History - pusty")
@Composable
fun HistoryScreenEmptyPreview() {
    GymTrackTheme {
        HistoryScreenContent(workouts = emptyList())
    }
}

// Preview — ekran z przykładowymi treningami
@Preview(showBackground = true, name = "History - z treningami")
@Composable
fun HistoryScreenWithDataPreview() {
    GymTrackTheme {
        HistoryScreenContent(
            workouts = listOf(
                WorkoutEntity(uid = 1, title = "Friday - back",  date = 1742425200000L, isCompleted = true),
                WorkoutEntity(uid = 2, title = "Monday - chest", date = 1742166000000L, isCompleted = true),
                WorkoutEntity(uid = 3, title = "Wednesday - legs", date = 1741993200000L, isCompleted = false),
            )
        )
    }
}