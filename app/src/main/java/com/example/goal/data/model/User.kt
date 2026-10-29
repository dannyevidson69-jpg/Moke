package com.example.goal.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class User(
    @PrimaryKey val id: String,
    val username: String,
    val displayName: String,
    val email: String,
    val avatarUrl: String,
    val bio: String,
    val isCreator: Boolean = true,
    val followersCount: Long = 128450L,
    val followingCount: Long = 420L,
    val totalLikesCount: Long = 1250000L
)
