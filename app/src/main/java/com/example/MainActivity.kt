package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.goal.data.local.GoalDatabase
import com.example.goal.data.repository.GoalRepository
import com.example.goal.ui.GoalViewModel
import com.example.goal.ui.Screen
import com.example.goal.ui.components.GoalBottomNavigation
import com.example.goal.ui.components.GoalTopAppBar
import com.example.goal.ui.create.CreateScreen
import com.example.goal.ui.home.HomeScreen
import com.example.goal.ui.live.LiveBroadcastScreen
import com.example.goal.ui.live.LiveScreen
import com.example.goal.ui.live.LiveWatchScreen
import com.example.goal.ui.messages.ChatDetailScreen
import com.example.goal.ui.messages.MessagesScreen
import com.example.goal.ui.player.VideoDetailScreen
import com.example.goal.ui.profile.ProfileScreen
import com.example.goal.ui.search.SearchScreen
import com.example.goal.ui.shorts.ShortsScreen
import com.example.goal.ui.studio.CreatorStudioScreen
import com.example.goal.ui.theme.GoalBackground
import com.example.goal.ui.theme.GoalTheme
import com.example.goal.ui.wallet.WalletScreen

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val database = GoalDatabase.getDatabase(this)
        val repository = GoalRepository(database.goalDao())

        val viewModelFactory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                if (modelClass.isAssignableFrom(GoalViewModel::class.java)) {
                    @Suppress("UNCHECKED_CAST")
                    return GoalViewModel(repository) as T
                }
                throw IllegalArgumentException("Unknown ViewModel class")
            }
        }

        setContent {
            GoalTheme {
                val goalViewModel: GoalViewModel = viewModel(factory = viewModelFactory)
                GoalApp(viewModel = goalViewModel)
            }
        }
    }
}

@Composable
fun GoalApp(viewModel: GoalViewModel) {
    val currentScreen by viewModel.currentScreen.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    val snackbarMsg by viewModel.snackbarMessage.collectAsStateWithLifecycle()

    LaunchedEffect(snackbarMsg) {
        snackbarMsg?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.clearSnackbar()
        }
    }

    // Determine if TopAppBar and BottomNavigation should be shown
    val showTopAppBar = currentScreen is Screen.Home ||
            currentScreen is Screen.Live ||
            currentScreen is Screen.Profile

    val showBottomNav = currentScreen is Screen.Home ||
            currentScreen is Screen.Shorts ||
            currentScreen is Screen.Create ||
            currentScreen is Screen.Live ||
            currentScreen is Screen.Profile

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(GoalBackground),
        contentWindowInsets = WindowInsets.safeDrawing,
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        topBar = {
            if (showTopAppBar) {
                GoalTopAppBar(
                    unreadMessageCount = 1,
                    onSearchClick = { viewModel.navigateTo(Screen.Search) },
                    onMessagesClick = { viewModel.navigateTo(Screen.Messages) },
                    onNotificationsClick = { viewModel.showToast("Notifications: New upload from ShroudClone!") },
                    onLogoClick = { viewModel.navigateTo(Screen.Home) }
                )
            }
        },
        bottomBar = {
            if (showBottomNav) {
                GoalBottomNavigation(
                    currentScreen = currentScreen,
                    onNavigate = { screen -> viewModel.navigateTo(screen) }
                )
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(GoalBackground)
        ) {
            when (val screen = currentScreen) {
                is Screen.Home -> HomeScreen(viewModel = viewModel)
                is Screen.Shorts -> ShortsScreen(viewModel = viewModel)
                is Screen.Create -> CreateScreen(viewModel = viewModel)
                is Screen.Live -> LiveScreen(viewModel = viewModel)
                is Screen.Profile -> ProfileScreen(viewModel = viewModel)
                is Screen.CreatorStudio -> CreatorStudioScreen(
                    viewModel = viewModel,
                    onBack = { viewModel.navigateBack() }
                )
                is Screen.Wallet -> WalletScreen(
                    viewModel = viewModel,
                    onBack = { viewModel.navigateBack() }
                )
                is Screen.Messages -> MessagesScreen(
                    viewModel = viewModel,
                    onBack = { viewModel.navigateBack() }
                )
                is Screen.ChatDetail -> ChatDetailScreen(
                    conversationId = screen.conversationId,
                    recipientName = screen.recipientName,
                    viewModel = viewModel,
                    onBack = { viewModel.navigateBack() }
                )
                is Screen.VideoDetail -> VideoDetailScreen(
                    videoId = screen.videoId,
                    viewModel = viewModel,
                    onBack = { viewModel.navigateBack() }
                )
                is Screen.Search -> SearchScreen(
                    viewModel = viewModel,
                    onBack = { viewModel.navigateBack() }
                )
                is Screen.LiveWatch -> LiveWatchScreen(
                    streamId = screen.streamId,
                    viewModel = viewModel,
                    onBack = { viewModel.navigateBack() }
                )
                is Screen.LiveBroadcast -> LiveBroadcastScreen(
                    viewModel = viewModel,
                    onBack = { viewModel.navigateBack() }
                )
            }
        }
    }
}
