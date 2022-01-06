package com.cx.goatlin

import android.content.ContentProvider
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.net.Uri
import android.database.sqlite.SQLiteQueryBuilder
import com.cx.goatlin.helpers.DatabaseHelper


class AccountProvider : ContentProvider() {

    private lateinit var database: DatabaseHelper

    private val ACCOUNTS = 1
    private val ACCOUNTS_ID = 2


    private val sURIMatcher = UriMatcher(UriMatcher.NO_MATCH)

    init {
        sURIMatcher.addURI(AUTHORITY, ACCOUNTS_TABLE, ACCOUNTS)
        sURIMatcher.addURI(AUTHORITY, ACCOUNTS_TABLE + "/#",
                ACCOUNTS_ID)
    }

    override fun onCreate(): Boolean {
        this.database = DatabaseHelper(context)
        return true
    }

    override fun query(uri: Uri, projection: Array<String>?, selection: String?,
                       selectionArgs: Array<String>?, sortOrder: String?): Cursor? {
        //return this.database.listAccounts()
        val queryBuilder = SQLiteQueryBuilder()
        queryBuilder.tables = ACCOUNTS_TABLE

        val uriType = sURIMatcher.match(uri)

        when (uriType) {
            ACCOUNTS_ID -> queryBuilder.appendWhere(ACCOUNTS_ID.toString() + "="
                    + uri.lastPathSegment)
            ACCOUNTS -> {
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
            ACCOUNTS -> id = sqlDB.insert(ACCOUNTS_TABLE, null, values)
            else -> throw IllegalArgumentException("Unknown URI: " + uri)
        }
        context.contentResolver.notifyChange(uri, null)
        return Uri.parse(ACCOUNTS_TABLE + "/" + id)
    }


    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int {
        throw UnsupportedOperationException("Not yet implemented")
    }

    override fun getType(uri: Uri): String? {
        TODO("Implement this to handle requests for the MIME type of the data" +
                "at the given URI")
    }

    override fun update(uri: Uri, values: ContentValues?, selection: String?,
                        selectionArgs: Array<String>?): Int {
        throw UnsupportedOperationException("Not yet implemented")
    }


    companion object {
        private val AUTHORITY = "com.cx.goatlin.accounts"
        private val ACCOUNTS_TABLE = "Accounts"
        val CONTENT_URI : Uri = Uri.parse("content://" + AUTHORITY + "/" +
                ACCOUNTS_TABLE)
        private val DATABASE_NAME = "data"
    }
}
