package com.example.gymtrack.ui.workout

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.gymtrack.data.local.entity.WorkoutExerciseItem
import com.example.gymtrack.ui.components.muscleGroupStyle
import com.example.gymtrack.ui.theme.*

@Composable
fun WorkoutDetailScreen(
    viewModel: WorkoutDetailViewModel,
    onBack: () -> Unit,
    onEdit: () -> Unit,
    onAddExercise: () -> Unit,
    onComplete: (Int) -> Unit
) {
    val workout by viewModel.workout.collectAsStateWithLifecycle()
    val exercises by viewModel.exercises.collectAsStateWithLifecycle()
    var showCompleteDialog by remember { mutableStateOf(false) }

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
                    text = workout?.title ?: "",
                    color = MahoganyRed,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center
                )
                IconButton(onClick = onEdit) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Edit",
                        tint = White
                    )
                }
                // Complete (checkmark) button
                IconButton(onClick = { showCompleteDialog = true }) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = "Complete",
                        tint = Color(0xFF2ECC71)
                    )
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
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Icon(Icons.Default.Add, contentDescription = null, tint = White)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        "Add exercise",
                        color = White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                }
            }
        }
    ) { padding ->
        if (exercises.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("No exercises yet", color = Silver, fontSize = 18.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Tap \"Add exercise\" below", color = Silver, fontSize = 14.sp)
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp),
                contentPadding = PaddingValues(vertical = 16.dp)
            ) {
                items(exercises) { item ->
                    WorkoutExerciseRow(
                        item = item,
                        onToggle = { viewModel.toggleExercise(item) }
                    )
                }
            }
        }
    }

    if (showCompleteDialog) {
        AlertDialog(
            onDismissRequest = { showCompleteDialog = false },
            containerColor = CarbonBlack,
            title = { Text("Complete training?", color = White, fontWeight = FontWeight.Bold) },
            text = { Text("This will mark the training as done.", color = Silver) },
            confirmButton = {
                TextButton(onClick = {
                    showCompleteDialog = false
                    onComplete(viewModel.completeWorkout())
                }) {
                    Text("Complete", color = Color(0xFF2ECC71), fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showCompleteDialog = false }) {
                    Text("Cancel", color = Silver)
                }
            }
        )
    }
}

@Composable
private fun WorkoutExerciseRow(
    item: WorkoutExerciseItem,
    onToggle: () -> Unit
) {
    val style = muscleGroupStyle(item.muscleGroup)

    val checkBg by animateColorAsState(
        targetValue = if (item.isCompleted) Color(0xFF2ECC71) else Color.Transparent,
        animationSpec = tween(300),
        label = "cbBg"
    )
    val checkBorder by animateColorAsState(
        targetValue = if (item.isCompleted) Color(0xFF2ECC71) else Silver,
        animationSpec = tween(300),
        label = "cbBorder"
    )

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(CarbonBlack)
            .padding(horizontal = 14.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Checkbox
        Box(
            modifier = Modifier
                .size(28.dp)
                .clip(RoundedCornerShape(6.dp))
                .border(2.dp, checkBorder, RoundedCornerShape(6.dp))
                .background(checkBg)
                .clickable(onClick = onToggle),
            contentAlignment = Alignment.Center
        ) {
            if (item.isCompleted) {
                Icon(
                    Icons.Default.Check,
                    contentDescription = null,
                    tint = White,
                    modifier = Modifier.size(18.dp)
                )
            }
        }

        Spacer(modifier = Modifier.width(12.dp))

        // Muscle group thumbnail
        Box(
            modifier = Modifier
                .size(44.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(style.color),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = style.icon,
                contentDescription = item.muscleGroup,
                tint = White,
                modifier = Modifier.size(26.dp)
            )
        }

        Spacer(modifier = Modifier.width(12.dp))

        // Name + stats
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = item.exerciseName,
                color = if (item.isCompleted) Silver else White,
                fontWeight = FontWeight.SemiBold,
                fontSize = 15.sp
            )
            Spacer(modifier = Modifier.height(6.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                StatColumn("Sets", item.sets.toString())
                StatColumn("Reps", item.reps.toString())
                if (item.weightKg > 0f) StatColumn("Weight (kg)", item.weightKg.toInt().toString())
            }
        }
    }
}

@Composable
private fun StatColumn(label: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(label, color = Silver, fontSize = 10.sp, letterSpacing = 0.3.sp)
        Text(value, color = White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
    }
}
