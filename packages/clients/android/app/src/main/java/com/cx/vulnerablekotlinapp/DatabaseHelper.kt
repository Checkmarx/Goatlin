package com.cx.vulnerablekotlinapp

import android.content.ContentValues
import android.content.Context
import android.content.SharedPreferences
import android.database.Cursor
import android.database.SQLException
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.database.sqlite.SQLiteStatement
import android.preference.PreferenceManager
import android.util.Log
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

    /**
     * Performs a database query to retrieve user record
     *
     * @param username String
     * @param password String
     * @return Int User record ID or -1 id record was not found
     */
    public fun userLogin(username: String , password: String) : Int {
        var ret = "select id from ${TABLE_ACCOUNTS} where username = '" + username + "' and password = '" + password + "';"
        var cursor: Cursor = this.readableDatabase.rawQuery(ret, null)
        if(cursor.count == 1)
        {
            cursor.moveToFirst()
            return cursor.getInt(0)
        }
        return -1
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

    public fun addNote (title: String, content :String, owner: Int): Boolean {
        val db: SQLiteDatabase = this.writableDatabase
        val record: ContentValues = ContentValues()
        var status: Boolean = true

        record.put("title", title)
        record.put("content", content)
        record.put("owner", owner)

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

    public fun updateNote (title: String, content :String, id: Int): Boolean {
        val db: SQLiteDatabase = this.writableDatabase
        val values: ContentValues = ContentValues()
        var status: Boolean = true

        values.put("title", title)
        values.put("content", content)


        val count: Int = db.update(TABLE_NOTES, values, "id = ?", arrayOf(id.toString()))

        return count == 1
    }

    public fun listNotes (owner: Int): Cursor {
        val db: SQLiteDatabase = this.readableDatabase
        val columns: Array<String> = arrayOf("id AS _id", "title")
        val filter: String = "owner = ?"
        val filterValues: Array<String> = arrayOf(owner.toString())

        return db.query(false, TABLE_NOTES, columns, filter, filterValues,
                "","","","")
    }

    public fun getNote(id: Int): Array<String> {
        val db: SQLiteDatabase = this.readableDatabase
        val columns: Array<String> = arrayOf("title", "content")
        val filter: String = "id = ?"
        val filterValues: Array<String> = arrayOf(id.toString())
        val note: Array<String> = arrayOf("", "")

        val cursor: Cursor = db.query(false, TABLE_NOTES, columns, filter, filterValues,
                "","","","")

        if (cursor.count == 1) {
            cursor.moveToFirst()

            note[0] = cursor.getString(cursor.getColumnIndex("title"))
            note[1] = cursor.getString(cursor.getColumnIndex("content"))
        }

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