package com.example.notescleanarchitecture.presentation.notedetail

import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.view.View
import androidx.fragment.app.viewModels
import com.example.model.Note
import com.example.notescleanarchitecture.databinding.FragmentNoteBinding
import com.example.notescleanarchitecture.framework.NoteViewModel
import com.example.notescleanarchitecture.presentation.BaseFragment
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class NoteFragment : BaseFragment<FragmentNoteBinding>(FragmentNoteBinding::inflate) {
    private val viewModel: NoteViewModel by viewModels()
    private var currentNote: Note? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.let {
            currentNote = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                it.getParcelable(ARG_NOTE, Note::class.java)
            } else {
                it.getParcelable(ARG_NOTE)
            }
            Log.d(TAG, "onViewCreated: $currentNote")
        }
        initView()
        actionButton()
    }

    private fun initView() {
        currentNote?.let { note ->
            binding.apply {
                edtTitle.text = Editable.Factory.getInstance().newEditable(note.title)
                edtContent.text = Editable.Factory.getInstance().newEditable(note.content)
                buttonDelete.visibility = View.VISIBLE
            }
        }
    }

    private fun actionButton() {
        binding.apply {
            buttonCheck.setOnClickListener {
                val title = edtTitle.text.toString()
                val content = edtContent.text.toString()
                val deadLine =
               /* if (title.isNotEmpty() && content.isNotEmpty()) {
                    val currentTime = System.currentTimeMillis()
                    val creationTime = currentNote?.creationTime ?: currentTime
                    val idNote = currentNote?.id ?: 0
                    viewModel.addNote(
                        Note(
                            id = idNote,
                            title = title,
                            content = content,
                            creationTime = creationTime,
                            updateTime = currentTime
                        )
                    )
                }*/
                navController.popBackStack()
            }

            buttonDelete.setOnClickListener {
                currentNote?.let { note ->
                    showAlert(
                        title = "Delete Note?",
                        message = "Do you want to delete this note?",
                        negativeButtonText = "No",
                        positiveButtonText = "Yes",
                        onPositiveButtonClick = {
                            viewModel.removeNote(note)
                            navController.popBackStack()
                        }
                    )
                }
            }
        }
    }

    companion object {
        const val ARG_NOTE = "ARG_NOTE"
        const val TAG = "NoteFragment"
    }
}