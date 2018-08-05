package com.freeankit.freenotepad.view

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.support.design.widget.BottomSheetBehavior
import android.support.design.widget.BottomSheetDialogFragment
import android.support.v7.app.ActionBar
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.SeekBar
import com.freeankit.freenotepad.R
import com.freeankit.freenotepad.helper.showSnackbar
import com.freeankit.freenotepad.model.DataHolder
import com.freeankit.freenotepad.model.Note
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_create.*
import java.util.*
import com.freeankit.freenotepad.helper.TAG
import kotlinx.android.synthetic.main.bottom_sheet.*


/**
 * @author Ankit Kumar (ankitdroiddeveloper@gmail.com) on 20/12/2017 (MM/DD/YYYY )
 */
class CreateActivity : AppCompatActivity() {
    private var id: Note? = null
    private var isRated: Boolean = false

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
            Log.d(TAG, id.toString())
            getNotFromDB(id!!)
        }
        initToolbar()

    }

    private fun initBottomSheet() {
        val bottomSheetBehavior = BottomSheetBehavior.from(bottom_sheet)

// change the state of the bottom sheet
//        bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
//        bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
//        bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN

// set the peek height
        bottomSheetBehavior.peekHeight = 540

// set hideable or not
        bottomSheetBehavior.isHideable = false
        bottomSheetBehavior.setBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onSlide(p0: View, p1: Float) {

            }

            override fun onStateChanged(p0: View, p1: Int) {

            }
        })
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
        initSeekBars()
    }

    private fun initSeekBars() {
        seek_overall_rating.isEnabled = false

        seek_simple.setOnSeekBarChangeListener(seekbarListener())
        seek_dev_dff.setOnSeekBarChangeListener(seekbarListener())
        seek_achieve.setOnSeekBarChangeListener(seekbarListener())
        seek_everyone.setOnSeekBarChangeListener(seekbarListener())
        seek_everyday.setOnSeekBarChangeListener(seekbarListener())
    }

    private fun seekbarListener(): SeekBar.OnSeekBarChangeListener {
        return object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                val progresss = (seek_simple.progress + seek_achieve.progress +
                        seek_dev_dff.progress + seek_everyday.progress + seek_everyone.progress) / 5
                seek_overall_rating.progress = progresss
                isRated = true
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {

            }

            override fun onStopTrackingTouch(p0: SeekBar?) {

            }
        }
    }

    private fun getNotFromDB(note: Note) {
        edit_text.setText(note.text)
        title_text.setText(note.title)


        seek_simple.progress = if (note.simple_rate != null) note.simple_rate!! else 0

        seek_achieve.progress = if (note.achieve_rate != null) note.achieve_rate!! else 0
        seek_dev_dff.progress = if (note.dev_difficulty_rate != null) note.dev_difficulty_rate!! else 0
        seek_everyone.progress = if (note.everyone_rate != null) note.everyone_rate!! else 0
        seek_everyday.progress = if (note.everyday_rate != null) note.everyday_rate!! else 0
        seek_overall_rating.progress = if (note.overall_rate != null) note.overall_rate!! else 0
    }


    private fun save() {
        when {
            id == null -> saveNote()
            isRated -> rateIdea()
            else -> updateNote()
        }
    }

    private fun saveNote() {
        val note = Note()
        note.uid = DataHolder.getInstance(applicationContext).uid
        note.text = edit_text.text.toString()
        note.title = title_text.text.toString()

        note.simple_rate = seek_simple.progress
        note.achieve_rate = seek_achieve.progress
        note.dev_difficulty_rate = seek_dev_dff.progress
        note.everyday_rate = seek_everyday.progress
        note.everyone_rate = seek_everyone.progress
        note.overall_rate = seek_overall_rating.progress

        if (!note.title.isNullOrEmpty() || !note.text.isNullOrEmpty())
            saveDataToFirebase(note)
    }

    private fun rateIdea() {
        id?.simple_rate = seek_simple.progress
        id?.achieve_rate = seek_achieve.progress
        id?.dev_difficulty_rate = seek_dev_dff.progress
        id?.everyday_rate = seek_everyday.progress
        id?.everyone_rate = seek_everyone.progress
        id?.overall_rate = seek_overall_rating.progress

        id?.let { updateDataToFirebase(it) }
    }

    private fun updateNote() {

        id?.text = edit_text.text.toString()
        id?.title = title_text.text.toString()

        id?.let {
            if (DataHolder.getInstance(applicationContext).isVerified
                    && (it.uid == DataHolder.getInstance(applicationContext).uid
                            || it.uid == null)) {
                updateDataToFirebase(it)
            } else {
                Handler().postDelayed({
                    showSnackbar("Please login to update Ideas")
                }, 300)
            }
        }
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
        onBackPressed()
    }

    private fun deleteThisNote() {
        if (DataHolder.getInstance(applicationContext).isVerified) {
            if (id != null && id?.uid == DataHolder.getInstance(applicationContext).uid) {
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
                Handler().postDelayed({
                    onBackPressed()
                }, 300)
            } else {
                showSnackbar("You are allowed to delete only your Idea")
            }
        } else {
            showSnackbar("Please login to delete Ideas")
        }
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
            R.id.action_theme -> {
                initBottomSheet()
            }
            else -> return super.onOptionsItemSelected(item)
        }
        return super.onOptionsItemSelected(item)
    }

}
