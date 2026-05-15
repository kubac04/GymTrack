package com.example.gymtrack.ui.workout

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.ImageLoader
import coil.compose.AsyncImage
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import coil.request.ImageRequest
import com.example.gymtrack.data.local.entity.WorkoutExerciseItem
import com.example.gymtrack.ui.components.muscleGroupStyle
import com.example.gymtrack.ui.theme.*
import com.example.gymtrack.util.exerciseDrawableRes

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WorkoutDetailScreen(
    viewModel: WorkoutDetailViewModel,
    onBack: () -> Unit,
    onEdit: () -> Unit,
    onAddExercise: () -> Unit,
    onComplete: (Int) -> Unit
) {
    val workout   by viewModel.workout.collectAsStateWithLifecycle()
    val exercises by viewModel.exercises.collectAsStateWithLifecycle()
    var showCompleteDialog by remember { mutableStateOf(false) }

    Scaffold(
        containerColor = Black,
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text  = workout?.title ?: "",
                        style = MaterialTheme.typography.titleMedium.copy(color = MahoganyRed, fontWeight = FontWeight.Bold)
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = MahoganyRed)
                    }
                },
                actions = {
                    IconButton(onClick = onEdit) {
                        Icon(Icons.Default.Edit, contentDescription = "Edit", tint = White)
                    }
                    IconButton(onClick = { showCompleteDialog = true }) {
                        Icon(Icons.Default.Check, contentDescription = "Complete", tint = Color(0xFF2ECC71))
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = CarbonBlack)
            )
        },
        bottomBar = {
            Surface(color = Black, tonalElevation = 3.dp) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .navigationBarsPadding()
                        .padding(horizontal = 20.dp, vertical = 14.dp)
                ) {
                    Button(
                        onClick  = onAddExercise,
                        modifier = Modifier.fillMaxWidth().height(54.dp),
                        colors   = ButtonDefaults.buttonColors(containerColor = MahoganyRed),
                        shape    = RoundedCornerShape(14.dp)
                    ) {
                        Icon(Icons.Default.Add, contentDescription = null, tint = White)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            "Add exercise",
                            style = MaterialTheme.typography.labelLarge.copy(color = White, fontWeight = FontWeight.Bold)
                        )
                    }
                }
            }
        }
    ) { padding ->
        if (exercises.isEmpty()) {
            Box(
                modifier         = Modifier.fillMaxSize().padding(padding),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("No exercises yet", style = MaterialTheme.typography.bodyLarge.copy(color = Silver))
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Tap \"Add exercise\" below", style = MaterialTheme.typography.bodySmall.copy(color = Silver))
                }
            }
        } else {
            LazyColumn(
                modifier        = Modifier.fillMaxSize().padding(padding).padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp),
                contentPadding  = PaddingValues(vertical = 16.dp)
            ) {
                items(exercises) { item ->
                    WorkoutExerciseRow(
                        item     = item,
                        onToggle = { viewModel.toggleExercise(item) }
                    )
                }
            }
        }
    }

    if (showCompleteDialog) {
        AlertDialog(
            onDismissRequest = { showCompleteDialog = false },
            containerColor   = CarbonBlack,
            title   = { Text("Complete training?", style = MaterialTheme.typography.titleMedium.copy(color = White, fontWeight = FontWeight.Bold)) },
            text    = { Text("This will mark the training as done.", style = MaterialTheme.typography.bodyMedium.copy(color = Silver)) },
            confirmButton = {
                TextButton(onClick = {
                    showCompleteDialog = false
                    onComplete(viewModel.completeWorkout())
                }) {
                    Text("Complete", style = MaterialTheme.typography.labelLarge.copy(color = Color(0xFF2ECC71), fontWeight = FontWeight.Bold))
                }
            },
            dismissButton = {
                TextButton(onClick = { showCompleteDialog = false }) {
                    Text("Cancel", style = MaterialTheme.typography.labelLarge.copy(color = Silver))
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
    val style      = muscleGroupStyle(item.muscleGroup)
    val context    = LocalContext.current
    val drawableRes = remember(item.exerciseName) { exerciseDrawableRes(context, item.exerciseName) }

    val gifLoader = remember {
        ImageLoader.Builder(context)
            .components {
                if (android.os.Build.VERSION.SDK_INT >= 28) add(ImageDecoderDecoder.Factory())
                else add(GifDecoder.Factory())
            }
            .build()
    }

    ElevatedCard(
        modifier  = Modifier.fillMaxWidth(),
        shape     = RoundedCornerShape(14.dp),
        colors    = CardDefaults.elevatedCardColors(containerColor = CarbonBlack),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier          = Modifier.fillMaxWidth().padding(horizontal = 14.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked          = item.isCompleted,
                onCheckedChange  = { onToggle() },
                colors           = CheckboxDefaults.colors(
                    checkedColor   = Color(0xFF2ECC71),
                    uncheckedColor = Silver,
                    checkmarkColor = White
                )
            )

            Spacer(modifier = Modifier.width(4.dp))

            Box(
                modifier         = Modifier
                    .size(44.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(style.color),
                contentAlignment = Alignment.Center
            ) {
                if (drawableRes != 0) {
                    AsyncImage(
                        model              = ImageRequest.Builder(context).data(drawableRes).build(),
                        imageLoader        = gifLoader,
                        contentDescription = item.exerciseName,
                        contentScale       = ContentScale.Crop,
                        modifier           = Modifier.fillMaxSize()
                    )
                } else {
                    Icon(
                        imageVector        = style.icon,
                        contentDescription = item.muscleGroup,
                        tint               = White,
                        modifier           = Modifier.size(26.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text  = item.exerciseName,
                    style = Typography.titleSmall.copy(
                        color      = if (item.isCompleted) Silver else White,
                        fontWeight = FontWeight.SemiBold
                    )
                )
                Spacer(modifier = Modifier.height(6.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(20.dp, Alignment.CenterHorizontally)
                ) {
                    StatChip("Sets",  item.sets.toString())
                    StatChip("Reps",  item.reps.toString())
                    if (item.weightKg > 0f) StatChip("kg", item.weightKg.toInt().toString())
                }
            }
        }
    }
}

@Composable
private fun StatChip(label: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(label, style = MaterialTheme.typography.labelSmall.copy(color = Silver))
        Text(value, style = MaterialTheme.typography.titleSmall.copy(color = White, fontWeight = FontWeight.Bold))
    }
}
