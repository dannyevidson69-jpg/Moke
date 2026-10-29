package com.example.goal.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.goal.data.model.Comment
import com.example.goal.data.model.Conversation
import com.example.goal.data.model.LiveStream
import com.example.goal.data.model.Message
import com.example.goal.data.model.Transaction
import com.example.goal.data.model.User
import com.example.goal.data.model.Video

@Database(
    entities = [
        User::class,
        Video::class,
        Comment::class,
        Message::class,
        Conversation::class,
        LiveStream::class,
        Transaction::class
    ],
    version = 1,
    exportSchema = false
)
abstract class GoalDatabase : RoomDatabase() {
    abstract fun goalDao(): GoalDao

    companion object {
        @Volatile
        private var INSTANCE: GoalDatabase? = null

        fun getDatabase(context: Context): GoalDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    GoalDatabase::class.java,
                    "goal_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
