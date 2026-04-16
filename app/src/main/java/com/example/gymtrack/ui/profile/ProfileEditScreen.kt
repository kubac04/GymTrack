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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.gymtrack.R
import com.example.gymtrack.ui.components.BottomNavTab
import com.example.gymtrack.ui.components.BottomNavigationBar
import com.example.gymtrack.ui.theme.*

@Composable
fun ProfileEditScreen(
    viewModel: ProfileViewModel,
    onHistoryClick: () -> Unit = {},
    onAddClick: () -> Unit = {}
) {
    val userProfile by viewModel.userProfile.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    var firstName by remember { mutableStateOf("") }
    var weight    by remember { mutableStateOf("") }
    var height    by remember { mutableStateOf("") }

    // Wypełnij pola przy pierwszym załadowaniu danych z bazy , o ile użytkownik jest w bazie
    var fieldsInitialized by remember { mutableStateOf(false) }
    LaunchedEffect(userProfile) {
        if (!fieldsInitialized) {
            userProfile?.let {
                firstName         = it.firstName
                weight            = it.weight.toString()
                height            = it.height.toString()
                fieldsInitialized = true
            }
        }
    }

    // Nasłuchuj na sygnał "zapisano" z ViewModelu i pokaże potwierdzenie zapisu
    LaunchedEffect(Unit) {
        viewModel.saveSuccess.collect {
            snackbarHostState.showSnackbar(
                message  = "Profile saved!",
                duration = SnackbarDuration.Short
            )
        }
    }

    ProfileEditContent(
        firstName         = firstName,
        weight            = weight,
        height            = height,
        onFirstNameChange = { firstName = it },
        onWeightChange    = { weight = it },
        onHeightChange    = { height = it },
        snackbarHostState = snackbarHostState,
        onSubmit = {
            viewModel.saveProfile(firstName.trim(), weight.trim(), height.trim())
        },
        onHistoryClick = onHistoryClick,
        onAddClick     = onAddClick
    )
}

@Composable
fun ProfileEditContent(
    firstName: String = "",
    weight: String = "",
    height: String = "",
    onFirstNameChange: (String) -> Unit = {},
    onWeightChange: (String) -> Unit = {},
    onHeightChange: (String) -> Unit = {},
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() },
    onSubmit: () -> Unit = {},
    onHistoryClick: () -> Unit = {},
    onAddClick: () -> Unit = {}
) {
    var errorMessage by remember { mutableStateOf("") }
    val scrollState  = rememberScrollState()

    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState) { data ->
                Snackbar(
                    snackbarData    = data,
                    containerColor  = MahoganyRed,
                    contentColor    = White
                )
            }
        },
        bottomBar = {
            BottomNavigationBar(
                selectedTab   = BottomNavTab.PROFILE,
                onTabSelected = { tab ->
                    when (tab) {
                        BottomNavTab.HISTORY      -> onHistoryClick()
                        BottomNavTab.NEW_TRAINING -> onAddClick()
                        else -> {}
                    }
                }
            )
        },
        containerColor = Black
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Black)
                .padding(innerPadding)
                .padding(horizontal = 24.dp)
                .verticalScroll(scrollState),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            // Logo
            Image(
                painter            = painterResource(id = R.drawable.gymtrack_logo_white),
                contentDescription = "GymTrack Logo",
                modifier           = Modifier
                    .width(290.dp)
                    .height(220.dp)
            )

            Spacer(modifier = Modifier.height(0.dp))

            // Ikona profilowa
            Icon(
                imageVector        = Icons.Default.AccountCircle,
                contentDescription = "Profile picture",
                tint               = Color(0xFF555555),
                modifier           = Modifier
                    .size(90.dp)
                    .clip(CircleShape)
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Przycisk zmiany zdjęcia DO ZROBINEIA JESZCZE
            OutlinedButton(
                onClick          = { /* TODO: image picker */ },
                shape            = RoundedCornerShape(8.dp),
                colors           = ButtonDefaults.outlinedButtonColors(contentColor = MahoganyRed),
                border           = androidx.compose.foundation.BorderStroke(1.5.dp, MahoganyRed),
                contentPadding   = PaddingValues(horizontal = 14.dp, vertical = 4.dp)
            ) {
                Text(
                    text       = "Change picture",
                    color      = MahoganyRed,
                    fontSize   = 13.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text       = "Edit your profile:",
                color      = White,
                fontSize   = 20.sp,
                fontWeight = FontWeight.Bold,
                textAlign  = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(16.dp))

            ProfileEditField(
                value         = firstName,
                onValueChange = onFirstNameChange,
                placeholder   = "First Name"
            )
            Spacer(modifier = Modifier.height(12.dp))
            ProfileEditField(
                value         = weight,
                onValueChange = onWeightChange,
                placeholder   = "Weight (kg)"
            )
            Spacer(modifier = Modifier.height(12.dp))
            ProfileEditField(
                value         = height,
                onValueChange = onHeightChange,
                placeholder   = "Height (cm)"
            )

            if (errorMessage.isNotEmpty()) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text      = errorMessage,
                    color     = StrawberryRed,
                    fontSize  = 14.sp,
                    textAlign = TextAlign.Center
                )
            }

            Spacer(modifier = Modifier.height(28.dp))

            // Submit changes
            Button(
                onClick = {
                    val normalizedWeight = weight.replace(',', '.')
                    when {
                        firstName.isBlank()                  -> errorMessage = "Please enter your first name."
                        normalizedWeight.toFloatOrNull() == null -> errorMessage = "Please enter a valid weight."
                        height.toIntOrNull() == null          -> errorMessage = "Please enter a valid height."
                        else -> {
                            errorMessage = ""
                            onSubmit()
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(containerColor = MahoganyRed),
                shape  = RoundedCornerShape(28.dp)
            ) {
                Row(
                    verticalAlignment   = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text       = "Submit changes",
                        color      = White,
                        fontSize   = 18.sp,
                        fontWeight = FontWeight.ExtraBold
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Icon(
                        imageVector        = Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = null,
                        tint               = White,
                        modifier           = Modifier.size(20.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
private fun ProfileEditField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String
) {
    TextField(
        value         = value,
        onValueChange = onValueChange,
        placeholder   = {
            Text(
                text      = placeholder,
                color     = Color.Gray,
                modifier  = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )
        },
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .clip(RoundedCornerShape(12.dp)),
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor   = DustGrey,
            unfocusedContainerColor = DustGrey,
            focusedBorderColor      = Color.Transparent,
            unfocusedBorderColor    = Color.Transparent,
            cursorColor             = Black,
            focusedTextColor        = Black,
            unfocusedTextColor      = Color(0xFF444444)
        ),
        textStyle = MaterialTheme.typography.bodyLarge.copy(textAlign = TextAlign.Center),
        singleLine = true
    )
}

@Preview(showBackground = true, name = "Profile Edit")
@Composable
fun ProfileEditPreview() {
    GymTrackTheme {
        ProfileEditContent(
            firstName = "Jak Kowalski",
            weight    = "86.7",
            height    = "187"
        )
    }
}
