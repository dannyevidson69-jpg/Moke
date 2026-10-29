package com.example.goal.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.goal.data.model.Comment
import com.example.goal.data.model.Conversation
import com.example.goal.data.model.LiveStream
import com.example.goal.data.model.Message
import com.example.goal.data.model.Transaction
import com.example.goal.data.model.User
import com.example.goal.data.model.Video
import com.example.goal.data.model.VideoType
import kotlinx.coroutines.flow.Flow

@Dao
interface GoalDao {
    // Videos
    @Query("SELECT * FROM videos ORDER BY createdAt DESC")
    fun getAllVideos(): Flow<List<Video>>

    @Query("SELECT * FROM videos WHERE type = :type ORDER BY createdAt DESC")
    fun getVideosByType(type: VideoType): Flow<List<Video>>

    @Query("SELECT * FROM videos WHERE id = :id")
    suspend fun getVideoById(id: String): Video?

    @Query("SELECT * FROM videos WHERE isLiked = 1 ORDER BY createdAt DESC")
    fun getLikedVideos(): Flow<List<Video>>

    @Query("SELECT * FROM videos WHERE isSaved = 1 ORDER BY createdAt DESC")
    fun getSavedVideos(): Flow<List<Video>>

    @Query("SELECT * FROM videos WHERE isUserOwned = 1 ORDER BY createdAt DESC")
    fun getUserOwnedVideos(): Flow<List<Video>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertVideo(video: Video)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertVideos(videos: List<Video>)

    @Update
    suspend fun updateVideo(video: Video)

    @Query("UPDATE videos SET isLiked = :isLiked, likesCount = likesCount + :delta WHERE id = :id")
    suspend fun toggleVideoLike(id: String, isLiked: Boolean, delta: Int)

    @Query("UPDATE videos SET isSaved = :isSaved WHERE id = :id")
    suspend fun toggleVideoSave(id: String, isSaved: Boolean)

    @Query("UPDATE videos SET viewsCount = viewsCount + 1 WHERE id = :id")
    suspend fun incrementViews(id: String)

    @Query("DELETE FROM videos WHERE id = :id")
    suspend fun deleteVideo(id: String)

    // Comments
    @Query("SELECT * FROM comments WHERE videoId = :videoId ORDER BY createdAt DESC")
    fun getCommentsForVideo(videoId: String): Flow<List<Comment>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertComment(comment: Comment)

    @Query("UPDATE comments SET isLiked = :isLiked, likesCount = likesCount + :delta WHERE id = :commentId")
    suspend fun toggleCommentLike(commentId: String, isLiked: Boolean, delta: Int)

    // User Profile
    @Query("SELECT * FROM users LIMIT 1")
    fun getCurrentUser(): Flow<User?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: User)

    @Update
    suspend fun updateUser(user: User)

    // Conversations & Messages
    @Query("SELECT * FROM conversations ORDER BY lastTimestamp DESC")
    fun getAllConversations(): Flow<List<Conversation>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertConversations(conversations: List<Conversation>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertConversation(conversation: Conversation)

    @Query("SELECT * FROM messages WHERE conversationId = :conversationId ORDER BY createdAt ASC")
    fun getMessagesForConversation(conversationId: String): Flow<List<Message>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMessage(message: Message)

    // Live Streams
    @Query("SELECT * FROM live_streams WHERE status = 'LIVE' ORDER BY viewersCount DESC")
    fun getLiveStreams(): Flow<List<LiveStream>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLiveStreams(streams: List<LiveStream>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLiveStream(stream: LiveStream)

    // Transactions
    @Query("SELECT * FROM transactions ORDER BY createdAt DESC")
    fun getAllTransactions(): Flow<List<Transaction>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTransaction(transaction: Transaction)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTransactions(transactions: List<Transaction>)
}
