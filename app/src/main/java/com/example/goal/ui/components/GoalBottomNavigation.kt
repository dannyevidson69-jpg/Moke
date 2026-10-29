package com.example.goal.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LiveTv
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PlayCircleOutline
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.LiveTv
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.PlayCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.goal.ui.Screen
import com.example.goal.ui.theme.GoalBackground
import com.example.goal.ui.theme.GoalLiveRed
import com.example.goal.ui.theme.GoalOnSurfaceVariant
import com.example.goal.ui.theme.GoalPrimary
import com.example.goal.ui.theme.GoalSecondary

@Composable
fun GoalBottomNavigation(
    currentScreen: Screen,
    onNavigate: (Screen) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(GoalBackground)
            .navigationBarsPadding()
            .height(64.dp)
            .padding(horizontal = 8.dp)
            .testTag("bottom_nav_bar"),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        // Home Tab
        BottomNavItem(
            label = "Home",
            icon = if (currentScreen is Screen.Home) Icons.Filled.Home else Icons.Outlined.Home,
            isSelected = currentScreen is Screen.Home,
            onClick = { onNavigate(Screen.Home) },
            testTag = "nav_home_button"
        )

        // Shorts Tab
        BottomNavItem(
            label = "Shorts",
            icon = if (currentScreen is Screen.Shorts) Icons.Filled.PlayCircleOutline else Icons.Outlined.PlayCircle,
            isSelected = currentScreen is Screen.Shorts,
            onClick = { onNavigate(Screen.Shorts) },
            testTag = "nav_shorts_button"
        )

        // Create Center Button (+)
        Box(
            modifier = Modifier
                .size(46.dp)
                .clip(CircleShape)
                .background(
                    Brush.linearGradient(listOf(GoalPrimary, GoalSecondary))
                )
                .clickable { onNavigate(Screen.Create) }
                .testTag("nav_create_button"),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Create Video or Short",
                tint = Color.White,
                modifier = Modifier.size(28.dp)
            )
        }

        // Live Tab
        BottomNavItem(
            label = "Live",
            icon = if (currentScreen is Screen.Live) Icons.Filled.LiveTv else Icons.Outlined.LiveTv,
            isSelected = currentScreen is Screen.Live,
            badgeColor = GoalLiveRed,
            onClick = { onNavigate(Screen.Live) },
            testTag = "nav_live_button"
        )

        // Profile Tab
        BottomNavItem(
            label = "Profile",
            icon = if (currentScreen is Screen.Profile) Icons.Filled.Person else Icons.Outlined.Person,
            isSelected = currentScreen is Screen.Profile,
            onClick = { onNavigate(Screen.Profile) },
            testTag = "nav_profile_button"
        )
    }
}

@Composable
private fun BottomNavItem(
    label: String,
    icon: ImageVector,
    isSelected: Boolean,
    badgeColor: Color? = null,
    onClick: () -> Unit,
    testTag: String
) {
    Column(
        modifier = Modifier
            .clickable(onClick = onClick)
            .padding(horizontal = 12.dp, vertical = 6.dp)
            .testTag(testTag),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = if (isSelected) Color.White else GoalOnSurfaceVariant,
                modifier = Modifier.size(24.dp)
            )
            if (badgeColor != null) {
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .clip(CircleShape)
                        .background(badgeColor)
                        .align(Alignment.TopEnd)
                )
            }
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = label,
            fontSize = 11.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
            color = if (isSelected) Color.White else GoalOnSurfaceVariant
        )
    }
}
