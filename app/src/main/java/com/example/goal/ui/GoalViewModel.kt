package com.example.goal.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.goal.data.model.Comment
import com.example.goal.data.model.Conversation
import com.example.goal.data.model.LiveChatMessage
import com.example.goal.data.model.LiveStream
import com.example.goal.data.model.Message
import com.example.goal.data.model.Transaction
import com.example.goal.data.model.User
import com.example.goal.data.model.Video
import com.example.goal.data.model.VideoType
import com.example.goal.data.repository.GoalRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

sealed class Screen {
    object Home : Screen()
    object Shorts : Screen()
    object Create : Screen()
    object Live : Screen()
    object Profile : Screen()
    object CreatorStudio : Screen()
    object Wallet : Screen()
    object Messages : Screen()
    data class ChatDetail(val conversationId: String, val recipientName: String) : Screen()
    data class VideoDetail(val videoId: String) : Screen()
    object Search : Screen()
    data class LiveWatch(val streamId: String) : Screen()
    object LiveBroadcast : Screen()
}

class GoalViewModel(private val repository: GoalRepository) : ViewModel() {

    // Navigation back stack
    private val _currentScreen = MutableStateFlow<Screen>(Screen.Home)
    val currentScreen: StateFlow<Screen> = _currentScreen.asStateFlow()

    private val screenStack = mutableListOf<Screen>(Screen.Home)

    fun navigateTo(screen: Screen) {
        if (_currentScreen.value != screen) {
            screenStack.add(screen)
            _currentScreen.value = screen
        }
    }

    fun navigateBack(): Boolean {
        if (screenStack.size > 1) {
            screenStack.removeAt(screenStack.size - 1)
            _currentScreen.value = screenStack.last()
            return true
        }
        return false
    }

    // Category filter for Home Feed
    private val _selectedCategory = MutableStateFlow("All")
    val selectedCategory: StateFlow<String> = _selectedCategory.asStateFlow()

    fun selectCategory(category: String) {
        _selectedCategory.value = category
    }

    // Search query
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }

    // Data streams
    val allVideos: StateFlow<List<Video>> = repository.allVideos
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val filteredHomeVideos: StateFlow<List<Video>> = combine(
        repository.standardVideos,
        _selectedCategory
    ) { videos, category ->
        if (category == "All") videos else videos.filter { it.category.equals(category, ignoreCase = true) }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val shortsList: StateFlow<List<Video>> = repository.shortsVideos
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val liveStreams: StateFlow<List<LiveStream>> = repository.liveStreams
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val currentUser: StateFlow<User?> = repository.currentUser
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    val conversations: StateFlow<List<Conversation>> = repository.conversations
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val transactions: StateFlow<List<Transaction>> = repository.transactions
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val userOwnedVideos: StateFlow<List<Video>> = repository.userOwnedVideos
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val likedVideos: StateFlow<List<Video>> = repository.likedVideos
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val savedVideos: StateFlow<List<Video>> = repository.savedVideos
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Active Live Chat
    val liveChatMessages: StateFlow<List<LiveChatMessage>> = repository.liveChatMessages

    // UI Snackbars/alerts
    private val _snackbarMessage = MutableStateFlow<String?>(null)
    val snackbarMessage: StateFlow<String?> = _snackbarMessage.asStateFlow()

    fun clearSnackbar() {
        _snackbarMessage.value = null
    }

    fun showToast(msg: String) {
        _snackbarMessage.value = msg
    }

    // Video Interactions
    fun toggleLike(video: Video) {
        viewModelScope.launch {
            repository.toggleVideoLike(video)
        }
    }

    fun toggleSave(video: Video) {
        viewModelScope.launch {
            repository.toggleVideoSave(video)
            showToast(if (!video.isSaved) "Added to Saved Videos" else "Removed from Saved")
        }
    }

    fun recordView(videoId: String) {
        viewModelScope.launch {
            repository.incrementViewCount(videoId)
        }
    }

    fun deleteVideo(videoId: String) {
        viewModelScope.launch {
            repository.deleteVideo(videoId)
            showToast("Video deleted")
        }
    }

    fun getCommentsForVideo(videoId: String): StateFlow<List<Comment>> {
        return repository.getCommentsForVideo(videoId)
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    }

    fun addComment(videoId: String, content: String) {
        if (content.isBlank()) return
        viewModelScope.launch {
            repository.addComment(videoId, content, "Alex Vance")
            showToast("Comment posted!")
        }
    }

    fun toggleCommentLike(comment: Comment) {
        viewModelScope.launch {
            repository.toggleCommentLike(comment)
        }
    }

    // Direct Messages
    fun getMessagesForConversation(convId: String): StateFlow<List<Message>> {
        return repository.getMessagesForConversation(convId)
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    }

    fun sendMessage(conversationId: String, content: String, recipientName: String) {
        if (content.isBlank()) return
        viewModelScope.launch {
            repository.sendMessage(conversationId, content, recipientName)
            // Simulate real-time automated response
            delay(1500)
            repository.sendMessage(
                conversationId = conversationId,
                content = "Got your message! Let's definitely set up the GOAL stream session 🙌",
                recipientName = recipientName
            )
        }
    }

    // Live Streaming
    fun sendLiveMessage(streamId: String, text: String, isSuperChat: Boolean = false, amount: Double = 0.0) {
        if (text.isBlank()) return
        repository.sendLiveChatMessage(streamId, text, isSuperChat, amount)
    }

    fun startBroadcast(title: String, category: String) {
        viewModelScope.launch {
            val stream = repository.startNewLiveStream(title, category)
            showToast("You are now LIVE on GOAL!")
            navigateTo(Screen.LiveWatch(stream.id))
        }
    }

    // Creator Creation Studio flow
    private val _isUploading = MutableStateFlow(false)
    val isUploading: StateFlow<Boolean> = _isUploading.asStateFlow()

    private val _transcodingProgress = MutableStateFlow(0f)
    val transcodingProgress: StateFlow<Float> = _transcodingProgress.asStateFlow()

    private val _currentTranscodeResolution = MutableStateFlow("Initializing...")
    val currentTranscodeResolution: StateFlow<String> = _currentTranscodeResolution.asStateFlow()

    fun uploadAndPublishVideo(
        title: String,
        description: String,
        type: VideoType,
        category: String
    ) {
        viewModelScope.launch {
            _isUploading.value = true
            // Simulate FFmpeg transcode pipeline step by step: 360p -> 480p -> 720p -> 1080p
            val steps = listOf(
                "FFmpeg: Ingesting source video..." to 0.15f,
                "FFmpeg: Transcoding 360p stream (800 kbps)..." to 0.35f,
                "FFmpeg: Transcoding 480p stream (1400 kbps)..." to 0.55f,
                "FFmpeg: Transcoding 720p stream (2800 kbps)..." to 0.75f,
                "FFmpeg: Transcoding 1080p stream (5000 kbps)..." to 0.90f,
                "Generating thumbnail & uploading to CDN..." to 1.0f
            )

            for ((stepName, progress) in steps) {
                _currentTranscodeResolution.value = stepName
                _transcodingProgress.value = progress
                delay(600)
            }

            repository.createVideo(title, description, type, category)
            _isUploading.value = false
            _transcodingProgress.value = 0f
            _currentTranscodeResolution.value = ""
            showToast("Successfully published to GOAL!")
            navigateTo(if (type == VideoType.SHORT) Screen.Shorts else Screen.Home)
        }
    }

    // Wallet Payout
    fun requestPayout(amount: Double) {
        viewModelScope.launch {
            repository.requestPayout(amount)
            showToast("Payout request of $${String.format("%.2f", amount)} submitted!")
        }
    }
}
