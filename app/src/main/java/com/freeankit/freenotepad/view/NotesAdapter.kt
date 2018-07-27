package com.freeankit.freenotepad.view

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.StaggeredGridLayoutManager
import android.view.View
import android.view.ViewGroup
import com.freeankit.freenotepad.R
import com.freeankit.freenotepad.helper.layoutInflator
import com.freeankit.freenotepad.model.Note
import kotlinx.android.synthetic.main.item_note.view.*

/**
 * @author Ankit Kumar (ankitdroiddeveloper@gmail.com) on 20/12/2017 (MM/DD/YYYY )
 */
class NotesAdapter(private val context: Context, private val listener: OnPlaceClickListener, val layoutManager: RecyclerView.LayoutManager) : RecyclerView.Adapter<NotesAdapter.NotesViewHolder>() {
    private var notes: MutableList<Note> = ArrayList()
    override fun onBindViewHolder(holder: NotesViewHolder, position: Int) {
        val note = notes[position]
        holder.itemView.title_text_.text = note.title
        holder.itemView.text_.text = note.text
        holder.itemView.content.setOnClickListener {
            listener.onItemClicked(note)
        }

//        //rlm is RecyclerView.LayoutManager passed in constructor or setter in adapter
//        if (layoutManager is StaggeredGridLayoutManager) {
//            val layoutParams = holder.itemView.layoutParams as StaggeredGridLayoutManager.LayoutParams
//            /*
//     * to make View to occupy full width of the parent
//     */
//            layoutParams.isFullSpan = true
//        }

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
        fun onItemClicked(project: Note)
    }


}