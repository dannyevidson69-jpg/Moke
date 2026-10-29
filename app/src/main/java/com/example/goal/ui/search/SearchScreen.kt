package com.example.goal.ui.search

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.goal.data.model.VideoType
import com.example.goal.ui.GoalViewModel
import com.example.goal.ui.Screen
import com.example.goal.ui.components.VideoCard
import com.example.goal.ui.theme.GoalBackground
import com.example.goal.ui.theme.GoalOnSurfaceVariant
import com.example.goal.ui.theme.GoalPrimary
import com.example.goal.ui.theme.GoalSurface
import com.example.goal.ui.theme.GoalSurfaceVariant

@Composable
fun SearchScreen(
    viewModel: GoalViewModel,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    BackHandler { onBack() }

    val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()
    val allVideos by viewModel.allVideos.collectAsStateWithLifecycle()

    var filterType by remember { mutableStateOf("All") }
    val filterTabs = listOf("All", "Videos", "Shorts", "Esports", "Football")

    val searchResults = allVideos.filter { v ->
        val matchesQuery = searchQuery.isBlank() ||
                v.title.contains(searchQuery, ignoreCase = true) ||
                v.description.contains(searchQuery, ignoreCase = true) ||
                v.username.contains(searchQuery, ignoreCase = true) ||
                v.category.contains(searchQuery, ignoreCase = true)

        val matchesFilter = when (filterType) {
            "Videos" -> v.type == VideoType.STANDARD
            "Shorts" -> v.type == VideoType.SHORT
            "Esports" -> v.category.equals("Esports", ignoreCase = true)
            "Football" -> v.category.equals("Football", ignoreCase = true)
            else -> true
        }

        matchesQuery && matchesFilter
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(GoalBackground)
            .testTag("search_screen")
    ) {
        // Search Input Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBack) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = Color.White
                )
            }

            OutlinedTextField(
                value = searchQuery,
                onValueChange = { viewModel.updateSearchQuery(it) },
                placeholder = { Text("Search videos, #goal, esports...", color = Color.Gray, fontSize = 14.sp) },
                leadingIcon = {
                    Icon(Icons.Default.Search, contentDescription = null, tint = GoalPrimary)
                },
                trailingIcon = {
                    if (searchQuery.isNotEmpty()) {
                        IconButton(onClick = { viewModel.updateSearchQuery("") }) {
                            Icon(Icons.Default.Clear, contentDescription = "Clear", tint = Color.Gray)
                        }
                    }
                },
                modifier = Modifier
                    .weight(1f)
                    .testTag("search_text_input"),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = GoalPrimary,
                    unfocusedBorderColor = GoalSurfaceVariant,
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White
                ),
                shape = RoundedCornerShape(24.dp),
                singleLine = true
            )
        }

        // Filter Tabs
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 6.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            filterTabs.forEach { tab ->
                val isSelected = filterType == tab
                FilterChip(
                    selected = isSelected,
                    onClick = { filterType = tab },
                    label = {
                        Text(
                            text = tab,
                            color = if (isSelected) Color.White else GoalOnSurfaceVariant,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                        )
                    },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = GoalPrimary,
                        containerColor = GoalSurfaceVariant
                    ),
                    border = null,
                    shape = RoundedCornerShape(16.dp)
                )
            }
        }

        // Results List
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            if (searchResults.isEmpty()) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(40.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("No results match your search.", color = GoalOnSurfaceVariant)
                    }
                }
            } else {
                items(searchResults, key = { it.id }) { video ->
                    VideoCard(
                        video = video,
                        onClick = {
                            viewModel.recordView(video.id)
                            viewModel.navigateTo(
                                if (video.type == VideoType.SHORT) Screen.Shorts
                                else Screen.VideoDetail(video.id)
                            )
                        },
                        onLikeClick = { viewModel.toggleLike(video) },
                        onSaveClick = { viewModel.toggleSave(video) },
                        onShareClick = { viewModel.showToast("Link copied!") }
                    )
                }
            }
        }
    }
}
