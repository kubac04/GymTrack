package com.example.gymtrack.ui.exercise

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.gymtrack.ui.components.ExerciseTile
import com.example.gymtrack.ui.theme.*

@Composable
fun ExerciseListScreen(
    viewModel: ExerciseViewModel,
    onBack: () -> Unit,
    onExerciseClick: (Int) -> Unit
) {
    val exercises by viewModel.exercises.collectAsStateWithLifecycle()
    val query by viewModel.searchQuery.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Black)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(CarbonBlack)
                .padding(horizontal = 8.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBack) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = White)
            }
            OutlinedTextField(
                value = query,
                onValueChange = { viewModel.setSearchQuery(it) },
                placeholder = { Text("Search exercises...", color = Silver) },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = Silver) },
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(12.dp)),
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MahoganyRed,
                    unfocusedBorderColor = Silver,
                    focusedTextColor = White,
                    unfocusedTextColor = White,
                    cursorColor = MahoganyRed,
                    focusedContainerColor = CarbonBlack,
                    unfocusedContainerColor = CarbonBlack
                )
            )
            Spacer(modifier = Modifier.width(8.dp))
        }

        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = "All Exercises",
            color = White,
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp,
            modifier = Modifier.padding(horizontal = 20.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))

        if (exercises.isEmpty()) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("No exercises found", color = Silver, fontSize = 18.sp)
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(horizontal = 20.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(exercises) { exercise ->
                    ExerciseTile(
                        exercise = exercise,
                        onClick = { onExerciseClick(exercise.id) }
                    )
                }
                item { Spacer(modifier = Modifier.height(16.dp)) }
            }
        }
    }
}
