package com.example.goal.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChatBubbleOutline
import androidx.compose.material.icons.filled.NotificationsNone
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.goal.ui.theme.GoalBackground
import com.example.goal.ui.theme.GoalPrimary
import com.example.goal.ui.theme.GoalSecondary

@Composable
fun GoalTopAppBar(
    unreadMessageCount: Int = 1,
    onSearchClick: () -> Unit,
    onMessagesClick: () -> Unit,
    onNotificationsClick: () -> Unit,
    onLogoClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(GoalBackground)
            .padding(horizontal = 16.dp, vertical = 12.dp)
            .testTag("goal_top_app_bar"),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // GOAL Brand Logo & Title
        Row(
            modifier = Modifier
                .clickable { onLogoClick() }
                .testTag("app_logo_button"),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(
                        Brush.linearGradient(
                            listOf(GoalPrimary, GoalSecondary)
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.PlayArrow,
                    contentDescription = "GOAL Play",
                    tint = Color.White,
                    modifier = Modifier.size(22.dp)
                )
            }
            Spacer(modifier = Modifier.width(10.dp))
            Text(
                text = "GOAL",
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Black,
                    letterSpacing = 2.sp
                ),
                color = Color.White
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        // Search Action
        IconButton(
            onClick = onSearchClick,
            modifier = Modifier.testTag("search_icon_button")
        ) {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "Search videos and creators",
                tint = Color.White
            )
        }

        // Direct Messages Action with Badge
        IconButton(
            onClick = onMessagesClick,
            modifier = Modifier.testTag("messages_icon_button")
        ) {
            BadgedBox(
                badge = {
                    if (unreadMessageCount > 0) {
                        Badge(
                            containerColor = GoalPrimary,
                            contentColor = Color.White
                        ) {
                            Text(text = unreadMessageCount.toString())
                        }
                    }
                }
            ) {
                Icon(
                    imageVector = Icons.Default.ChatBubbleOutline,
                    contentDescription = "Direct Messages",
                    tint = Color.White
                )
            }
        }

        // Notifications Action
        IconButton(
            onClick = onNotificationsClick,
            modifier = Modifier.testTag("notifications_icon_button")
        ) {
            Icon(
                imageVector = Icons.Default.NotificationsNone,
                contentDescription = "Notifications",
                tint = Color.White
            )
        }
    }
}
