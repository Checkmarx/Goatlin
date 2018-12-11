package com.cx.vulnerablekotlinapp

import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.*

import kotlinx.android.synthetic.main.activity_home.*
import android.widget.TextView

class HomeActivity : AppCompatActivity() {
    private lateinit var listView: ListView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        setSupportActionBar(toolbar)

        fab.setOnClickListener { view ->
            val intent = Intent(this, EditNoteActivity::class.java)
            startActivity(intent)
        }

        val prefs = applicationContext.getSharedPreferences(applicationContext.packageName,
                Context.MODE_PRIVATE)
        val owner: Int = prefs.getInt("userId", -1)

        if (owner == -1) {
            // @todo user is not authenticated, send him to the login form
        }

        listView = findViewById<ListView>(R.id.list)

        val notes: Cursor = DatabaseHelper(this).listNotes(owner)
        val adapter = NoteCursorAdapter(this, R.layout.activity_home_note_item, notes, 0)
        listView.adapter = adapter

        listView.onItemClickListener = AdapterView.OnItemClickListener { adapterView, view, position, id ->
            val intent: Intent = Intent(applicationContext, EditNoteActivity::class.java)
            intent.putExtra("NOTE_ID", id.toString())
            startActivity(intent)
        }
    }
}

class NoteCursorAdapter(context: Context, layout: Int, cursor: Cursor, flags: Int) : ResourceCursorAdapter(context, layout, cursor, flags) {

    override fun bindView(view: View, context: Context, cursor: Cursor) {
        val title = view.findViewById(R.id.title) as TextView
        title.text = cursor.getString(cursor.getColumnIndex("title"))
    }

}