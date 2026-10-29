package com.example.goal.ui.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.Analytics
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.VideoLibrary
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import com.example.goal.ui.GoalViewModel
import com.example.goal.ui.Screen
import com.example.goal.ui.components.VideoCard
import com.example.goal.ui.components.formatViews
import com.example.goal.ui.theme.GoalBackground
import com.example.goal.ui.theme.GoalOnSurfaceVariant
import com.example.goal.ui.theme.GoalPrimary
import com.example.goal.ui.theme.GoalSecondary
import com.example.goal.ui.theme.GoalSurface
import com.example.goal.ui.theme.GoalSurfaceVariant

@Composable
fun ProfileScreen(
    viewModel: GoalViewModel,
    modifier: Modifier = Modifier
) {
    val user by viewModel.currentUser.collectAsStateWithLifecycle()
    val allVideos by viewModel.allVideos.collectAsStateWithLifecycle()
    val likedVideos by viewModel.likedVideos.collectAsStateWithLifecycle()
    val savedVideos by viewModel.savedVideos.collectAsStateWithLifecycle()

    var selectedTabIndex by remember { mutableIntStateOf(0) }
    val tabTitles = listOf("Videos", "Shorts", "Saved", "Liked")

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(GoalBackground)
            .testTag("profile_screen")
    ) {
        // Hero Banner
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(130.dp)
            ) {
                Image(
                    painter = painterResource(id = com.example.R.drawable.goal_banner),
                    contentDescription = "Profile Banner",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                listOf(Color.Transparent, GoalBackground)
                            )
                        )
                )
            }
        }

        // Profile Avatar & Information
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {
                // Avatar with verified badge
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .offset(y = (-40).dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Bottom
                ) {
                    Box(
                        modifier = Modifier
                            .size(80.dp)
                            .clip(CircleShape)
                            .border(3.dp, GoalBackground, CircleShape)
                            .background(
                                Brush.linearGradient(listOf(GoalPrimary, GoalSecondary))
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "AV",
                            color = Color.White,
                            fontWeight = FontWeight.Black,
                            fontSize = 28.sp
                        )
                    }

                    // Action Buttons: Creator Studio & Wallet
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.padding(bottom = 6.dp)
                    ) {
                        Button(
                            onClick = { viewModel.navigateTo(Screen.CreatorStudio) },
                            colors = ButtonDefaults.buttonColors(containerColor = GoalPrimary),
                            shape = RoundedCornerShape(18.dp),
                            contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                            modifier = Modifier.testTag("open_creator_studio_button")
                        ) {
                            Icon(Icons.Default.Analytics, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Studio", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }

                        Button(
                            onClick = { viewModel.navigateTo(Screen.Wallet) },
                            colors = ButtonDefaults.buttonColors(containerColor = GoalSurfaceVariant),
                            shape = RoundedCornerShape(18.dp),
                            contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                            modifier = Modifier.testTag("open_wallet_button")
                        ) {
                            Icon(Icons.Default.AccountBalanceWallet, contentDescription = null, tint = Color(0xFFFF9500), modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Wallet", fontSize = 12.sp, color = Color.White, fontWeight = FontWeight.Bold)
                        }
                    }
                }

                // Name & Verified Badge
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.offset(y = (-30).dp)
                ) {
                    Text(
                        text = user?.displayName ?: "Alex Vance",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = "Verified Creator",
                        tint = GoalPrimary,
                        modifier = Modifier.size(18.dp)
                    )
                }

                Text(
                    text = "@${user?.username ?: "alex_creator"}",
                    color = GoalOnSurfaceVariant,
                    fontSize = 13.sp,
                    modifier = Modifier.offset(y = (-28).dp)
                )

                // Bio
                Text(
                    text = user?.bio ?: "Pro Gamer & Creator | Streaming Daily at 7PM UTC | Building GOAL 🚀",
                    color = Color(0xFFE2E8F0),
                    fontSize = 13.sp,
                    lineHeight = 18.sp,
                    modifier = Modifier.offset(y = (-22).dp)
                )

                // Followers / Stats Counter Row
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .offset(y = (-14).dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(GoalSurface)
                        .padding(vertical = 12.dp),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    ProfileStatItem("Followers", formatViews(user?.followersCount ?: 148200L).replace(" views", ""))
                    ProfileStatItem("Following", (user?.followingCount ?: 385L).toString())
                    ProfileStatItem("Total Likes", formatViews(user?.totalLikesCount ?: 2800000L).replace(" views", ""))
                }
            }
        }

        // Tabs Row (Videos, Shorts, Saved, Liked)
        item {
            TabRow(
                selectedTabIndex = selectedTabIndex,
                containerColor = GoalBackground,
                contentColor = Color.White,
                indicator = { tabPositions ->
                    TabRowDefaults.SecondaryIndicator(
                        Modifier.tabIndicatorOffset(tabPositions[selectedTabIndex]),
                        color = GoalPrimary
                    )
                },
                modifier = Modifier.padding(horizontal = 16.dp)
            ) {
                tabTitles.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTabIndex == index,
                        onClick = { selectedTabIndex = index },
                        text = {
                            Text(
                                text = title,
                                fontWeight = if (selectedTabIndex == index) FontWeight.Bold else FontWeight.Normal,
                                color = if (selectedTabIndex == index) Color.White else GoalOnSurfaceVariant
                            )
                        }
                    )
                }
            }
            Spacer(modifier = Modifier.height(14.dp))
        }

        // Tab Content
        val displayedVideos = when (selectedTabIndex) {
            0 -> allVideos.filter { it.type == com.example.goal.data.model.VideoType.STANDARD }
            1 -> allVideos.filter { it.type == com.example.goal.data.model.VideoType.SHORT }
            2 -> savedVideos
            3 -> likedVideos
            else -> allVideos
        }

        if (displayedVideos.isEmpty()) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(40.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text("No videos found in this tab.", color = GoalOnSurfaceVariant)
                }
            }
        } else {
            items(displayedVideos, key = { it.id }) { video ->
                Box(modifier = Modifier.padding(horizontal = 16.dp)) {
                    VideoCard(
                        video = video,
                        onClick = {
                            viewModel.recordView(video.id)
                            viewModel.navigateTo(Screen.VideoDetail(video.id))
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

@Composable
fun ProfileStatItem(label: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = value,
            color = Color.White,
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = label,
            color = GoalOnSurfaceVariant,
            fontSize = 11.sp
        )
    }
}
