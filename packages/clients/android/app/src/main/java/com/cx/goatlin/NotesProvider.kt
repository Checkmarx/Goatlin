package com.cx.goatlin

import android.content.ContentProvider
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.database.sqlite.SQLiteQueryBuilder
import android.net.Uri
import com.cx.goatlin.helpers.DatabaseHelper

class NotesProvider : ContentProvider() {

    private lateinit var database:DatabaseHelper

    private val NOTES = 1
    private val NOTES_ID = 2


    private val sURIMatcher = UriMatcher(UriMatcher.NO_MATCH)

    init {
        sURIMatcher.addURI(NotesProvider.AUTHORITY, NotesProvider.NOTES_TABLE, NOTES)
        sURIMatcher.addURI(NotesProvider.AUTHORITY, NotesProvider.NOTES_TABLE + "/#",
                NOTES_ID)
    }

    override fun onCreate(): Boolean {
        this.database = DatabaseHelper(context)
        return true
    }

    override fun query(uri: Uri, projection: Array<String>?, selection: String?,
                       selectionArgs: Array<String>?, sortOrder: String?): Cursor? {
        val queryBuilder = SQLiteQueryBuilder()
        queryBuilder.tables = NotesProvider.NOTES_TABLE

        val uriType = sURIMatcher.match(uri)

        when (uriType) {
            NOTES_ID -> queryBuilder.appendWhere(NOTES_ID.toString() + "="
                    + uri.lastPathSegment)
            NOTES -> {
            }
            else -> throw IllegalArgumentException("Unknown URI")
        }

        val cursor = queryBuilder.query(this.database.readableDatabase,
                projection, selection, selectionArgs, null, null,
                sortOrder)
        cursor.setNotificationUri(context.contentResolver,
                uri)
        return cursor

    }

    override fun insert(uri: Uri, values: ContentValues): Uri? {
        val uriType = sURIMatcher.match(uri)

        val sqlDB = this.database.writableDatabase

        val id: Long
        when (uriType) {
            NOTES -> id = sqlDB.insert(NotesProvider.NOTES_TABLE, null, values)
            else -> throw IllegalArgumentException("Unknown URI: " + uri)
        }
        context.contentResolver.notifyChange(uri, null)
        return Uri.parse(NotesProvider.NOTES_TABLE + "/" + id)
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int {
        TODO("Implement this to handle requests to delete one or more rows")
    }

    override fun getType(uri: Uri): String? {
        TODO("Implement this to handle requests for the MIME type of the data" +
                "at the given URI")
    }

    override fun update(uri: Uri, values: ContentValues?, selection: String?,
                        selectionArgs: Array<String>?): Int {
        TODO("Implement this to handle requests to update one or more rows.")
    }


    companion object {
        private val AUTHORITY = "com.cx.goatlin.notes"
        private val NOTES_TABLE = "Notes"
        val CONTENT_URI : Uri = Uri.parse("content://" + AUTHORITY + "/" +
                NOTES_TABLE)
        private val DATABASE_NAME = "data"
    }
}
