package com.example.emailapplication

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class EmailDatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_VERSION = 2
        private const val DATABASE_NAME = "EmailDatabase.db"

        private const val TABLE_NAME = "email_table"
        private const val COLUMN_ID = "id"
        private const val COLUMN_RECEIVER_MAIL = "receiver_mail"
        private const val COLUMN_SUBJECT = "subject"
        private const val COLUMN_BODY = "body"
        private const val COLUMN_FAVORITE = "favorite"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val createTable = "CREATE TABLE $TABLE_NAME (" +
                "${COLUMN_ID} INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "${COLUMN_RECEIVER_MAIL} TEXT, " +
                "${COLUMN_SUBJECT} TEXT, " +
                "${COLUMN_BODY} TEXT, " +
                "${COLUMN_FAVORITE} INTEGER DEFAULT 0" +
                ")"

        db?.execSQL(createTable)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    fun insertEmail(email: Email) {
        val db = writableDatabase
        val values = ContentValues()
        values.put(COLUMN_RECEIVER_MAIL, email.recevierMail)
        values.put(COLUMN_SUBJECT, email.subject)
        values.put(COLUMN_BODY, email.body)
        values.put(COLUMN_FAVORITE, if (email.favorite) 1 else 0)
        db.insert(TABLE_NAME, null, values)
        db.close()
    }


    @SuppressLint("Range")
    fun getAllEmails(): List<Email> {
        val emails = mutableListOf<Email>()
        val db = readableDatabase
        val cursor: Cursor = db.rawQuery("SELECT * FROM $TABLE_NAME", null)
        if (cursor.moveToFirst()) {
            do {
                val email = Email(
                    id = cursor.getInt(cursor.getColumnIndex(COLUMN_ID)),
                    recevierMail = cursor.getString(cursor.getColumnIndex(COLUMN_RECEIVER_MAIL)),
                    subject = cursor.getString(cursor.getColumnIndex(COLUMN_SUBJECT)),
                    body = cursor.getString(cursor.getColumnIndex(COLUMN_BODY)),
                    favorite = cursor.getInt(cursor.getColumnIndex(COLUMN_FAVORITE)) == 1
                )
                emails.add(email)
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return emails
    }

    @SuppressLint("Range")
    fun getFavoriteEmails(): List<Email> {
        val emails = mutableListOf<Email>()
        val db = readableDatabase
        val cursor: Cursor = db.rawQuery("SELECT * FROM $TABLE_NAME WHERE $COLUMN_FAVORITE = 1", null)

        if (cursor.moveToFirst()) {
            do {
                val email = Email(
                    id = cursor.getInt(cursor.getColumnIndex(COLUMN_ID)),
                    recevierMail = cursor.getString(cursor.getColumnIndex(COLUMN_RECEIVER_MAIL)),
                    subject = cursor.getString(cursor.getColumnIndex(COLUMN_SUBJECT)),
                    body = cursor.getString(cursor.getColumnIndex(COLUMN_BODY)),
                    favorite = true
                )
                emails.add(email)
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return emails
    }

    fun deleteEmail(emailId: Int) {
        val db = writableDatabase
        db.delete(TABLE_NAME, "$COLUMN_ID = ?", arrayOf(emailId.toString()))
        db.close()
    }
}
