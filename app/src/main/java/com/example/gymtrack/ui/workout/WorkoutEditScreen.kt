package com.example.gymtrack.ui.workout

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DragHandle
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.gymtrack.data.local.entity.WorkoutExerciseItem
import com.example.gymtrack.ui.components.muscleGroupStyle
import com.example.gymtrack.ui.theme.*

@Composable
fun WorkoutEditScreen(
    viewModel: WorkoutDetailViewModel,
    onBack: () -> Unit,
    onAddExercise: () -> Unit
) {
    val workout by viewModel.workout.collectAsStateWithLifecycle()
    val exercises by viewModel.exercises.collectAsStateWithLifecycle()

    Scaffold(
        containerColor = Black,
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(CarbonBlack)
                    .padding(horizontal = 8.dp, vertical = 4.dp)
                    .statusBarsPadding(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBack) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = MahoganyRed)
                }
                Text(
                    text = "Edit training",
                    color = White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    modifier = Modifier.weight(1f)
                )
                TextButton(onClick = onBack) {
                    Text("Done", color = MahoganyRed, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                }
            }
        },
        bottomBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(CarbonBlack)
                    .padding(horizontal = 20.dp, vertical = 14.dp)
                    .navigationBarsPadding()
            ) {
                Button(
                    onClick = onAddExercise,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(54.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = MahoganyRed),
                    shape = RoundedCornerShape(28.dp)
                ) {
                    Text(
                        "Add another exercise",
                        color = White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                }
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 20.dp)
        ) {
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = workout?.title ?: "",
                color = MahoganyRed,
                fontWeight = FontWeight.Bold,
                fontSize = 22.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))

            if (exercises.isEmpty()) {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("No exercises yet", color = Silver, fontSize = 16.sp)
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(exercises) { item ->
                        EditableRow(
                            item = item,
                            onRemove = { viewModel.removeExercise(item) },
                            onUpdate = { sets, reps, weight ->
                                viewModel.updateExercise(item, sets, reps, weight)
                            }
                        )
                    }
                    item { Spacer(modifier = Modifier.height(8.dp)) }
                }
            }
        }
    }
}

@Composable
private fun EditableRow(
    item: WorkoutExerciseItem,
    onRemove: () -> Unit,
    onUpdate: (Int, Int, Float) -> Unit
) {
    val style = muscleGroupStyle(item.muscleGroup)
    var sets   by remember(item.workoutExerciseId) { mutableStateOf(item.sets.toString()) }
    var reps   by remember(item.workoutExerciseId) { mutableStateOf(item.reps.toString()) }
    var weight by remember(item.workoutExerciseId) {
        mutableStateOf(if (item.weightKg > 0f) item.weightKg.toInt().toString() else "")
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(CarbonBlack)
            .padding(horizontal = 12.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Drag handle
        Icon(
            Icons.Default.DragHandle,
            contentDescription = null,
            tint = Silver,
            modifier = Modifier.size(20.dp)
        )

        Spacer(modifier = Modifier.width(10.dp))

        // Muscle group icon
        Box(
            modifier = Modifier
                .size(36.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(style.color),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = style.icon,
                contentDescription = item.muscleGroup,
                tint = White,
                modifier = Modifier.size(20.dp)
            )
        }

        Spacer(modifier = Modifier.width(10.dp))

        Column(modifier = Modifier.weight(1f)) {
            // Exercise name + delete button
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = item.exerciseName,
                    color = White,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 14.sp,
                    modifier = Modifier.weight(1f)
                )
                IconButton(onClick = onRemove, modifier = Modifier.size(28.dp)) {
                    Icon(
                        Icons.Default.Close,
                        contentDescription = "Remove",
                        tint = StrawberryRed,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Sets / Reps / Weight fields
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                NumberBox(label = "Sets", value = sets, onValueChange = { v ->
                    sets = v
                    onUpdate(v.toIntOrNull() ?: item.sets, reps.toIntOrNull() ?: item.reps, weight.toFloatOrNull() ?: 0f)
                })
                NumberBox(label = "Reps", value = reps, onValueChange = { v ->
                    reps = v
                    onUpdate(sets.toIntOrNull() ?: item.sets, v.toIntOrNull() ?: item.reps, weight.toFloatOrNull() ?: 0f)
                })
                NumberBox(label = "Weight (kg)", value = weight, onValueChange = { v ->
                    weight = v
                    onUpdate(sets.toIntOrNull() ?: item.sets, reps.toIntOrNull() ?: item.reps, v.toFloatOrNull() ?: 0f)
                })
            }
        }
    }
}

@Composable
private fun NumberBox(label: String, value: String, onValueChange: (String) -> Unit) {
    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.width(64.dp)) {
        Text(label, color = Silver, fontSize = 10.sp, textAlign = TextAlign.Center, maxLines = 1)
        Spacer(modifier = Modifier.height(4.dp))
        Box(
            modifier = Modifier
                .size(42.dp)
                .clip(RoundedCornerShape(8.dp))
                .border(1.5.dp, Silver, RoundedCornerShape(8.dp))
                .background(Black),
            contentAlignment = Alignment.Center
        ) {
            BasicTextField(
                value = value,
                onValueChange = onValueChange,
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                cursorBrush = SolidColor(MahoganyRed),
                textStyle = TextStyle(
                    color = White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    textAlign = TextAlign.Center
                ),
                modifier = Modifier.fillMaxWidth().padding(horizontal = 4.dp)
            )
        }
    }
}
