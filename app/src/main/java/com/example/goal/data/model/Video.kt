package com.example.goal.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

enum class VideoType {
    STANDARD,
    SHORT
}

@Entity(tableName = "videos")
data class Video(
    @PrimaryKey val id: String,
    val userId: String,
    val username: String,
    val userAvatar: String = "",
    val title: String,
    val description: String,
    val type: VideoType,
    val category: String = "Gaming",
    val videoUrl: String = "",
    val thumbnailUrl: String = "",
    val viewsCount: Long = 0L,
    val likesCount: Long = 0L,
    val commentsCount: Long = 0L,
    val durationSeconds: Int = 60,
    val createdAt: Long = System.currentTimeMillis(),
    val isLiked: Boolean = false,
    val isSaved: Boolean = false,
    val isUserOwned: Boolean = false
)
