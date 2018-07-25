package com.freeankit.freenotepad.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
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
    private var list_of_items = arrayOf("1", "2", "3", "4", "5", "6", "7", "8", "9", "10")

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
        if (id != null) {
            hideKeybaord()
            getNotFromDB(id!!)
        } else {
            showKeyboard()
        }
        setSpinners()
    }

    private fun setSpinners() {
        // Create an ArrayAdapter using a simple spinner layout and languages array
        val aa = ArrayAdapter(this, android.R.layout.simple_spinner_item, list_of_items)
        // Set layout to use when the list of choices appear
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        // Set Adapter to Spinner
        spinner_achievable!!.adapter = aa
        spinner_dev_diff!!.adapter = aa
        spinner_everyday!!.adapter = aa
        spinner_everyone!!.adapter = aa
        spinner_simple!!.adapter = aa
    }

    private fun getNotFromDB(note: Note) {
        //val note = DataStore.notes.byId(id)
        edit_text.setText(note.text)
        title_text.setText(note.title)

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

    fun hideKeybaord() {
        val imm: InputMethodManager = getSystemService(
                Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(title_text.windowToken, 0)
    }

    fun showKeyboard() {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)
    }
}
