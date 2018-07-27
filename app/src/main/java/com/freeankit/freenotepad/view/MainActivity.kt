@file:Suppress("DEPRECATION")

package com.freeankit.freenotepad.view

import android.app.ActivityOptions
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.support.v4.content.ContextCompat.startActivity
import android.support.v4.view.ViewCompat
import android.support.v7.app.ActionBar
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.StaggeredGridLayoutManager
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.freeankit.freenotepad.R
import com.freeankit.freenotepad.helper.SpaceItemDecoration
import com.freeankit.freenotepad.model.Note
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*


/**
 * @author Ankit Kumar (ankitdroiddeveloper@gmail.com) on 20/12/2017 (MM/DD/YYYY )
 */
class MainActivity : AppCompatActivity(), NotesAdapter.OnPlaceClickListener {

    override fun onItemClicked(project: Note, sharedView: View) {
        Handler().postDelayed({
            // Check if we're running on Android 5.0 or higher
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                // get the element that receives the click event // get the common element for the transition in this activity
                val options = ActivityOptions
                        .makeSceneTransitionAnimation(this, sharedView, ViewCompat.getTransitionName(sharedView))
//                startActivity(CreateActivity[this].putExtra("id", project), ActivityOptions.makeSceneTransitionAnimation(this).toBundle())
                startActivity(CreateActivity[this].putExtra("id", project), options.toBundle())
            } else {
                startActivity(CreateActivity[this].putExtra("id", project))

            }
        }, 150)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val slm = StaggeredGridLayoutManager(2, 1)
//        val slm = GridLayoutManager(this, 2)

        recycler.layoutManager = slm
        recycler.addItemDecoration(SpaceItemDecoration(this, R.dimen.margin_small))
        recycler.adapter = NotesAdapter(this, this, slm)
        fab.setOnClickListener { startActivity(CreateActivity[this]) }
        initToolbar()
    }

    override fun onResume() {
        super.onResume()
        refresh()
    }

    public override fun onDestroy() {
        super.onDestroy()

    }

    private fun refresh() {
        loadNotes()

    }

    /**
     * Initializes toolbar
     */
    private fun initToolbar() {
        setSupportActionBar(action_bar)
        (action_bar as Toolbar).setContentInsetsAbsolute(0, 0)
        (action_bar as Toolbar).drawingCacheBackgroundColor = applicationContext!!.resources.getColor(R.color.white)
    }


    private fun loadNotes() {
        // Write a message to the database
        val database = FirebaseDatabase.getInstance()
        val myRef = database.getReference("Ideas").child("note")
        myRef.addValueEventListener(object : ValueEventListener {
            val noteList = arrayListOf<Note>()
            override fun onDataChange(snapshot: DataSnapshot) {
                noteList.clear()
                Log.e("Count ", "" + snapshot.childrenCount)
                for (postSnapshot in snapshot.children) {
                    val post = postSnapshot.getValue(Note::class.java)
                    post?.let { noteList.add(it) }
                }
                Log.e("Note ", "" + noteList)
                (recycler.adapter as NotesAdapter).addData(noteList)
            }

            override fun onCancelled(p0: DatabaseError) {
            }
        })

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_settings -> {

            }
            else -> return super.onOptionsItemSelected(item)
        }
        return super.onOptionsItemSelected(item)
    }
}
