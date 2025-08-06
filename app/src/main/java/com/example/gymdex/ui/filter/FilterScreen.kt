package com.example.gymdex.ui.filter

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Checkbox
import androidx.compose.material.CheckboxDefaults
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.RadioButton
import androidx.compose.material.RadioButtonDefaults
import androidx.compose.material.Switch
import androidx.compose.material.SwitchDefaults
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.gymdex.ui.list.ExerciseListViewModel

@Composable
fun FilterScreen(
    viewModel: FilterViewModel,
    navController: NavController,
    onBackClick: () -> Unit = {}
) {
    val equipments = viewModel.equipments.value
    val muscleGroups = viewModel.muscleGroups.value
    val isLoading = viewModel.isLoading.value

    val showFavoritesOnly = viewModel.showFavoritesOnly.value
    val selectedMuscleGroupId = viewModel.selectedMuscleGroupId.value
    val selectedEquipmentIds = viewModel.selectedEquipmentIds.value
    val noEquipmentNeeded = viewModel.noEquipmentNeeded.value

    val colors = MaterialTheme.colorScheme

    Scaffold (
        topBar = {
            TopAppBar(
                backgroundColor = colors.background,
                title = {
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Filter exercises",
                            textAlign = TextAlign.Center,
                            color = Color.White,
                            style = MaterialTheme.typography.displayLarge
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = { onBackClick() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }
                },
                actions = {
                    Text(
                        text = "Show",
                        style = MaterialTheme.typography.bodyMedium,
                        color = colors.background,
                        modifier = Modifier
                            .background(
                                color = Color.White,
                                shape = RoundedCornerShape(8.dp)
                            )
                            .clickable {
                                viewModel.applyFilters()?.let { route ->
                                    navController.navigate(route) {
                                        popUpTo("filter") { inclusive = true }
                                    }
                                }
                            }
                            .padding(horizontal = 12.dp, vertical = 6.dp)
                            .align(Alignment.CenterVertically)
                    )
                }
            )
        },
        modifier = Modifier
            .fillMaxSize()
            .background(colors.background)
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp)
                        .background(
                            color = Color.White
                        )
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Show only favorite exercises",
                        style = MaterialTheme.typography.bodyLarge,
                        color = colors.background
                    )
                    Switch(
                        checked = showFavoritesOnly,
                        onCheckedChange = { viewModel.updateFavoritesOnly(it) },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = Color.Green,
                            checkedTrackColor = colors.secondary,
                            uncheckedThumbColor = colors.background,
                            uncheckedTrackColor = colors.background
                        )
                    )
                }
                Text(
                    text = "Muscle group:",
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
                if (muscleGroups.isNotEmpty()) {
                    muscleGroups.forEach { muscle ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                                .background(
                                    color = colors.primary,
                                    shape = RoundedCornerShape(15.dp)
                                )
                                .padding(horizontal = 16.dp, vertical = 8.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = muscle.name,
                                style = MaterialTheme.typography.bodyMedium,
                                color = colors.onPrimary
                            )
                            RadioButton(
                                selected = selectedMuscleGroupId == muscle.id,
                                onClick = { viewModel.updateMuscleGroupId(muscle.id) },
                                colors = RadioButtonDefaults.colors(
                                    selectedColor = colors.secondary,
                                    unselectedColor = colors.onPrimary.copy(alpha = 0.5f)
                                )
                            )
                        }
                    }
                } else if (!isLoading) {
                    Text(
                        text = "No muscle groups available",
                        style = MaterialTheme.typography.bodyMedium,
                        color = colors.onPrimary,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                }
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
                if (!isLoading) {
                    // No equipment needed checkbox
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                            .background(
                                color = colors.primary,
                                shape = RoundedCornerShape(15.dp)
                            )
                            .padding(horizontal = 16.dp, vertical = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "No equipment needed",
                            style = MaterialTheme.typography.bodyMedium,
                            color = colors.onPrimary
                        )
                        Checkbox(
                            checked = noEquipmentNeeded,
                            onCheckedChange = { viewModel.updateNoEquipmentNeeded(it) },
                            colors = CheckboxDefaults.colors(
                                checkedColor = colors.secondary,
                                uncheckedColor = colors.onPrimary.copy(alpha = 0.5f)
                            )
                        )
                    }
                    // Equipment checkboxes
                    if (equipments.isNotEmpty()) {
                        equipments.forEach { equipment ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp)
                                    .background(
                                        color = colors.primary,
                                        shape = RoundedCornerShape(15.dp)
                                    )
                                    .padding(horizontal = 16.dp, vertical = 8.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = equipment.name,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = colors.onPrimary
                                )
                                Checkbox(
                                    checked = selectedEquipmentIds.contains(equipment.id),
                                    onCheckedChange = { viewModel.updateEquipmentIds(equipment.id, it)
                                    },
                                    colors = CheckboxDefaults.colors(
                                        checkedColor = colors.secondary,
                                        uncheckedColor = colors.onPrimary.copy(alpha = 0.5f)
                                    )
                                )
                            }
                        }
                    } else {
                        Text(
                            text = "No equipment available",
                            style = MaterialTheme.typography.bodyMedium,
                            color = colors.onPrimary,
                            modifier = Modifier.padding(vertical = 8.dp)
                        )
                    }
                }

            }
        }

    }

}