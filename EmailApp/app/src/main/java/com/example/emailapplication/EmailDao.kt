package com.example.emailapplication

import androidx.room.*

@Dao
interface EmailDao {

    @Query("SELECT * FROM email_table WHERE subject = :subject")
    suspend fun getOrderBySubject(subject: String): Email?

    @Query("SELECT * FROM email_table WHERE favorite = 1")
    suspend fun getFavoriteEmails(): List<Email>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEmail(email: Email)

    @Query("DELETE FROM email_table WHERE id = :id")
    suspend fun deleteEmailById(id: Int)
}
