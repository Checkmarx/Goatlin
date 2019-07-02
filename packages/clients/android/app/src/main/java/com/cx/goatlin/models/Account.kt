package com.cx.goatlin.models

import android.database.Cursor

data class Account(var username: String, var password: String) {
    var id: Int = -1

    constructor(cursor: Cursor) : this(
            cursor.getString(cursor.getColumnIndex("username")),
            cursor.getString(cursor.getColumnIndex("password"))
    ){
        id = cursor.getInt(cursor.getColumnIndex("id"))
    }
}