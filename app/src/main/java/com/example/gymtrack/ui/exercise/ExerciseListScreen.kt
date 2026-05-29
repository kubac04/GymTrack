package com.example.gymtrack.ui.exercise

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.gymtrack.ui.components.ExerciseTile
import com.example.gymtrack.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExerciseListScreen(
    viewModel: ExerciseViewModel,
    onBack: () -> Unit,
    onExerciseClick: (Int) -> Unit
) {
    val exercises by viewModel.exercises.collectAsStateWithLifecycle()
    val query     by viewModel.searchQuery.collectAsStateWithLifecycle()

    Scaffold(
        containerColor = Black,
        topBar = {
            TopAppBar(
                title = {
                    OutlinedTextField(
                        value         = query,
                        onValueChange = { viewModel.setSearchQuery(it) },
                        placeholder   = { Text("Search exercises...", color = Silver) },
                        leadingIcon   = { Icon(Icons.Default.Search, contentDescription = null, tint = Silver) },
                        modifier      = Modifier.fillMaxWidth(),
                        singleLine    = true,
                        colors        = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor      = MahoganyRed,
                            unfocusedBorderColor    = Silver,
                            focusedTextColor        = White,
                            unfocusedTextColor      = White,
                            cursorColor             = MahoganyRed,
                            focusedContainerColor   = CarbonBlack,
                            unfocusedContainerColor = CarbonBlack
                        )
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = CarbonBlack)
            )
        }
    ) { padding ->
        if (exercises.isEmpty()) {
            Box(
                modifier           = Modifier.fillMaxSize().padding(padding),
                contentAlignment   = Alignment.Center
            ) {
                Text("No exercises found", color = Silver, style = MaterialTheme.typography.bodyLarge)
            }
        } else {
            LazyColumn(
                modifier        = Modifier.fillMaxSize().padding(padding),
                contentPadding  = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                item {
                    Text(
                        text  = "All Exercises",
                        style = MaterialTheme.typography.titleMedium.copy(color = White),
                        modifier = Modifier.padding(bottom = 4.dp)
                    )
                }
                items(exercises) { exercise ->
                    ExerciseTile(
                        exercise = exercise,
                        onClick  = { onExerciseClick(exercise.id) }
                    )
                }
                item { Spacer(modifier = Modifier.height(8.dp)) }
            }
        }
    }
}
