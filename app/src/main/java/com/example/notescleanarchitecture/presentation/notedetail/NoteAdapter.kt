package com.example.notescleanarchitecture.presentation.notedetail

import android.R.color
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.model.Note
import com.example.model.toDeadlineTag
import com.example.notescleanarchitecture.R
import com.example.notescleanarchitecture.databinding.ItemNoteBinding
import com.example.notescleanarchitecture.extension.formatDate
import com.example.notescleanarchitecture.extension.formatDateStyle1
import com.example.notescleanarchitecture.utils.Constants


class NoteAdapter(private val listener: OnNoteClickListener) : PagingDataAdapter<Note, NoteAdapter.NoteViewHolder>(DataComparator) {
//    private val listNote = mutableListOf<Note>()
//
//    @SuppressLint("NotifyDataSetChanged")
//    fun setListNotes(notes: List<Note>) {
//        Log.d(TAG, "setListNotes: $notes")
//        listNote.clear()
//        listNote.addAll(notes)
//        notifyDataSetChanged()
//    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        return NoteViewHolder(ItemNoteBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

//    override fun getItemCount(): Int = listNote.size

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class NoteViewHolder(private val binding: ItemNoteBinding) : ViewHolder(binding.root) {
        init {
            binding.root.setOnClickListener {
                getItem(bindingAdapterPosition)?.let { note ->
                    listener.onNoteItemClick(note)
                }
            }
        }

        fun bind(note: Note?) {
            binding.apply {
                title.text = note?.title
                content.text = note?.content
                lastUpdate.text = root.context.getString(R.string.title_last_update, note?.creationTime?.formatDate())
                deadLine.text = root.context.getString(R.string.title_deadline, note?.deadline?.formatDateStyle1())
                status.text = note?.status.toString()

                status.background.setTint(root.context.getColor(note?.status?.color ?: R.color.purple_200))

                val currentTime = System.currentTimeMillis()
                val deadLine = note?.deadline ?: Constants.INVALID_LONG_VALUE

                val noteDeadLineTag = if (deadLine.formatDateStyle1() == currentTime.formatDateStyle1()) {
                    "Today".toDeadlineTag()
                } else if (deadLine < currentTime) {
                    "Overdue".toDeadlineTag()
                } else {
                    "Upcoming".toDeadlineTag()
                }
                deadlineTag.text = noteDeadLineTag.title
                deadlineTag.background.setTint(root.context.getColor(noteDeadLineTag.colorTag))
                noteLayout.background.setTint(root.context.getColor(noteDeadLineTag.bgColor))
            }

        }
    }

    object DataComparator : DiffUtil.ItemCallback<Note>() {
        override fun areItemsTheSame(oldItem: Note, newItem: Note): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Note, newItem: Note): Boolean {
            return oldItem == newItem
        }
    }

    fun interface OnNoteClickListener {
        fun onNoteItemClick(note: Note)
    }

    companion object {
        const val TAG = "NoteAdapter"
    }
}