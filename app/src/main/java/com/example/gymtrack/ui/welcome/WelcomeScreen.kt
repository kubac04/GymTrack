package com.example.gymtrack.ui.welcome

import android.net.Uri
import android.widget.MediaController
import android.widget.VideoView
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.example.gymtrack.R
import com.example.gymtrack.ui.theme.*

@Composable
fun WelcomeScreen(onNextClick: () -> Unit = {}) {
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Black)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(48.dp))

        // Logo
        Image(
            painter            = painterResource(id = R.drawable.gymtrack_logo_white),
            contentDescription = "GymTrack Logo",
            modifier           = Modifier.size(220.dp)
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text      = "Welcome to GymTrack",
            style     = MaterialTheme.typography.headlineMedium.copy(
                color      = White,
                fontWeight = FontWeight.ExtraBold
            ),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(24.dp))

        // ── Player wideo ──────────────────────────────────────────────────
        ElevatedCard(
            modifier  = Modifier
                .fillMaxWidth()
                .height(220.dp),
            shape     = RoundedCornerShape(16.dp),
            colors    = CardDefaults.elevatedCardColors(containerColor = CarbonBlack),
            elevation = CardDefaults.elevatedCardElevation(defaultElevation = 4.dp)
        ) {
            AndroidView(
                modifier = Modifier.fillMaxSize(),
                factory  = { ctx ->
                    VideoView(ctx).apply {
                        val uri = Uri.parse(
                            "android.resource://${ctx.packageName}/${R.raw.intro_video}"
                        )
                        setVideoURI(uri)

                        // Kontrolki play/pause/seek pojawiają się po tapnięciu
                        val mc = MediaController(ctx)
                        mc.setAnchorView(this)
                        setMediaController(mc)
                    }
                }
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text  = "Tap the video to play",
            style = MaterialTheme.typography.bodySmall.copy(color = Silver),
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text      = "Before you can start tracking your stats, you need to create a profile.",
            style     = MaterialTheme.typography.bodyLarge.copy(color = Silver),
            lineHeight = MaterialTheme.typography.bodyLarge.lineHeight,
            textAlign = TextAlign.Center,
            modifier  = Modifier.padding(horizontal = 8.dp)
        )

        Spacer(modifier = Modifier.height(36.dp))

        // Przycisk
        Button(
            onClick  = onNextClick,
            modifier = Modifier
                .fillMaxWidth()
                .height(64.dp),
            colors   = ButtonDefaults.buttonColors(containerColor = MahoganyRed),
            shape    = RoundedCornerShape(32.dp)
        ) {
            Row(
                verticalAlignment     = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text  = "Create your profile",
                    style = MaterialTheme.typography.labelLarge.copy(
                        color      = White,
                        fontWeight = FontWeight.ExtraBold
                    )
                )
                Spacer(modifier = Modifier.width(12.dp))
                Icon(
                    imageVector        = Icons.AutoMirrored.Filled.ArrowForward,
                    contentDescription = null,
                    tint               = White,
                    modifier           = Modifier.size(24.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(32.dp))
    }
}

@Preview(showBackground = true)
@Composable
fun WelcomeScreenPreview() {
    WelcomeScreen()
}
