package com.example.gymtrack.ui.workout

import android.media.MediaPlayer
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.gymtrack.R
import com.example.gymtrack.ui.theme.*

@Composable
fun WorkoutSummaryScreen(
    caloriesBurned: Int,
    onContinue: () -> Unit
) {
    val context = LocalContext.current

    DisposableEffect(Unit) {
        val mediaPlayer = MediaPlayer.create(context, R.raw.yeah_buddy)
        mediaPlayer?.start()
        onDispose {
            mediaPlayer?.stop()
            mediaPlayer?.release()
        }
    }

    val animatedValue = remember { Animatable(0f) }
    LaunchedEffect(caloriesBurned) {
        animatedValue.animateTo(
            targetValue     = caloriesBurned.toFloat(),
            animationSpec   = tween(
                durationMillis = 2000,
                easing         = EaseOutQuart
            )
        )
    }
    val displayedCalories = animatedValue.value.toInt()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Black),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("👏", fontSize = 80.sp)

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text       = "Congrats!",
            color      = White,
            fontWeight = FontWeight.Bold,
            fontSize   = 32.sp
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text       = "Training completed!",
            color      = White,
            fontWeight = FontWeight.Bold,
            fontSize   = 24.sp
        )

        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text     = "You burned around:",
            color    = Silver,
            fontSize = 18.sp
        )

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            verticalAlignment     = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text("🔥", fontSize = 40.sp)
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text       = "$displayedCalories kcal",
                color      = MahoganyRed,
                fontWeight = FontWeight.Bold,
                fontSize   = 42.sp
            )
        }

        Spacer(modifier = Modifier.height(48.dp))

        Button(
            onClick  = onContinue,
            modifier = Modifier
                .width(200.dp)
                .height(52.dp),
            colors = ButtonDefaults.buttonColors(containerColor = MahoganyRed),
            shape  = RoundedCornerShape(12.dp)
        ) {
            Text("Continue", color = White, fontWeight = FontWeight.Bold, fontSize = 18.sp)
        }
    }
}