package com.example.gymtrack.ui.workout

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
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
fun WorkoutEditScreen(
    viewModel: WorkoutDetailViewModel,
    onBack: () -> Unit,
    onAddExercise: () -> Unit
) {
    val workout   by viewModel.workout.collectAsStateWithLifecycle()
    val exercises by viewModel.exercises.collectAsStateWithLifecycle()

    Scaffold(
        containerColor = Black,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text  = "Edit training",
                        style = MaterialTheme.typography.titleMedium.copy(color = White, fontWeight = FontWeight.Bold)
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = MahoganyRed)
                    }
                },
                actions = {
                    TextButton(onClick = onBack) {
                        Text("Done", style = MaterialTheme.typography.labelLarge.copy(color = MahoganyRed, fontWeight = FontWeight.Bold))
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = CarbonBlack)
            )
        },
        bottomBar = {
            Surface(color = CarbonBlack, tonalElevation = 3.dp) {
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
                        shape    = RoundedCornerShape(28.dp)
                    ) {
                        Text(
                            "Add another exercise",
                            style = MaterialTheme.typography.labelLarge.copy(color = White, fontWeight = FontWeight.Bold)
                        )
                    }
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
                text     = workout?.title ?: "",
                style    = MaterialTheme.typography.titleLarge.copy(color = MahoganyRed, fontWeight = FontWeight.Bold),
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))

            if (exercises.isEmpty()) {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("No exercises yet", style = MaterialTheme.typography.bodyLarge.copy(color = Silver))
                }
            } else {
                LazyColumn(
                    modifier            = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(exercises) { item ->
                        EditableRow(
                            item     = item,
                            onRemove = { viewModel.removeExercise(item) },
                            onUpdate = { sets, reps, weight -> viewModel.updateExercise(item, sets, reps, weight) }
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

    var sets   by remember(item.workoutExerciseId) { mutableStateOf(item.sets.toString()) }
    var reps   by remember(item.workoutExerciseId) { mutableStateOf(item.reps.toString()) }
    var weight by remember(item.workoutExerciseId) {
        mutableStateOf(if (item.weightKg > 0f) item.weightKg.toInt().toString() else "")
    }

    ElevatedCard(
        modifier  = Modifier.fillMaxWidth(),
        shape     = RoundedCornerShape(14.dp),
        colors    = CardDefaults.elevatedCardColors(containerColor = CarbonBlack),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier          = Modifier.fillMaxWidth().padding(horizontal = 12.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(Icons.Default.DragHandle, contentDescription = null, tint = Silver, modifier = Modifier.size(20.dp))
            Spacer(modifier = Modifier.width(10.dp))

            Box(
                modifier         = Modifier
                    .size(36.dp)
                    .clip(RoundedCornerShape(8.dp))
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
                        modifier           = Modifier.size(20.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.width(10.dp))

            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text     = item.exerciseName,
                        style    = MaterialTheme.typography.titleSmall.copy(color = White, fontWeight = FontWeight.SemiBold),
                        modifier = Modifier.weight(1f)
                    )
                    IconButton(onClick = onRemove, modifier = Modifier.size(28.dp)) {
                        Icon(Icons.Default.Close, contentDescription = "Remove", tint = StrawberryRed, modifier = Modifier.size(18.dp))
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
                    NumberField(label = "Sets", value = sets, onValueChange = { v ->
                        sets = v
                        onUpdate(v.toIntOrNull() ?: item.sets, reps.toIntOrNull() ?: item.reps, weight.toFloatOrNull() ?: 0f)
                    })
                    NumberField(label = "Reps", value = reps, onValueChange = { v ->
                        reps = v
                        onUpdate(sets.toIntOrNull() ?: item.sets, v.toIntOrNull() ?: item.reps, weight.toFloatOrNull() ?: 0f)
                    })
                    NumberField(label = "kg", value = weight, onValueChange = { v ->
                        weight = v
                        onUpdate(sets.toIntOrNull() ?: item.sets, reps.toIntOrNull() ?: item.reps, v.toFloatOrNull() ?: 0f)
                    })
                }
            }
        }
    }
}

@Composable
private fun NumberField(label: String, value: String, onValueChange: (String) -> Unit) {
    OutlinedTextField(
        value         = value,
        onValueChange = onValueChange,
        label         = { Text(label, style = MaterialTheme.typography.labelSmall) },
        singleLine    = true,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        modifier      = Modifier.width(72.dp),
        textStyle     = MaterialTheme.typography.titleSmall.copy(
            color     = White,
            fontWeight = FontWeight.Bold,
            textAlign  = TextAlign.Center
        ),
        colors        = OutlinedTextFieldDefaults.colors(
            focusedBorderColor      = MahoganyRed,
            unfocusedBorderColor    = Silver,
            focusedTextColor        = White,
            unfocusedTextColor      = White,
            cursorColor             = MahoganyRed,
            focusedLabelColor       = MahoganyRed,
            unfocusedLabelColor     = Silver,
            focusedContainerColor   = Black,
            unfocusedContainerColor = Black
        )
    )
}
