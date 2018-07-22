package com.freeankit.freenotepad.view

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.freeankit.freenotepad.R
import com.freeankit.freenotepad.db.DataStore
import com.freeankit.freenotepad.helper.layoutInflator
import com.freeankit.freenotepad.model.Note
import kotlinx.android.synthetic.main.item_note.view.*

/**
 * @author Ankit Kumar (ankitdroiddeveloper@gmail.com) on 20/12/2017 (MM/DD/YYYY )
 */
class NotesAdapter(private val context: Context, private val listener: NotesAdapter.OnPlaceClickListener) : RecyclerView.Adapter<NotesAdapter.NotesViewHolder>() {
    private var isRefreshing = false
    private var notes: MutableList<Note> = ArrayList()
    override fun onBindViewHolder(holder: NotesViewHolder, position: Int) {
        val note = notes[position]
        holder.itemView.text_.text = note.text
        holder.itemView.content.setOnClickListener {
            listener.onItemClicked(note)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotesViewHolder {
        val view = context.layoutInflator.inflate(R.layout.item_note, parent, false)
        return NotesViewHolder(view)
    }

    override fun getItemCount(): Int {
        return notes.size
    }

    inner class NotesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun getItemId(position: Int): Long {
        return notes[position].id.toLong()
    }


    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        refresh()
    }

    fun refresh() {
        if (isRefreshing) return
        isRefreshing = true
        DataStore.execute {
            val notes = DataStore.notes.all
            Handler(Looper.getMainLooper()).post {
                this@NotesAdapter.notes = notes as MutableList<Note>
                notifyDataSetChanged()
                isRefreshing = false
            }
        }
    }


    interface OnPlaceClickListener {
        fun onItemClicked(project: Note)
    }


}