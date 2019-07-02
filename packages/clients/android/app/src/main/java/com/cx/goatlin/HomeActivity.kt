package com.cx.goatlin

import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.*
import kotlinx.android.synthetic.main.activity_home.*
import android.widget.TextView
import com.cx.goatlin.api.model.Note
import com.cx.goatlin.api.service.Client
import com.cx.goatlin.helpers.CryptoHelper
import com.cx.goatlin.helpers.DatabaseHelper
import com.cx.goatlin.helpers.PreferenceHelper
import com.cx.goatlin.models.Account
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeActivity : AppCompatActivity() {
    private lateinit var listView: ListView
    private val apiService by lazy {
        Client.create()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        setSupportActionBar(toolbar)

        fab.setOnClickListener { view ->
            val intent = Intent(this, EditNoteActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()

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

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.home, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.sync -> {
                sync()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    /**
     * Sends authenticate user local notes to the back-end server
     */
    private fun sync() {
        val username: String = PreferenceHelper.getString("userEmail", "")
        val account: Account = DatabaseHelper(applicationContext).getAccount(username)
        val basicAuth: String = Client.getBasicAuthorizationHeader(account.username, account.password)
        val cursor: Cursor = DatabaseHelper(applicationContext).listNotes(account.id)
        while (cursor.moveToNext()) {
            val id: Int = cursor.getInt(cursor.getColumnIndex("_id"))
            val title: String = cursor.getString(cursor.getColumnIndex("title"))
            val content: String = cursor.getString(cursor.getColumnIndex("content"))
            val createdAt: String = cursor.getString(cursor.getColumnIndex("createdAt"))
            val note: Note = Note(title, content, createdAt)

            val call: Call<Void> = apiService.syncNote(basicAuth,username,id, note)
            call.enqueue(object: Callback<Void> {
                override fun onFailure(call: Call<Void>, t: Throwable) {
                    Log.e("Sync", t.message.toString())
                }

                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                    Log.i("Sync", "Note #$id: ${response.code()}")
                }
            })
        }
    }
}

class NoteCursorAdapter(context: Context, layout: Int, cursor: Cursor, flags: Int) : ResourceCursorAdapter(context, layout, cursor, flags) {

    override fun bindView(view: View, context: Context, cursor: Cursor) {
        val title = view.findViewById(R.id.title) as TextView
        title.text = CryptoHelper.decrypt(cursor.getString(cursor.getColumnIndex("title")))
    }

}