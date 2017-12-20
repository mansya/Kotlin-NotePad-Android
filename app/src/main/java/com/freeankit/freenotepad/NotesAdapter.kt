package com.freeankit.freenotepad

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

/**
 * @author Ankit Kumar (ankitdroiddeveloper@gmail.com) on 20/12/2017 (MM/DD/YYYY )
 */
class NotesAdapter(private val context: Context) : RecyclerView.Adapter<NotesAdapter.NotesViewHolder>() {
    private var isRefreshing = false
    private var notes: List<Note> = ArrayList()
    override fun onBindViewHolder(holder: NotesViewHolder, position: Int) {
        val note = notes[position]
        holder.text.text = note.text
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotesViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_note, parent, false)
        return NotesViewHolder(view)
    }

    override fun getItemCount(): Int {
        return notes.size
    }

    class NotesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        internal var text: TextView = itemView.findViewById(R.id.text_)
    }

    override fun getItemId(position: Int): Long {
        return notes[position].id.toLong()
    }


    override fun onAttachedToRecyclerView(recyclerView: RecyclerView?) {
        refresh()
    }

    fun refresh() {
        if (isRefreshing) return
        isRefreshing = true
        DataStore.execute {
            val notes = DataStore.notes?.all
            Handler(Looper.getMainLooper()).post {
                this@NotesAdapter.notes = notes!!
                notifyDataSetChanged()
                isRefreshing = false
            }
        }
    }

}