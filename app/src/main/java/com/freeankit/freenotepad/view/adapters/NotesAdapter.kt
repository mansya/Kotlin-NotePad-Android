package com.freeankit.freenotepad.view.adapters

import android.content.Context
import android.support.v4.view.ViewCompat
import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.freeankit.freenotepad.R
import com.freeankit.freenotepad.helper.layoutInflator
import com.freeankit.freenotepad.model.Note
import kotlinx.android.synthetic.main.item_note.view.*

/**
 * @author Ankit Kumar (ankitdroiddeveloper@gmail.com) on 20/12/2017 (MM/DD/YYYY )
 */
class NotesAdapter(private val context: Context, private val listener: OnPlaceClickListener, private val isCircled: Boolean) : RecyclerView.Adapter<NotesAdapter.NotesViewHolder>() {
    private var notes: MutableList<Note> = ArrayList()
    override fun onBindViewHolder(holder: NotesViewHolder, position: Int) {
        val note = notes[position]
        holder.itemView.title_text_.text = note.title
        holder.itemView.text_.text = note.text
        holder.itemView.content.setOnClickListener {
            listener.onItemClicked(note, holder.itemView.content)
        }
        ViewCompat.setTransitionName(holder.itemView.content, "robot")

        if (isCircled) {
            (holder.itemView.content as CardView).radius = 100F
            holder.itemView.title_text_.maxLines = 3
            if (note.title.isNullOrEmpty()) {
                holder.itemView.text_.visibility = View.VISIBLE
                holder.itemView.title_text_.visibility = View.GONE
            } else {
                holder.itemView.text_.visibility = View.GONE
                holder.itemView.title_text_.visibility = View.VISIBLE
            }

        } else {
            (holder.itemView.content as CardView).radius = 0F
            holder.itemView.title_text_.maxLines = 1
            holder.itemView.text_.visibility = View.VISIBLE
            holder.itemView.title_text_.visibility = View.VISIBLE
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
        return position.toLong()
    }

    fun addData(notess: List<Note>) {
        notes.clear()
        notes.addAll(notess)
        notifyDataSetChanged()
    }

    fun addNote(note: Note) {
        notes.add(note)
        notifyDataSetChanged()
    }


    interface OnPlaceClickListener {
        fun onItemClicked(project: Note, sharedView: View)
    }


}