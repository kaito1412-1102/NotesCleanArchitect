package com.example.notescleanarchitecture.presentation.notedetail

import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.view.View
import androidx.fragment.app.viewModels
import com.example.model.Note
import com.example.model.Status
import com.example.notescleanarchitecture.R
import com.example.notescleanarchitecture.databinding.FragmentNoteBinding
import com.example.notescleanarchitecture.extension.formatDate
import com.example.notescleanarchitecture.extension.formatDateStyle1
import com.example.notescleanarchitecture.presentation.NoteViewModel
import com.example.notescleanarchitecture.presentation.BaseFragment
import com.example.notescleanarchitecture.utils.Constants.INVALID_LONG_VALUE
import com.google.android.material.datepicker.MaterialDatePicker
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class NoteFragment : BaseFragment<FragmentNoteBinding>(FragmentNoteBinding::inflate) {
    private val viewModel: NoteViewModel by viewModels()
    private var currentNote: Note? = null

    private var datePickerValue: Long = System.currentTimeMillis()
    private var status = Status.TODO

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
        Log.d(TAG, "onViewCreated: ${Status.TODO}")
    }

    private fun initView() {
        currentNote?.let { note ->
            binding.apply {
                edtTitle.text = Editable.Factory.getInstance().newEditable(note.title)
                edtContent.text = Editable.Factory.getInstance().newEditable(note.content)
                buttonDelete.visibility = View.VISIBLE
                status = note.status
                if (status == Status.TODO) {
                    buttonTodo.setImageResource(R.drawable.ic_todo)
                } else {
                    buttonTodo.setImageResource(R.drawable.ic_done)
                }
                buttonTodo.visibility = View.VISIBLE
                datePickerValue = note.deadline
            }
        }
        binding.tvDeadline.text = requireContext().getString(R.string.title_deadline, datePickerValue.formatDateStyle1())
    }

    private fun actionButton() {
        binding.apply {
            buttonCheck.setOnClickListener {
                val title = edtTitle.text.toString()
                val content = edtContent.text.toString()
                if (title.isEmpty() || content.isEmpty()) {
                    showAlert(
                        title = "Add note error!",
                        message = "Please fill title, content and choose deadline for this note!",
                        positiveButtonText = "OK",
                    )
                    return@setOnClickListener
                }
                    val currentTime = System.currentTimeMillis()
                    val creationTime = currentNote?.creationTime ?: currentTime
                    val idNote = currentNote?.id ?: 0
                    viewModel.addNote(
                        Note(
                            id = idNote,
                            title = title,
                            content = content,
                            creationTime = creationTime,
                            updateTime = currentTime,
                            deadline =  datePickerValue,
                            status = status,
                        )
                    )
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

            buttonCalendar.setOnClickListener {
                val datePicker =
                    MaterialDatePicker.Builder.datePicker()
                        .setTitleText("Select date")
                        .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                        .build()
                datePicker.addOnPositiveButtonClickListener {
                    Log.d(TAG, "actionButton: $it - ${it.formatDate()}")
//                    if (validateDate(it)) {
                        datePickerValue = it
                        binding.tvDeadline.text = it.formatDateStyle1()
//                    }
                }
                datePicker.show(requireActivity().supportFragmentManager, null)
            }

            buttonTodo.setOnClickListener {
                when (status) {
                    Status.TODO -> {
                        status = Status.DONE
                        binding.buttonTodo.setImageResource(R.drawable.ic_done)
                    }

                    else -> {
                        status = Status.TODO
                        binding.buttonTodo.setImageResource(R.drawable.ic_todo)
                    }
                }
            }
        }
    }

    private fun validateDate(it: Long?): Boolean {
        val currentDate = System.currentTimeMillis()
        if ((it ?: INVALID_LONG_VALUE) <= currentDate) {
            showAlert(
                title = "Date Invalid!",
                message = "Please select a date > ${currentDate.formatDateStyle1()}",
                positiveButtonText = "OK",
            )
            return false
        }
        return true
    }

    companion object {
        const val ARG_NOTE = "ARG_NOTE"
        const val TAG = "NoteFragment"
    }
}