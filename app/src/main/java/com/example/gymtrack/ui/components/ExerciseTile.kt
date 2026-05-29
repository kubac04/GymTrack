package com.example.gymtrack.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.remember
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.ImageLoader
import coil.compose.AsyncImage
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import coil.request.ImageRequest
import com.example.gymtrack.data.local.entity.ExerciseEntity
import com.example.gymtrack.util.exerciseDrawableRes
import com.example.gymtrack.ui.theme.*

@Composable
fun ExerciseTile(
    exercise: ExerciseEntity,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val style         = muscleGroupStyle(exercise.muscleGroup)
    val context       = LocalContext.current
    val localImageRes = remember(exercise.name) { exerciseDrawableRes(context, exercise.name) }

    val gifLoader = remember {
        ImageLoader.Builder(context)
            .components {
                if (android.os.Build.VERSION.SDK_INT >= 28) add(ImageDecoderDecoder.Factory())
                else add(GifDecoder.Factory())
            }
            .build()
    }

    ElevatedCard(
        onClick   = onClick,
        modifier  = modifier.fillMaxWidth(),
        shape     = RoundedCornerShape(14.dp),
        colors    = CardDefaults.elevatedCardColors(containerColor = CarbonBlack),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier           = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 12.dp),
            verticalAlignment  = Alignment.CenterVertically
        ) {
            Box(
                modifier           = Modifier
                    .size(52.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(style.color),
                contentAlignment   = Alignment.Center
            ) {
                if (localImageRes != 0) {
                    AsyncImage(
                        model             = ImageRequest.Builder(context).data(localImageRes).build(),
                        imageLoader       = gifLoader,
                        contentDescription = exercise.name,
                        contentScale      = ContentScale.Crop,
                        modifier          = Modifier.fillMaxSize()
                    )
                } else {
                    Icon(
                        imageVector        = style.icon,
                        contentDescription = exercise.muscleGroup,
                        tint               = White,
                        modifier           = Modifier.size(28.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.width(14.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text       = exercise.name,
                    style      = MaterialTheme.typography.titleSmall.copy(color = White, fontWeight = FontWeight.SemiBold)
                )
                Spacer(modifier = Modifier.height(3.dp))
                Text(
                    text  = exercise.muscleGroup,
                    style = MaterialTheme.typography.labelSmall.copy(color = style.color, fontWeight = FontWeight.Medium)
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text     = exercise.description,
                    style    = MaterialTheme.typography.bodySmall.copy(color = Silver),
                    maxLines = 1
                )
            }
        }
    }
}
