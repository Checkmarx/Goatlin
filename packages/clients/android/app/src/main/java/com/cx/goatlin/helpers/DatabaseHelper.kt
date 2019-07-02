package com.cx.goatlin.helpers

import android.content.ContentValues
import android.content.Context
import android.content.SharedPreferences
import android.database.Cursor
import android.database.SQLException
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import com.cx.goatlin.models.Account
import com.cx.goatlin.models.Note
import java.io.File
import java.io.FileOutputStream

class DatabaseHelper (val context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    private val preferences: SharedPreferences = context.getSharedPreferences(
            "${context.packageName}.database_versions",
            Context.MODE_PRIVATE
    )

    private fun installedDatabaseIsOutdated(): Boolean {
        return preferences.getInt(DATABASE_NAME, 0) < DATABASE_VERSION
    }

    private fun writeDatabaseVersionInPreferences() {
        preferences.edit().apply {
            putInt(DATABASE_NAME, DATABASE_VERSION)
            apply()
        }
    }

    private fun installDatabaseFromAssets() {
        val inputStream = context.assets.open("$ASSETS_PATH/$DATABASE_NAME.sqlite3")

        try {
            val outputFile = File(context.getDatabasePath(DATABASE_NAME).path)
            val outputStream = FileOutputStream(outputFile)

            inputStream.copyTo(outputStream)
            inputStream.close()

            outputStream.flush()
            outputStream.close()
        } catch (exception: Throwable) {
            throw RuntimeException("The $DATABASE_NAME database couldn't be installed.", exception)
        }
    }

    @Synchronized
    private fun installOrUpdateIfNecessary() {
        if (installedDatabaseIsOutdated()) {
            context.deleteDatabase(DATABASE_NAME)
            installDatabaseFromAssets()
            writeDatabaseVersionInPreferences()
        }
    }

    public fun createAccount(username: String, password: String) : Boolean {
        val db: SQLiteDatabase = this.writableDatabase
        val record: ContentValues = ContentValues()
        var status = true

        record.put("username", username)
        record.put("password", password)

        try {
            db.insertOrThrow(TABLE_ACCOUNTS, null, record)
        }
        catch (e: SQLException) {
            Log.e("Database signup", e.toString())
            status = false
        }
        finally {
            return status
        }
    }

    public fun getAccount(username: String): Account {
        val db: SQLiteDatabase = this.readableDatabase
        val columns: Array<String> = arrayOf("id", "username", "password")
        val filter: String = "username = ?"
        val filterValues: Array<String> = arrayOf(username)
        val account: Account

        val cursor: Cursor = db.query(false, TABLE_ACCOUNTS, columns, filter, filterValues,
                "","","","")

        if (cursor.count != 1) {
            throw Exception("Account not found")
        }

        cursor.moveToFirst()

        account = Account(cursor)

        return account
    }

    public fun listAccounts(): Cursor{
        val db: SQLiteDatabase = this.readableDatabase
        val columns: Array<String> = arrayOf("id AS _id", "username","password")
        return db.query(TABLE_ACCOUNTS, columns, null, null,
                "","","","")
    }

    public fun addNote (note: Note): Boolean {
        val db: SQLiteDatabase = this.writableDatabase
        val record: ContentValues = ContentValues()
        var status: Boolean = true

        record.put("title", note.title)
        record.put("content", note.content)
        record.put("owner", note.owner)

        try {
            db.insertOrThrow(TABLE_NOTES, null, record)
        }
        catch (e: SQLException) {
            Log.e("Add Note", e.toString())
            status = false
        }
        finally {
            return status
        }
    }

    public fun updateNote (note: Note): Boolean {
        val db: SQLiteDatabase = this.writableDatabase
        val values: ContentValues = ContentValues()
        var status: Boolean = true

        values.put("title", note.title)
        values.put("content", note.content)


        val count: Int = db.update(TABLE_NOTES, values, "id = ?",
                arrayOf(note.id.toString()))

        return count == 1
    }

    public fun listNotes (owner: Int): Cursor {
        val db: SQLiteDatabase = this.readableDatabase
        val columns: Array<String> = arrayOf("id AS _id", "title", "content", "createdAt")
        val filter: String = "owner = ?"
        val filterValues: Array<String> = arrayOf(owner.toString())

        return db.query(false, TABLE_NOTES, columns, filter, filterValues,
                "","","","")
    }

    public fun getNote(id: Int): Note {
        val db: SQLiteDatabase = this.readableDatabase
        val columns: Array<String> = arrayOf("title", "content")
        val filter: String = "id = ?"
        val filterValues: Array<String> = arrayOf(id.toString())
        val note: Note

        val cursor: Cursor = db.query(false, TABLE_NOTES, columns, filter, filterValues,
                "","","","")

        if (cursor.count != 1) {
            throw Exception("Note not found")
        }

        cursor.moveToFirst()

        note = Note(CryptoHelper.decrypt(cursor.getString(cursor.getColumnIndex("title"))),
                CryptoHelper.decrypt(cursor.getString(cursor.getColumnIndex("content"))))
        note.id = id

        return note
    }

    override fun getWritableDatabase(): SQLiteDatabase {
        // throw RuntimeException("The $DATABASE_NAME database is not writable.")
        installOrUpdateIfNecessary()
        return super.getWritableDatabase()
    }

    override fun getReadableDatabase(): SQLiteDatabase {
        installOrUpdateIfNecessary()
        return super.getReadableDatabase()
    }

    override fun onCreate(db: SQLiteDatabase?) {
        // Nothing to do: Database already created
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        // Nothing to do: Database already created
    }

    companion object {
        const val ASSETS_PATH = "database"
        const val DATABASE_NAME = "data"
        const val DATABASE_VERSION = 4
        const val TABLE_ACCOUNTS = "Accounts"
        const val TABLE_NOTES = "Notes"
    }
}