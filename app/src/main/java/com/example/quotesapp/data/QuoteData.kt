package com.example.quotesapp.data

import androidx.room.Dao
import androidx.room.Database
import androidx.room.Delete
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.RoomDatabase
import kotlinx.coroutines.flow.Flow

@Entity(tableName = "quotes")
data class QuoteEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val text: String,
    val attribution: String,
    val tags: String?,
    val notes: String?,
    val createdAt: Long
)

@Dao
interface QuoteDao {
    @Query("SELECT * FROM quotes ORDER BY createdAt DESC")
    fun getAll(): Flow<List<QuoteEntity>>

    @Insert
    suspend fun insert(entity: QuoteEntity)

    @Delete
    suspend fun delete(entity: QuoteEntity)
}

@Database(entities = [QuoteEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun quoteDao(): QuoteDao
}

class QuoteRepository(private val dao: QuoteDao) {
    fun myQuotes(): Flow<List<QuoteEntity>> = dao.getAll()

    suspend fun add(
        text: String,
        author: String,
        tags: String?,
        notes: String?,
        now: Long
    ) {
        dao.insert(
            QuoteEntity(
                text = text,
                attribution = author,
                tags = tags,
                notes = notes,
                createdAt = now
            )
        )
    }
}
