package com.example.gymtrack.ui.exercise

import android.content.Intent
import android.net.Uri
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.ImageLoader
import coil.compose.AsyncImage
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import coil.request.ImageRequest
import com.example.gymtrack.ui.components.muscleGroupStyle
import com.example.gymtrack.ui.theme.*
import com.example.gymtrack.util.exerciseDrawableRes

@Composable
fun ExerciseDetailScreen(
    exerciseId: Int,
    viewModel: ExerciseViewModel,
    onBack: () -> Unit,
    onAddToWorkout: (Int) -> Unit
) {
    val exerciseFlow = remember(exerciseId) { viewModel.getExerciseById(exerciseId) }
    val exercise by exerciseFlow.collectAsStateWithLifecycle(initialValue = null)
    val context = LocalContext.current

    val gifLoader = remember {
        ImageLoader.Builder(context)
            .components {
                if (android.os.Build.VERSION.SDK_INT >= 28) add(ImageDecoderDecoder.Factory())
                else add(GifDecoder.Factory())
            }
            .build()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Black)
    ) {
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
                text = exercise?.name ?: "",
                color = White,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                modifier = Modifier.weight(1f)
            )
        }

        exercise?.let { ex ->
            val style = muscleGroupStyle(ex.muscleGroup)
            val localImageRes = remember(ex.name) { exerciseDrawableRes(context, ex.name) }
            val hasLocalImage = localImageRes != 0

            // YouTube watch URL extracted from embed URL
            val youtubeWatchUrl = remember(ex.videoUrl) {
                val videoId = ex.videoUrl
                    .removePrefix("https://www.youtube.com/embed/")
                    .split("?").first()
                "https://www.youtube.com/watch?v=$videoId"
            }

            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
            ) {
                // Header strip
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(style.color)
                        .padding(horizontal = 20.dp, vertical = 16.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(52.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(White.copy(alpha = 0.15f)),
                            contentAlignment = Alignment.Center
                        ) {
                            if (hasLocalImage) {
                                AsyncImage(
                                    model = ImageRequest.Builder(context).data(localImageRes).build(),
                                    imageLoader = gifLoader,
                                    contentDescription = ex.name,
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier.fillMaxSize()
                                )
                            } else {
                                Icon(style.icon, contentDescription = null, tint = White, modifier = Modifier.size(32.dp))
                            }
                        }
                        Spacer(modifier = Modifier.width(16.dp))
                        Column {
                            Text(ex.name, color = White, fontWeight = FontWeight.Bold, fontSize = 20.sp)
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(ex.muscleGroup, color = White.copy(alpha = 0.8f), fontSize = 13.sp)
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

                    // YouTube jako przycisk (nie ramka) gdy jest lokalny GIF
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
                } else {
                    // Brak lokalnego GIF-a — pokaz WebView z YouTube
                    if (ex.videoUrl.isNotEmpty()) {
                        Text(
                            text = "Exercise video",
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
                                .height(210.dp)
                                .clip(RoundedCornerShape(14.dp))
                                .background(CarbonBlack)
                        ) {
                            AndroidView(
                                factory = { ctx ->
                                    WebView(ctx).apply {
                                        webViewClient = WebViewClient()
                                        webChromeClient = WebChromeClient()
                                        settings.javaScriptEnabled = true
                                        settings.mediaPlaybackRequiresUserGesture = false
                                        settings.domStorageEnabled = true
                                        val html = """
                                            <html><head>
                                            <style>body{margin:0;padding:0;background:#000;}
                                            iframe{display:block;width:100%;height:100%;border:none;}</style>
                                            </head><body>
                                            <iframe src="${ex.videoUrl}?rel=0&controls=1"
                                              allow="accelerometer; autoplay; encrypted-media; gyroscope"
                                              allowfullscreen></iframe>
                                            </body></html>
                                        """.trimIndent()
                                        loadDataWithBaseURL("https://www.youtube.com", html, "text/html", "utf-8", null)
                                    }
                                },
                                modifier = Modifier.fillMaxSize()
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Opis + kroki
                Column(modifier = Modifier.padding(horizontal = 20.dp)) {
                    Text(ex.description, color = Silver, fontSize = 15.sp, lineHeight = 22.sp)
                    Spacer(modifier = Modifier.height(20.dp))
                    Text("How to perform", color = White, fontWeight = FontWeight.Bold, fontSize = 17.sp)
                    Spacer(modifier = Modifier.height(12.dp))

                    ex.steps.lines().forEachIndexed { index, step ->
                        if (step.isNotBlank()) {
                            val clean = step.trimStart { it.isDigit() || it == '.' || it == ' ' }
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(bottom = 12.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(24.dp)
                                        .clip(RoundedCornerShape(6.dp))
                                        .background(style.color),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text("${index + 1}", color = White, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                                }
                                Spacer(modifier = Modifier.width(12.dp))
                                Text(clean, color = WhiteSmoke, fontSize = 14.sp, lineHeight = 20.sp, modifier = Modifier.weight(1f))
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(CarbonBlack)
                    .padding(horizontal = 20.dp, vertical = 14.dp)
                    .navigationBarsPadding()
            ) {
                Button(
                    onClick = { onAddToWorkout(ex.id) },
                    modifier = Modifier.fillMaxWidth().height(54.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = MahoganyRed),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Text("Add to current workout", color = White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                }
            }
        }
    }
}
