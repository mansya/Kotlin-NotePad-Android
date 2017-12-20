package com.freeankit.freenotepad

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import java.util.*

/**
 * @author Ankit Kumar (ankitdroiddeveloper@gmail.com) on 20/12/2017 (MM/DD/YYYY )
 */
class CreateActivity : AppCompatActivity() {
    operator fun get(context: Context): Intent {
        return Intent(context, CreateActivity::class.java)
    }

    private var editText: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create)
        editText = findViewById(R.id.edit_text)
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
        DataStore.INSTANCE.execute(Runnable {
            val note = updateNote()
            DataStore.getNotes().insert(note)
        })
    }

    private fun updateNote(): Note {
        val note = Note()
        note.setText(editText!!.text.toString())
        note.setUpdatedAt(Date())
        return note
    }
}