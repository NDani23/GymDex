package com.example.gymdex.ui.main

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.runtime.Composable
import androidx.compose.material.Button
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.Alignment
import androidx.compose.material.Text
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ButtonDefaults
import androidx.compose.ui.unit.dp
import androidx.compose.material3.MaterialTheme
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.structuralEqualityPolicy
import androidx.compose.runtime.derivedStateOf
import androidx.compose.ui.draw.clip
import com.example.gymdex.R

@Composable
fun HomeScreen(
    viewModel: HomeViewModel,
    onMuscleGroupClick: (Int) -> Unit = {},
) {
    val muscleGroups = viewModel.muscleGroups.value
    val isLoading = viewModel.isLoading.value
    val colors = MaterialTheme.colorScheme
    val pagerState = rememberPagerState(pageCount = { 2 })

    VerticalPager(
        state = pagerState,
        modifier = Modifier
            .fillMaxSize()
            .background(colors.background)
    ) { page ->
        when (page) {
            0 -> {
                // Page 1: Welcome Section
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.rounded_logo),
                        contentDescription = "GymDex Logo",
                        modifier = Modifier
                            .padding(bottom = 24.dp)
                            .height(130.dp)
                            .clip(RoundedCornerShape(50))
                    )
                    Column(
                        modifier = Modifier
                            .background(
                                color = colors.primary,
                                shape = RoundedCornerShape(16.dp)
                            )
                            .padding(20.dp)
                    ) {
                        Text(
                            text = "Welcome to GymDex",
                            style = MaterialTheme.typography.displayLarge,
                            color = colors.onPrimary,
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .padding(bottom = 16.dp)
                                .fillMaxWidth()
                        )
                        Text(
                            text = "Here you can browse hundreds of gym exercises so you don't have to walk around aimlessly the next time you are there." +
                                    " You are only a few taps away from finding the next game changing exercise you need! Lets maximize those gain!",
                            style = MaterialTheme.typography.labelSmall,
                            textAlign = TextAlign.Justify,
                            color = colors.onPrimary
                        )
                    }
                    Text(
                        text = "↓ Swipe down to continue ↓",
                        style = MaterialTheme.typography.labelSmall,
                        color = colors.onSecondary.copy(alpha = 0.5f),
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .padding(top = 16.dp)
                            .padding(horizontal = 12.dp, vertical = 6.dp)
                    )
                }
            }
            1 -> {
                // Page 2: Muscle Group List
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 80.dp, vertical = 70.dp),
                    verticalArrangement = Arrangement.SpaceEvenly,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    if (isLoading) {
                        CircularProgressIndicator(
                            color = colors.primary,
                            modifier = Modifier.padding(16.dp)
                        )
                    } else if (muscleGroups.isNotEmpty()) {
                        muscleGroups.forEach { muscleGroup ->
                            Button(
                                onClick = {
                                    onMuscleGroupClick(muscleGroup.id)
                                },
                                colors = ButtonDefaults.buttonColors(
                                    backgroundColor = colors.primary,
                                    contentColor = colors.onPrimary
                                ),
                                shape = RoundedCornerShape(50),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 8.dp)
                            ) {
                                Text(
                                    text = muscleGroup.name,
                                    style = MaterialTheme.typography.bodyLarge
                                )
                            }
                        }
                    } else {
                        Text(
                            text = "No muscle groups found",
                            color = colors.onSecondary,
                            style = MaterialTheme.typography.bodyLarge,
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                }
            }
        }
    }
}