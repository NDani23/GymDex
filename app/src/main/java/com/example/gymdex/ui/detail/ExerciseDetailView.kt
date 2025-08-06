package com.example.gymdex.ui.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.TopAppBar
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.remember
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.gymdex.utils.NetworkImage
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Divider
import org.intellij.lang.annotations.JdkConstants.HorizontalAlignment

@Composable
fun ExerciseDetailScreen(
    exerciseId : Int,
    viewModel : ExerciseDetailViewModel,
    navController : NavController
) {
    viewModel.loadExercise(exerciseId)
    val exercise = viewModel.exercise.value
    val equipmentNames = viewModel.equipmentNames.value
    val isLoading = viewModel.isLoading.value
    val error = viewModel.error.value
    val colors = MaterialTheme.colorScheme

    //val favoriteIcon = if (exercise!!.favorite) Icons.Default.Star else Icons.Outlined.Star

    Scaffold(
        topBar = {
            TopAppBar(
                backgroundColor = colors.background,
                title = {
                    Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                    ) {
                        Text(
                        text = exercise?.name ?: "Exercise",
                        textAlign = TextAlign.Center,
                        color = Color.White,
                        style = MaterialTheme.typography.displayLarge
                    )
                } },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { viewModel.toggleFavorite() }) {
                        Icon(
                            imageVector = if (exercise?.favorite == true) Icons.Default.Favorite else Icons.Outlined.FavoriteBorder,
                            contentDescription = "SetFavorite",
                            tint = Color.White
                        )
                    }
                }
            )
        },
        modifier = Modifier
            .fillMaxSize()
            .background(colors.background)
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(colors.background)
                .padding(vertical = 30.dp),
            //verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                Column(
                    modifier = Modifier
                        .background(
                            color = colors.primary,
                            shape = RoundedCornerShape(16.dp)
                        )
                        .padding(20.dp)
                ) {
                    when {
                        isLoading -> CircularProgressIndicator()
                        error != null -> Text(text = "Error: $error")
                        exercise != null -> {
                            val imageUrls = remember(exercise) {
                                listOfNotNull(
                                    exercise.primaryImage?.takeIf { it.isNotEmpty() },
                                    exercise.secondaryImage?.takeIf { it.isNotEmpty() }
                                )
                            }
                            if (imageUrls.isNotEmpty()) {
                                val pagerState = rememberPagerState(pageCount = { imageUrls.size })
                                Column {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(300.dp)
                                            .padding(bottom = 8.dp)
                                            .background(
                                                color = Color.Transparent,
                                                shape = RoundedCornerShape(8.dp)
                                            ),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        HorizontalPager(
                                            state = pagerState,
                                            modifier = Modifier.fillMaxSize()
                                        ) { page ->
                                            NetworkImage(
                                                url = imageUrls[page],
                                                modifier = Modifier.fillMaxSize(),
                                                contentScale = ContentScale.Fit,
                                                circularRevealEnabled = true
                                            )
                                        }
                                    }
                                    // Pager indicator (dots)
                                    if (imageUrls.size > 1) {
                                        Row(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(bottom = 8.dp),
                                            horizontalArrangement = Arrangement.Center
                                        ) {
                                            repeat(imageUrls.size) { index ->
                                                Box(
                                                    modifier = Modifier
                                                        .padding(horizontal = 4.dp)
                                                        .size(8.dp)
                                                        .clip(CircleShape)
                                                        .background(
                                                            if (pagerState.currentPage == index) {
                                                                colors.onPrimary
                                                            } else {
                                                                colors.onPrimary.copy(alpha = 0.3f)
                                                            }
                                                        )
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                            val cleanedDescription = exercise.description
                                .replace("<p>", "")
                                .replace("<li>", "- ")
                                .replace("</li>", "")
                                .replace("<ul>", "")
                                .replace("<ol>", "")
                                .replace("</ol>", "")
                                .replace("</p>", "\n\n")
                                .trim()
                            cleanedDescription.split("\n\n").forEach { paragraph ->
                                Text(
                                    text = paragraph.trim(),
                                    modifier = Modifier.padding(vertical = 8.dp),
                                    textAlign = TextAlign.Start,
                                    style = MaterialTheme.typography.labelSmall,
                                    color = colors.onPrimary
                                )
                            }
                        }
                        else -> Text(
                            text = "Exercise not found",
                            style = MaterialTheme.typography.labelSmall,
                            color = colors.onPrimary
                        )
                    }
                }
            }
            if (equipmentNames.isNotEmpty()) {
                item {
                    Text(
                        text = "Required equipments:",
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 15.dp, bottom = 8.dp, start = 15.dp),
                        style = MaterialTheme.typography.bodyLarge,
                        textAlign = TextAlign.Start,
                        color = Color.White
                    )
                    Divider(
                        color = Color.White,
                        thickness = 1.dp,
                        modifier = Modifier.padding(bottom = 8.dp),
                    )
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp)
                            .padding(start = 15.dp)
                            .background(
                                color = colors.background
                            ),

                    ) {
                        equipmentNames.forEach { name ->
                            Column(
                                modifier = Modifier
                                    .width(200.dp)
                                    .padding(vertical = 8.dp)
                                    .background(
                                        color = colors.primary,
                                        shape = RoundedCornerShape(16.dp)
                                    ),
                                horizontalAlignment = Alignment.Start
                            ) {
                                Text(
                                    text = name,
                                    modifier = Modifier
                                        .padding(vertical = 4.dp)
                                        .fillMaxWidth(),
                                    textAlign = TextAlign.Center,
                                    style = MaterialTheme.typography.bodyLarge,
                                    color = colors.onPrimary,
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}