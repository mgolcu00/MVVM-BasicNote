package com.mertgolcu.basicnote.ui.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.mertgolcu.basicnote.data.Note
import com.mertgolcu.basicnote.databinding.ItemNoteBinding

class NoteAdapter(private val listener: OnItemClickListener) :
    PagingDataAdapter<Note, NoteAdapter.NoteViewHolder>(NOTE_COMPARATOR) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val binding = ItemNoteBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NoteViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val currentItem = getItem(position)
        if (currentItem != null) {
            holder.bind(currentItem)
        }
    }

    fun noteWithIndex(pos: Int): Note {
        return getItem(pos)!!
    }


    inner class NoteViewHolder(private val binding: ItemNoteBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                val pos = bindingAdapterPosition
                if (pos != RecyclerView.NO_POSITION) {
                    val item = getItem(pos)
                    if (item != null) {
                        listener.onItemClick(item)
                    }
                }
            }
        }

        fun bind(note: Note) {
            binding.apply {
                textViewNoteTitle.text = note.title
                textViewNoteText.text = note.note
            }
        }

    }


    interface OnItemClickListener {
        fun onItemClick(note: Note)
    }

    companion object {
        private val NOTE_COMPARATOR = object : DiffUtil.ItemCallback<Note>() {
            override fun areItemsTheSame(oldItem: Note, newItem: Note): Boolean =
                oldItem.id == newItem.id


            override fun areContentsTheSame(oldItem: Note, newItem: Note): Boolean =
                oldItem == newItem

        }
    }
}