package com.freeankit.freenotepad.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import com.freeankit.freenotepad.R
import com.freeankit.freenotepad.model.Note
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
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
            R.id.action_delete -> {
                deleteThisNote()
            }
            else -> return super.onOptionsItemSelected(item)
        }
        return super.onOptionsItemSelected(item)
    }

    private fun save() {
        if (id == null) {
            saveNote()
        } else {
            updateNote()
        }
    }

    private fun saveNote() {
        val note = Note()
        note.text = edit_text.text.toString()
        note.title = title_text.text.toString()
        if (!note.title.isNullOrEmpty() || !note.text.isNullOrEmpty())
            saveDataToFirebase(note)
    }

    private fun updateNote() {
        id?.text = edit_text.text.toString()
        id?.title = title_text.text.toString()
        id?.let { updateDataToFirebase(it) }
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

    private fun deleteThisNote() {
        if (id != null) {
            val database = FirebaseDatabase.getInstance()
            val deleteQuery = database.getReference("Ideas").child("note").orderByChild("title").equalTo(id?.title)
            deleteQuery.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    for (appleSnapshot in dataSnapshot.children) {
                        appleSnapshot.ref.removeValue()
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    Log.e("Del", "onCancelled", databaseError.toException())
                }
            })
        }
        finish()
    }
}