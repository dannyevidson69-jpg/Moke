package com.example.goal.ui.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LiveTv
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.MainActivity
import com.example.goal.ui.GoalViewModel
import com.example.goal.ui.Screen
import com.example.goal.ui.components.VideoCard
import com.example.goal.ui.theme.GoalBackground
import com.example.goal.ui.theme.GoalLiveRed
import com.example.goal.ui.theme.GoalPrimary
import com.example.goal.ui.theme.GoalSecondary
import com.example.goal.ui.theme.GoalSurfaceVariant

@Composable
fun HomeScreen(
    viewModel: GoalViewModel,
    modifier: Modifier = Modifier
) {
    val videos by viewModel.filteredHomeVideos.collectAsStateWithLifecycle()
    val selectedCategory by viewModel.selectedCategory.collectAsStateWithLifecycle()

    val categories = listOf("All", "Gaming", "Football", "Esports", "Tech", "Music")

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(GoalBackground)
            .padding(horizontal = 16.dp)
            .testTag("home_feed_list"),
        contentPadding = PaddingValues(top = 8.dp, bottom = 24.dp)
    ) {
        // Horizontal Filter Chips
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState())
                    .padding(bottom = 14.dp)
                    .testTag("category_filter_row"),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                categories.forEach { category ->
                    val isSelected = selectedCategory == category
                    FilterChip(
                        selected = isSelected,
                        onClick = { viewModel.selectCategory(category) },
                        label = {
                            Text(
                                text = category,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                color = if (isSelected) Color.White else Color(0xFFCBD5E1)
                            )
                        },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = GoalPrimary,
                            containerColor = GoalSurfaceVariant
                        ),
                        border = null,
                        shape = RoundedCornerShape(18.dp),
                        modifier = Modifier.testTag("filter_chip_$category")
                    )
                }
            }
        }

        // Hero Spotlight Banner
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(16f / 8f)
                    .clip(RoundedCornerShape(16.dp))
                    .clickable { viewModel.navigateTo(Screen.LiveWatch("live_1")) }
                    .padding(bottom = 16.dp)
                    .testTag("hero_spotlight_banner")
            ) {
                // Banner background - use generated goal_banner drawable
                Image(
                    painter = painterResource(id = com.example.R.drawable.goal_banner),
                    contentDescription = "GOAL Championship Finals Banner",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )

                // Dark gradient overlay
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                listOf(Color.Transparent, Color.Black.copy(alpha = 0.85f))
                            )
                        )
                )

                // Live Badge top right
                Row(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(12.dp)
                        .clip(RoundedCornerShape(6.dp))
                        .background(GoalLiveRed)
                        .padding(horizontal = 8.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(6.dp)
                            .clip(CircleShape)
                            .background(Color.White)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "LIVE NOW",
                        color = Color.White,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                // Banner Details bottom left
                Column(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(16.dp)
                ) {
                    Text(
                        text = "GOAL Championship Finals 2026",
                        color = Color.White,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "18.4K viewers • ShroudClone vs CyberSquad",
                        color = Color(0xFFE2E8F0),
                        fontSize = 12.sp
                    )
                }
            }
        }

        // Long-form Video Feed
        items(videos, key = { it.id }) { video ->
            VideoCard(
                video = video,
                onClick = {
                    viewModel.recordView(video.id)
                    viewModel.navigateTo(Screen.VideoDetail(video.id))
                },
                onLikeClick = { viewModel.toggleLike(video) },
                onSaveClick = { viewModel.toggleSave(video) },
                onShareClick = { viewModel.showToast("Link copied to clipboard: goal.stream/v/${video.id}") }
            )
        }
    }
}
