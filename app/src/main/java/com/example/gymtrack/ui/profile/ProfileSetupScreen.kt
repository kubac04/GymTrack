package com.example.gymtrack.ui.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.gymtrack.R
import com.example.gymtrack.ui.theme.*

@Composable
fun ProfileScreen(onNextClick: () -> Unit = {}) {
    // Zapisanie zmiennych by się odświeżały i np przy obruceniu ekranu nie były resetowane
    var firstName by remember { mutableStateOf("") }
    var weight by remember { mutableStateOf("") }
    var height by remember { mutableStateOf("") }
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Black)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(scrollState),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(2.dp))

            // Logo
            Image(
                painter = painterResource(id = R.drawable.gymtrack_logo_white),
                contentDescription = "GymTrack Logo",
                modifier = Modifier.size(320.dp)
            )

            // Ikona profilu użytkownika
            Icon(
                imageVector = Icons.Default.AccountCircle,
                contentDescription = "Profile Icon",
                tint = Color.DarkGray,
                modifier = Modifier
                    .offset(y = (-45).dp)
                    .size(90.dp)
                    .clip(CircleShape)
            )


            Text(
                text = "Enter your details:",
                color = White,
                style = MaterialTheme.typography.titleLarge, // Zgodne z Type.kt (Rubik SemiBold)
                textAlign = TextAlign.Center,
                modifier = Modifier.offset(y = (-20).dp)
            )

            Spacer(modifier = Modifier.height(15.dp))

            // Pola tekstowe
            Column(
                modifier = Modifier.offset(y = (-10).dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                ProfileTextField(value = firstName, onValueChange = { firstName = it }, label = "First Name")
                Spacer(modifier = Modifier.height(12.dp))
                ProfileTextField(value = weight, onValueChange = { weight = it }, label = "Weight")
                Spacer(modifier = Modifier.height(12.dp))
                ProfileTextField(value = height, onValueChange = { height = it }, label = "Height")
            }
            
            Spacer(modifier = Modifier.height(32.dp))
        }

        // Przycisk Submit
        Button(
            onClick = onNextClick,
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
                    text = "Submit",
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

@Composable
fun ProfileTextField(value: String, onValueChange: (String) -> Unit, label: String) {
    TextField(
        value = value,
        onValueChange = onValueChange,
        placeholder = { 
            Text(
                text = label, 
                color = Color.Gray, 
                modifier = Modifier.fillMaxWidth(), 
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodyLarge
            ) 
        },
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .clip(RoundedCornerShape(12.dp)),
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = DustGrey,
            unfocusedContainerColor = DustGrey,
            focusedBorderColor = Color.Transparent,
            unfocusedBorderColor = Color.Transparent,
            cursorColor = Black,
            focusedTextColor = Black,
            unfocusedTextColor = Black
        ),
        textStyle = MaterialTheme.typography.bodyLarge.copy(textAlign = TextAlign.Center),
        singleLine = true
    )
}

@Preview(showBackground = true)
@Composable
fun ProfileScreenPreview() {
    GymTrackTheme {
        ProfileScreen()
    }
}