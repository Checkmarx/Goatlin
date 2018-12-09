package com.cx.vulnerablekotlinapp

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Gravity
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.EditText
import android.widget.Toast

class AddNoteActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notes_add)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.add_note_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle item selection
        return when (item.itemId) {
            R.id.save -> {
                val prefs: SharedPreferences = applicationContext.getSharedPreferences(
                        applicationContext.packageName, Context.MODE_PRIVATE)
                val title: String = findViewById<EditText>(R.id.title).text.toString()
                val content: String = findViewById<EditText>(R.id.content).text.toString()
                val owner: Int = prefs.getInt("userId", -1)

                if (owner == -1) {
                    // @todo user is not authenticated, send him to the login form
                }

                val status = DatabaseHelper(applicationContext).addNote(title, content, owner)

                if (status == true) {
                    val intent = Intent(this, HomeActivity::class.java)
                    startActivity(intent)
                }
                else {
                    val toast: Toast = Toast.makeText(this@AddNoteActivity,
                            "Failed to save note", Toast.LENGTH_LONG)

                    toast.setGravity(Gravity.CENTER_VERTICAL or Gravity.CENTER_HORIZONTAL,
                            0, 0)
                    toast.show()
                }

                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}