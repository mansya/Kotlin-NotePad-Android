package com.freeankit.freenotepad.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import com.freeankit.freenotepad.R
import com.freeankit.freenotepad.db.DataStore
import com.freeankit.freenotepad.model.Note
import kotlinx.android.synthetic.main.activity_create.*
import java.util.*

/**
 * @author Ankit Kumar (ankitdroiddeveloper@gmail.com) on 20/12/2017 (MM/DD/YYYY )
 */
class CreateActivity : AppCompatActivity() {
    private var id: Int = -1

    companion object {
        operator fun get(context: Context): Intent {
            return Intent(context, CreateActivity::class.java)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create)
        if (intent.hasExtra("id")) {
            id = intent.extras["id"] as Int
        }
        if (id != -1)
            getNotFromDB(id)
    }

    private fun getNotFromDB(id: Int) {
        val note = DataStore.notes.byId(id)
        edit_text.setText(note.text)
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
        if (id == -1)
            DataStore.execute(Runnable {
                val note = saveNote()
                DataStore.notes.insert(note)
            })
        else DataStore.execute(Runnable {
            val note = updateNote()
            DataStore.notes.update(note)
        })
    }

    private fun saveNote(): Note {
        val note = Note()
        note.text = edit_text.text.toString()
        note.updatedAt = Date()
        return note
    }

    private fun updateNote(): Note {
        val note = Note()
        note.id = id
        note.text = edit_text.text.toString()
        note.updatedAt = Date()
        return note
    }
}