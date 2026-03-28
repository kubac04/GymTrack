package com.example.gymtrack.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.gymtrack.R
import com.example.gymtrack.ui.theme.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward

@Composable
fun WelcomeScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Black)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(60.dp))

        // Logo
        Image(
            painter = painterResource(id = R.drawable.gymtrack_logo_white),
            contentDescription = "GymTrack Logo",
            modifier = Modifier.size(320.dp)
        )


        Spacer(modifier = Modifier.height(8.dp))

        // Tekst powitalny
        Text(
            text = "Welcome to GymTrack",
            color = White,
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(55.dp))
        Text(
            text = "Before you can start tracking your stats, you need to create a profile.",
            color = Silver,
            fontSize = 18.sp,
            lineHeight = 26.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 24.dp)
        )


        Spacer(modifier = Modifier.weight(1f))

        // Przycisk
        Button(
            onClick = {},
            modifier = Modifier
                .fillMaxWidth()
                .height(64.dp),
            colors = ButtonDefaults.buttonColors(containerColor = MahoganyRed),
            shape = RoundedCornerShape(32.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Create your profile",
                    color = White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.ExtraBold
                )
                Spacer(modifier = Modifier.width(12.dp))
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                    contentDescription = "Arrow",
                    tint = White,
                    modifier = Modifier.size(24.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}
//fun WelcomeScreen() {
//    Column(modifier = Modifier.padding(16.dp)) {
//        Text(text = "Welcome to GymTrack App")
//
//        Spacer(modifier = Modifier.height(8.dp))
//
//        Text(text = "Before you can start tracking your stats, you need to create a profile.")
//
//        Spacer(modifier = Modifier.height(16.dp))
//
//        Button(
//            onClick = {},
//            colors = ButtonDefaults.buttonColors(containerColor = MahoganyRed),
//            shape = RoundedCornerShape(100)
//        ) {
//            Row(verticalAlignment = Alignment.CenterVertically) {
//                Text(text = "Create your profile", color = White)
//                Spacer(modifier = Modifier.width(8.dp))
//                Icon(
//                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
//                    contentDescription = "Arrow",
//                    tint = White
//                )
//            }
//        }
//    }
//}

@Preview(showBackground = true)
@Composable
fun WelcomeScreenPreview() {
    WelcomeScreen()
}