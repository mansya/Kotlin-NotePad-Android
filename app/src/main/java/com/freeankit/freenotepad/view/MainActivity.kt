package com.freeankit.freenotepad.view

import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.GridLayoutManager
import android.util.Log
import android.view.Menu
import android.view.MenuItem
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

    override fun onItemClicked(project: Note) {
        Handler().postDelayed({
            startActivity(CreateActivity[this].putExtra("id", project))
        }, 150)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recycler.layoutManager = GridLayoutManager(this, 2)
        recycler.addItemDecoration(SpaceItemDecoration(this, R.dimen.margin_small))
        recycler.adapter = NotesAdapter(this, this)
        fab.setOnClickListener { startActivity(CreateActivity[this]) }
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

//    override fun onStart() {
//        super.onStart()
//
//        val database = FirebaseDatabase.getInstance()
//        val myRef = database.getReference("Ideas").child("note")
//        myRef.addChildEventListener(object : ChildEventListener {
//            override fun onCancelled(databaseError: DatabaseError) {
//                Log.e("Error", "postMessages:onCancelled", databaseError.toException())
//                Toast.makeText(baseContext, "Failed to load Message.", Toast.LENGTH_SHORT).show()
//            }
//
//            override fun onChildMoved(dataSnapshot: DataSnapshot, p1: String?) {
//
//            }
//
//            override fun onChildChanged(dataSnapshot: DataSnapshot, p1: String?) {
//                val message = dataSnapshot.getValue(Note::class.java)
//            }
//
//            override fun onChildAdded(dataSnapshot: DataSnapshot, previousChildName: String?) {
//                val message = dataSnapshot.getValue(Note::class.java)
////                message?.let { noteList.add(it) }
//                message?.let { (recycler.adapter as NotesAdapter).addNote(it) }
//            }
//
//            override fun onChildRemoved(dataSnapshot: DataSnapshot) {
//                val message = dataSnapshot.getValue(Note::class.java)
//            }
//        })
//
//
//    }


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
