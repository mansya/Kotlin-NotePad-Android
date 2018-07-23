package com.freeankit.freenotepad.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import com.freeankit.freenotepad.R
import com.freeankit.freenotepad.model.Note
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_create.*
import java.util.*


/**
 * @author Ankit Kumar (ankitdroiddeveloper@gmail.com) on 20/12/2017 (MM/DD/YYYY )
 */
class CreateActivity : AppCompatActivity() {
    private var id: Note? = null

    companion object {
        operator fun get(context: Context): Intent {
            return Intent(context, CreateActivity::class.java)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create)
        if (intent.hasExtra("id")) {
            id = intent.extras["id"] as Note
        }
        id?.let { getNotFromDB(it) }
    }

    private fun getNotFromDB(note: Note) {
        //val note = DataStore.notes.byId(id)
        edit_text.setText(note.text)
        title_text.setText(note.title)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_accept, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_accept -> {
                save()
                finish()
            }
            else -> return super.onOptionsItemSelected(item)
        }
        return super.onOptionsItemSelected(item)
    }

    private fun save() {
        if (id == null) {
            val note = saveNote()
            saveDataToFirebase(note)
        } else {
            val note = updateNote()
            note?.let { updateDataToFirebase(it) }
        }
    }

    private fun saveNote(): Note {
        val note = Note()
        note.text = edit_text.text.toString()
        note.title = title_text.text.toString()
        return note
    }

    private fun updateNote(): Note? {
        id?.text = edit_text.text.toString()
        id?.title = title_text.text.toString()
        return id
    }

    private fun saveDataToFirebase(note: Note) {
        val database = FirebaseDatabase.getInstance()
        val ideasDB = database.getReference("Ideas")
        val key = ideasDB.push().key
        note.id = key!!
        val noteValues: Map<String, Any> = note.toMap()
        val childUpdates = HashMap<String, Any>()
        childUpdates["/note/$key"] = noteValues
        ideasDB.updateChildren(childUpdates)
    }

    private fun updateDataToFirebase(note: Note) {
        val database = FirebaseDatabase.getInstance()
        val ideasDB = database.getReference("Ideas")
        val noteValues: Map<String, Any> = note.toMap()
        val childUpdates = HashMap<String, Any>()
        childUpdates["/note/${note.id}"] = noteValues
        ideasDB.updateChildren(childUpdates)
    }
}