package com.example.gymtrack.ui.exercise

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.PlayArrow
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
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.ImageLoader
import coil.compose.AsyncImage
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import coil.request.ImageRequest
import com.example.gymtrack.ui.components.muscleGroupStyle
import com.example.gymtrack.ui.theme.*
import com.example.gymtrack.util.exerciseDrawableRes

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExerciseDetailScreen(
    exerciseId: Int,
    viewModel: ExerciseViewModel,
    onBack: () -> Unit,
    onAddToWorkout: (Int) -> Unit
) {
    val exerciseFlow = remember(exerciseId) { viewModel.getExerciseById(exerciseId) }
    val exercise     by exerciseFlow.collectAsStateWithLifecycle(initialValue = null)
    val context      = LocalContext.current

    val gifLoader = remember {
        ImageLoader.Builder(context)
            .components {
                if (android.os.Build.VERSION.SDK_INT >= 28) add(ImageDecoderDecoder.Factory())
                else add(GifDecoder.Factory())
            }
            .build()
    }

    Scaffold(
        containerColor = Black,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text  = exercise?.name ?: "",
                        style = MaterialTheme.typography.titleMedium.copy(color = White, fontWeight = FontWeight.Bold)
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Default.ArrowBack, contentDescription = "Back", tint = MahoganyRed)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = CarbonBlack)
            )
        },
        bottomBar = {
            exercise?.let { ex ->
                Surface(color = Black, tonalElevation = 3.dp) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .navigationBarsPadding()
                            .padding(horizontal = 20.dp, vertical = 14.dp)
                    ) {
                        Button(
                            onClick  = { onAddToWorkout(ex.id) },
                            modifier = Modifier.fillMaxWidth().height(54.dp),
                            colors   = ButtonDefaults.buttonColors(containerColor = MahoganyRed),
                            shape    = RoundedCornerShape(14.dp)
                        ) {
                            Text(
                                "Add to current workout",
                                style = MaterialTheme.typography.labelLarge.copy(color = White, fontWeight = FontWeight.Bold)
                            )
                        }
                    }
                }
            }
        }
    ) { padding ->
        exercise?.let { ex ->
            val style         = muscleGroupStyle(ex.muscleGroup)
            val localImageRes = remember(ex.name) { exerciseDrawableRes(context, ex.name) }
            val hasLocalImage = localImageRes != 0

            // Wyciągamy video ID z embed URL
            val videoId = remember(ex.videoUrl) {
                ex.videoUrl
                    .removePrefix("https://www.youtube.com/embed/")
                    .split("?").first()
            }
            val youtubeWatchUrl  = "https://www.youtube.com/watch?v=$videoId"
            val thumbnailUrl     = "https://img.youtube.com/vi/$videoId/hqdefault.jpg"

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .verticalScroll(rememberScrollState())
            ) {
                // ── Header strip ──────────────────────────────────────────
                Surface(modifier = Modifier.fillMaxWidth(), color = style.color, tonalElevation = 2.dp) {
                    Row(
                        modifier          = Modifier.padding(horizontal = 20.dp, vertical = 16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier         = Modifier
                                .size(52.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(White.copy(alpha = 0.15f)),
                            contentAlignment = Alignment.Center
                        ) {
                            if (hasLocalImage) {
                                AsyncImage(
                                    model              = ImageRequest.Builder(context).data(localImageRes).build(),
                                    imageLoader        = gifLoader,
                                    contentDescription = ex.name,
                                    contentScale       = ContentScale.Crop,
                                    modifier           = Modifier.fillMaxSize()
                                )
                            } else {
                                Icon(style.icon, contentDescription = null, tint = White, modifier = Modifier.size(32.dp))
                            }
                        }
                        Spacer(modifier = Modifier.width(16.dp))
                        Column {
                            Text(ex.name, style = MaterialTheme.typography.titleLarge.copy(color = White, fontWeight = FontWeight.Bold))
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(ex.muscleGroup, style = MaterialTheme.typography.bodySmall.copy(color = White.copy(alpha = 0.8f)))
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // LOCAL GIF / image
                if (hasLocalImage) {
                    Text(
                        text = "Exercise preview",
                        color = White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        modifier = Modifier.padding(horizontal = 20.dp)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp)
                            .height(220.dp)
                            .clip(RoundedCornerShape(14.dp))
                            .background(CarbonBlack),
                        contentAlignment = Alignment.Center
                    ) {
                        AsyncImage(
                            model = ImageRequest.Builder(context)
                                .data(localImageRes)
                                .crossfade(true)
                                .build(),
                            imageLoader = gifLoader,
                            contentDescription = ex.name,
                            contentScale = ContentScale.Fit,
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                }

// YouTube button — always shown when videoUrl is present, regardless of GIF
                if (ex.videoUrl.isNotEmpty()) {
                    OutlinedButton(
                        onClick = {
                            context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(youtubeWatchUrl)))
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp)
                            .height(46.dp),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = MahoganyRed),
                        border = androidx.compose.foundation.BorderStroke(1.5.dp, MahoganyRed),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Icon(Icons.Default.PlayArrow, contentDescription = null, tint = MahoganyRed)
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Watch tutorial on YouTube", color = MahoganyRed, fontWeight = FontWeight.SemiBold)
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // ── Opis + kroki ──────────────────────────────────────────
                Column(modifier = Modifier.padding(horizontal = 20.dp)) {
                    Text(ex.description, style = MaterialTheme.typography.bodyMedium.copy(color = Silver), lineHeight = 22.sp)
                    Spacer(modifier = Modifier.height(20.dp))
                    Text("How to perform", style = MaterialTheme.typography.titleMedium.copy(color = White, fontWeight = FontWeight.Bold))
                    Spacer(modifier = Modifier.height(12.dp))

                    ex.steps.lines().forEachIndexed { index, step ->
                        if (step.isNotBlank()) {
                            val clean = step.trimStart { it.isDigit() || it == '.' || it == ' ' }
                            Row(modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp)) {
                                Box(
                                    modifier         = Modifier
                                        .size(24.dp)
                                        .clip(RoundedCornerShape(6.dp))
                                        .background(style.color),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text("${index + 1}",
                                        style = MaterialTheme.typography.labelSmall.copy(color = White, fontWeight = FontWeight.Bold))
                                }
                                Spacer(modifier = Modifier.width(12.dp))
                                Text(clean,
                                    style      = MaterialTheme.typography.bodySmall.copy(color = WhiteSmoke),
                                    lineHeight = 20.sp,
                                    modifier   = Modifier.weight(1f))
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }
    }
}
