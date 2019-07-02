package com.cx.goatlin

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Gravity
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.EditText
import android.widget.Toast
import com.cx.goatlin.helpers.CryptoHelper
import com.cx.goatlin.helpers.DatabaseHelper
import com.cx.goatlin.helpers.PreferenceHelper
import com.cx.goatlin.models.Note

class EditNoteActivity : AppCompatActivity() {
    lateinit var note: Note

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_note_edit)

        initializeNote()

        findViewById<EditText>(R.id.title).setText(note.title)
        findViewById<EditText>(R.id.content).setText(note.content)
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
                val saved: Boolean = saveNote()

                if (saved == false) {
                    showError("Could not save!")
                }

                // val intent = Intent(this, HomeActivity::class.java)
                // startActivity(intent)
                finish()

                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    /**
     * Initializes internal "note" property with a new Note or one from database depending on
     * whether intent string extra "NOTE_ID" is defined
     */
    private fun initializeNote() {
        val noteId: String? = intent.getStringExtra("NOTE_ID")

        if (noteId != null) {
            try {
                note = DatabaseHelper(applicationContext).getNote(noteId.toInt())
            }
            catch (e: Exception) {
                Log.e("EditNoteActivity", e.toString())
                note = Note("", "")
            }
        } else {
            note = Note("", "")
        }
    }

    /**
     * Saves internal note property to database
     */
    private fun saveNote(): Boolean {
        var status: Boolean

        // update note
        note.title = CryptoHelper.encrypt(findViewById<EditText>(R.id.title).text.toString())
        note.content = CryptoHelper.encrypt(findViewById<EditText>(R.id.content).text.toString())

        if (note.id == -1) {
            // it's a new note
            val owner = PreferenceHelper.getInt("userId", -1)

            note.owner = owner

            status = DatabaseHelper(applicationContext).addNote(note)
        } else {
            status = DatabaseHelper(applicationContext).updateNote(note)
        }

        return status
    }

    /**
     * Show a Toast with given error message
     *
     * @param message CharSequence  Error message to display
     * @return void
     */
    private fun showError(message: CharSequence) {
        val toast: Toast = Toast.makeText(this@EditNoteActivity, message, Toast.LENGTH_LONG)

        toast.setGravity(Gravity.TOP or Gravity.CENTER_HORIZONTAL, 0, 0)
        toast.show()
    }
}