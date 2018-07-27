package com.freeankit.freenotepad.view

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.support.design.widget.Snackbar
import android.support.v7.app.ActionBar
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            root.transitionName = "robot"
        }

        if (intent.hasExtra("id")) {
            id = intent.extras["id"] as Note
        }
        if (id != null) {
            getNotFromDB(id!!)
        }
        initToolbar()
    }


    private fun initToolbar() {
        setSupportActionBar(action_bar_)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        (action_bar_ as Toolbar).setContentInsetsAbsolute(0, 0)
        (action_bar_ as Toolbar).drawingCacheBackgroundColor = applicationContext!!.resources.getColor(R.color.white)

        (supportActionBar as ActionBar).title = id?.title ?: "New Idea"
        action_bar_.setTitleTextColor(resources.getColor(R.color.white))

        fab_.setOnClickListener {
            save()
            onBackPressed()
        }
    }


    private fun getNotFromDB(note: Note) {
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
                    showSnackbar("Deleted idea")
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    Log.e("Del", "onCancelled", databaseError.toException())
                }
            })
        }

        Handler().postDelayed({
            onBackPressed()
        }, 300)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_accept, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
            }
            R.id.action_delete -> {
                deleteThisNote()
            }
            else -> return super.onOptionsItemSelected(item)
        }
        return super.onOptionsItemSelected(item)
    }


}

fun Activity.showSnackbar(message: String) {
    val sb = Snackbar.make(findViewById<View>(android.R.id.content), message, Snackbar.LENGTH_SHORT)
    val sbView = sb.view
    sbView.setBackgroundColor(applicationContext?.resources?.getColor(R.color.colorAccent) as Int)
    val textView = sbView.findViewById<View>(android.support.design.R.id.snackbar_text) as TextView
    textView.setTextColor(applicationContext?.resources?.getColor(R.color.white) as Int)
    sb.show()

}

fun Activity.hideKeyboard() {
    val imm: InputMethodManager = getSystemService(
            Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(title_text.windowToken, 0)
}

fun Activity.showKeyboard() {
    val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)
}