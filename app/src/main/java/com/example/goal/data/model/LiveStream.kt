package com.example.goal.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

enum class StreamStatus {
    LIVE,
    SCHEDULED,
    ENDED
}

@Entity(tableName = "live_streams")
data class LiveStream(
    @PrimaryKey val id: String,
    val userId: String,
    val username: String,
    val userAvatar: String,
    val title: String,
    val category: String,
    val streamKey: String,
    val status: StreamStatus = StreamStatus.LIVE,
    val viewersCount: Long = 1420L,
    val startedAt: Long = System.currentTimeMillis(),
    val thumbnailGradientId: Int = 0
)

data class LiveChatMessage(
    val id: String,
    val streamId: String,
    val username: String,
    val avatarUrl: String,
    val message: String,
    val isSuperChat: Boolean = false,
    val superChatAmount: Double = 0.0,
    val timestamp: Long = System.currentTimeMillis()
)

enum class TransactionType {
    SUBSCRIPTION,
    TIP,
    PAYOUT,
    AD_REVENUE
}

@Entity(tableName = "transactions")
data class Transaction(
    @PrimaryKey val id: String,
    val userId: String,
    val amount: Double,
    val type: TransactionType,
    val status: String = "Completed",
    val title: String,
    val createdAt: Long = System.currentTimeMillis()
)
