package com.example.goal.data.repository

import com.example.goal.data.local.GoalDao
import com.example.goal.data.model.Comment
import com.example.goal.data.model.Conversation
import com.example.goal.data.model.LiveChatMessage
import com.example.goal.data.model.LiveStream
import com.example.goal.data.model.Message
import com.example.goal.data.model.StreamStatus
import com.example.goal.data.model.Transaction
import com.example.goal.data.model.TransactionType
import com.example.goal.data.model.User
import com.example.goal.data.model.Video
import com.example.goal.data.model.VideoType
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.util.UUID

class GoalRepository(private val dao: GoalDao) {

    val allVideos: Flow<List<Video>> = dao.getAllVideos()
    val shortsVideos: Flow<List<Video>> = dao.getVideosByType(VideoType.SHORT)
    val standardVideos: Flow<List<Video>> = dao.getVideosByType(VideoType.STANDARD)
    val likedVideos: Flow<List<Video>> = dao.getLikedVideos()
    val savedVideos: Flow<List<Video>> = dao.getSavedVideos()
    val userOwnedVideos: Flow<List<Video>> = dao.getUserOwnedVideos()
    val currentUser: Flow<User?> = dao.getCurrentUser()
    val conversations: Flow<List<Conversation>> = dao.getAllConversations()
    val liveStreams: Flow<List<LiveStream>> = dao.getLiveStreams()
    val transactions: Flow<List<Transaction>> = dao.getAllTransactions()

    // Real-time live chat memory state for active live stream session
    private val _liveChatMessages = MutableStateFlow<List<LiveChatMessage>>(emptyList())
    val liveChatMessages = _liveChatMessages.asStateFlow()

    init {
        CoroutineScope(Dispatchers.IO).launch {
            seedInitialDataIfNeeded()
        }
    }

    private suspend fun seedInitialDataIfNeeded() {
        val existingVideos = dao.getAllVideos().first()
        if (existingVideos.isNotEmpty()) return

        // Seed Current User
        val defaultUser = User(
            id = "user_me",
            username = "alex_creator",
            displayName = "Alex Vance",
            email = "alex@goal.stream",
            avatarUrl = "",
            bio = "🎮 Pro Gamer & Content Creator | Streaming Daily at 7PM UTC | Building GOAL 🚀",
            isCreator = true,
            followersCount = 148200L,
            followingCount = 385L,
            totalLikesCount = 2840000L
        )
        dao.insertUser(defaultUser)

        // Seed Sample Videos (Standard Long-form)
        val initialVideos = listOf(
            Video(
                id = "vid_1",
                userId = "creator_1",
                username = "ApexLegendsOfficial",
                title = "GOAL Championship Finals 2026 - Top 10 Insane Clutch Moments!",
                description = "Watch the most electric highlights from the Season 4 GOAL Esports Championship. Incredible sniper trickshots and 1v3 outplays!",
                type = VideoType.STANDARD,
                category = "Esports",
                thumbnailUrl = "",
                viewsCount = 342900L,
                likesCount = 42800L,
                commentsCount = 1530L,
                durationSeconds = 874,
                createdAt = System.currentTimeMillis() - 86400000L
            ),
            Video(
                id = "vid_2",
                userId = "creator_2",
                username = "StrikerFootball",
                title = "Champions League Wonder Goals & Tactile Breakdown | Matchday Analysis",
                description = "Deep tactical analysis of last night's 90th-minute screamer and pressing systems in modern European football.",
                type = VideoType.STANDARD,
                category = "Football",
                thumbnailUrl = "",
                viewsCount = 189400L,
                likesCount = 28100L,
                commentsCount = 890L,
                durationSeconds = 642,
                createdAt = System.currentTimeMillis() - 172800000L
            ),
            Video(
                id = "vid_3",
                userId = "creator_3",
                username = "CyberTechReview",
                title = "Building The Ultimate 8K Creator Streaming Rig - Complete Guide!",
                description = "Dual PC streaming setup, optical capture cards, studio soundproofing, and FFmpeg hardware acceleration configurations.",
                type = VideoType.STANDARD,
                category = "Tech",
                thumbnailUrl = "",
                viewsCount = 95400L,
                likesCount = 12700L,
                commentsCount = 412L,
                durationSeconds = 1120,
                createdAt = System.currentTimeMillis() - 259200000L
            ),
            Video(
                id = "vid_4",
                userId = "creator_4",
                username = "BeatsAndVibes",
                title = "Deep Focus Synthwave & Lofi Mix [Chill Beats to Code/Game to]",
                description = "Original ambient production for long coding sessions and gaming marathons. Mixed live on GOAL Studio.",
                type = VideoType.STANDARD,
                category = "Music",
                thumbnailUrl = "",
                viewsCount = 520000L,
                likesCount = 74000L,
                commentsCount = 2100L,
                durationSeconds = 3600,
                createdAt = System.currentTimeMillis() - 345600000L
            ),
            // Seed Sample Shorts (Vertical Feed)
            Video(
                id = "short_1",
                userId = "short_creator_1",
                username = "goal_creator",
                title = "Building the GOAL platform live! 🚀🔥",
                description = "From zero to real-time video streaming with Kotlin & Jetpack Compose! #gaming #goal #flutter #android",
                type = VideoType.SHORT,
                category = "Tech",
                viewsCount = 89000L,
                likesCount = 12400L,
                commentsCount = 482L,
                durationSeconds = 38,
                createdAt = System.currentTimeMillis() - 3600000L
            ),
            Video(
                id = "short_2",
                userId = "short_creator_2",
                username = "SpeedyGamer",
                title = "Can you hit this rocket jump in 2026? 🎮🤯",
                description = "Frame-perfect rocket jump sequence in unreal engine 5 game! #gaming #shorts #speedrun",
                type = VideoType.SHORT,
                category = "Gaming",
                viewsCount = 145000L,
                likesCount = 29500L,
                commentsCount = 1120L,
                durationSeconds = 24,
                createdAt = System.currentTimeMillis() - 7200000L
            ),
            Video(
                id = "short_3",
                userId = "short_creator_3",
                username = "FreestyleFC",
                title = "World Record 360 Rainbow Flick in the Stadium! ⚽⚡",
                description = "Street football freestyle skills you need to learn today! #football #skills #goal",
                type = VideoType.SHORT,
                category = "Football",
                viewsCount = 210000L,
                likesCount = 48200L,
                commentsCount = 1940L,
                durationSeconds = 18,
                createdAt = System.currentTimeMillis() - 14400000L
            ),
            Video(
                id = "short_4",
                userId = "short_creator_4",
                username = "SynthMaster",
                title = "Drop of the year tested on arena monitors! 🔊🔥",
                description = "Bass test on 20,000 Watt sound system in the arena #music #producer #goal",
                type = VideoType.SHORT,
                category = "Music",
                viewsCount = 76000L,
                likesCount = 15300L,
                commentsCount = 630L,
                durationSeconds = 29,
                createdAt = System.currentTimeMillis() - 28800000L
            )
        )
        dao.insertVideos(initialVideos)

        // Seed Sample Comments for short_1
        val initialComments = listOf(
            Comment(
                id = "c_1",
                videoId = "short_1",
                userId = "u_1",
                username = "NeoGamer99",
                content = "This UI is super clean! Loving the dark theme and smooth performance!",
                likesCount = 84,
                createdAt = System.currentTimeMillis() - 1800000L
            ),
            Comment(
                id = "c_2",
                videoId = "short_1",
                userId = "u_2",
                username = "DevMaster",
                content = "Great architecture, room database and real-time live chat works amazingly.",
                likesCount = 41,
                createdAt = System.currentTimeMillis() - 900000L
            ),
            Comment(
                id = "c_3",
                videoId = "vid_1",
                userId = "u_3",
                username = "SniperKing",
                content = "That 1v3 clutch at 07:15 was completely unreal!!",
                likesCount = 156,
                createdAt = System.currentTimeMillis() - 40000000L
            )
        )
        for (c in initialComments) {
            dao.insertComment(c)
        }

        // Seed Live Streams
        val initialStreams = listOf(
            LiveStream(
                id = "live_1",
                userId = "streamer_1",
                username = "ShroudClone",
                userAvatar = "",
                title = "🔴 Ranked Grandmaster Push | 24H Marathon Stream [GOAL Live]",
                category = "Gaming",
                streamKey = "live_pk_8849201948",
                status = StreamStatus.LIVE,
                viewersCount = 18450L,
                thumbnailGradientId = 1
            ),
            LiveStream(
                id = "live_2",
                userId = "streamer_2",
                username = "ElClasicoHub",
                userAvatar = "",
                title = "⚽ LIVE WATCHALONG: Derby Final Highlights & Fan Reactions",
                category = "Football",
                streamKey = "live_pk_4482910395",
                status = StreamStatus.LIVE,
                viewersCount = 32800L,
                thumbnailGradientId = 2
            ),
            LiveStream(
                id = "live_3",
                userId = "streamer_3",
                username = "TechLiveLab",
                userAvatar = "",
                title = "⚡ Live Coding FFmpeg Video Transcoder & WebSocket Chat Server",
                category = "Tech",
                streamKey = "live_pk_1239857211",
                status = StreamStatus.LIVE,
                viewersCount = 4210L,
                thumbnailGradientId = 3
            )
        )
        dao.insertLiveStreams(initialStreams)

        // Seed Initial Live Chat for live_1
        _liveChatMessages.value = listOf(
            LiveChatMessage("lc_1", "live_1", "Alex_Vance", "", "Welcome everyone to GOAL Live!"),
            LiveChatMessage("lc_2", "live_1", "PixelHunter", "", "What a sniper shot! 🎯"),
            LiveChatMessage("lc_3", "live_1", "StreamFan99", "", "Hyped for this match! 🔥"),
            LiveChatMessage("lc_4", "live_1", "GoldMember", "", "Sent 100 GOAL Coins! 💰", isSuperChat = true, superChatAmount = 5.0)
        )

        // Seed Conversations
        val convos = listOf(
            Conversation(
                id = "conv_1",
                otherUserId = "user_sarah",
                otherUsername = "SarahStreamer",
                otherAvatar = "",
                lastMessage = "Hey Alex! Collab stream this Friday on GOAL?",
                lastTimestamp = System.currentTimeMillis() - 1200000L,
                unreadCount = 1,
                isOnline = true
            ),
            Conversation(
                id = "conv_2",
                otherUserId = "user_marcus",
                otherUsername = "MarcusDev",
                otherAvatar = "",
                lastMessage = "Your new Short went viral! +25K views already!",
                lastTimestamp = System.currentTimeMillis() - 7200000L,
                unreadCount = 0,
                isOnline = false
            ),
            Conversation(
                id = "conv_3",
                otherUserId = "user_esports",
                otherUsername = "GOAL Support",
                otherAvatar = "",
                lastMessage = "Creator payout of $450.00 was successfully processed.",
                lastTimestamp = System.currentTimeMillis() - 86400000L,
                unreadCount = 0,
                isOnline = true
            )
        )
        dao.insertConversations(convos)

        // Seed Messages for conv_1
        dao.insertMessage(
            Message(
                id = "m_1",
                conversationId = "conv_1",
                senderId = "user_sarah",
                senderName = "SarahStreamer",
                content = "Hey Alex! Loved your latest tournament analysis video!",
                createdAt = System.currentTimeMillis() - 3600000L,
                isFromMe = false
            )
        )
        dao.insertMessage(
            Message(
                id = "m_2",
                conversationId = "conv_1",
                senderId = "user_me",
                senderName = "Alex Vance",
                content = "Thanks Sarah! Really appreciate it. How is your channel growing?",
                createdAt = System.currentTimeMillis() - 2400000L,
                isFromMe = true
            )
        )
        dao.insertMessage(
            Message(
                id = "m_3",
                conversationId = "conv_1",
                senderId = "user_sarah",
                senderName = "SarahStreamer",
                content = "Hey Alex! Collab stream this Friday on GOAL?",
                createdAt = System.currentTimeMillis() - 1200000L,
                isFromMe = false
            )
        )

        // Seed Transactions
        val initialTx = listOf(
            Transaction("tx_1", "user_me", 450.00, TransactionType.PAYOUT, "Completed", "Monthly Creator Payout - Bank Wire", System.currentTimeMillis() - 86400000L),
            Transaction("tx_2", "user_me", 125.50, TransactionType.SUBSCRIPTION, "Completed", "GOAL Tier 2 Subscriptions (25 users)", System.currentTimeMillis() - 172800000L),
            Transaction("tx_3", "user_me", 64.00, TransactionType.TIP, "Completed", "Superchat & Live Tips during Marathon", System.currentTimeMillis() - 259200000L),
            Transaction("tx_4", "user_me", 850.20, TransactionType.AD_REVENUE, "Completed", "Video Monetization & Shorts Revenue Share", System.currentTimeMillis() - 400000000L)
        )
        dao.insertTransactions(initialTx)
    }

    suspend fun toggleVideoLike(video: Video) {
        val newLiked = !video.isLiked
        val delta = if (newLiked) 1 else -1
        dao.toggleVideoLike(video.id, newLiked, delta)
    }

    suspend fun toggleVideoSave(video: Video) {
        dao.toggleVideoSave(video.id, !video.isSaved)
    }

    suspend fun incrementViewCount(videoId: String) {
        dao.incrementViews(videoId)
    }

    suspend fun deleteVideo(videoId: String) {
        dao.deleteVideo(videoId)
    }

    fun getCommentsForVideo(videoId: String): Flow<List<Comment>> {
        return dao.getCommentsForVideo(videoId)
    }

    suspend fun addComment(videoId: String, content: String, username: String) {
        val comment = Comment(
            id = "c_${UUID.randomUUID()}",
            videoId = videoId,
            userId = "user_me",
            username = username,
            content = content,
            createdAt = System.currentTimeMillis(),
            likesCount = 0
        )
        dao.insertComment(comment)
    }

    suspend fun toggleCommentLike(comment: Comment) {
        val newLiked = !comment.isLiked
        val delta = if (newLiked) 1 else -1
        dao.toggleCommentLike(comment.id, newLiked, delta)
    }

    suspend fun createVideo(
        title: String,
        description: String,
        type: VideoType,
        category: String,
        durationSeconds: Int = 45
    ): Video {
        val newVideo = Video(
            id = "vid_${UUID.randomUUID().toString().take(8)}",
            userId = "user_me",
            username = "alex_creator",
            title = title,
            description = description,
            type = type,
            category = category,
            viewsCount = 1L,
            likesCount = 0L,
            commentsCount = 0L,
            durationSeconds = durationSeconds,
            createdAt = System.currentTimeMillis(),
            isUserOwned = true
        )
        dao.insertVideo(newVideo)
        return newVideo
    }

    fun getMessagesForConversation(convId: String): Flow<List<Message>> {
        return dao.getMessagesForConversation(convId)
    }

    suspend fun sendMessage(conversationId: String, content: String, recipientName: String) {
        val myMessage = Message(
            id = "m_${UUID.randomUUID()}",
            conversationId = conversationId,
            senderId = "user_me",
            senderName = "Alex Vance",
            content = content,
            createdAt = System.currentTimeMillis(),
            isFromMe = true
        )
        dao.insertMessage(myMessage)

        // Update conversation last message
        val updatedConv = Conversation(
            id = conversationId,
            otherUserId = recipientName.lowercase(),
            otherUsername = recipientName,
            otherAvatar = "",
            lastMessage = content,
            lastTimestamp = System.currentTimeMillis(),
            unreadCount = 0,
            isOnline = true
        )
        dao.insertConversation(updatedConv)
    }

    fun sendLiveChatMessage(streamId: String, text: String, isSuperChat: Boolean = false, amount: Double = 0.0) {
        val newMsg = LiveChatMessage(
            id = "lc_${UUID.randomUUID().toString().take(6)}",
            streamId = streamId,
            username = "Alex Vance",
            avatarUrl = "",
            message = text,
            isSuperChat = isSuperChat,
            superChatAmount = amount,
            timestamp = System.currentTimeMillis()
        )
        _liveChatMessages.value = _liveChatMessages.value + newMsg
    }

    suspend fun startNewLiveStream(title: String, category: String): LiveStream {
        val stream = LiveStream(
            id = "live_${UUID.randomUUID().toString().take(8)}",
            userId = "user_me",
            username = "alex_creator",
            userAvatar = "",
            title = title,
            category = category,
            streamKey = "live_pk_${System.currentTimeMillis().toString().takeLast(8)}",
            status = StreamStatus.LIVE,
            viewersCount = 1L,
            thumbnailGradientId = (1..3).random()
        )
        dao.insertLiveStream(stream)
        return stream
    }

    suspend fun requestPayout(amount: Double): Transaction {
        val tx = Transaction(
            id = "tx_${UUID.randomUUID().toString().take(8)}",
            userId = "user_me",
            amount = amount,
            type = TransactionType.PAYOUT,
            status = "Processing",
            title = "Creator Bank Withdrawal Request",
            createdAt = System.currentTimeMillis()
        )
        dao.insertTransaction(tx)
        return tx
    }
}
