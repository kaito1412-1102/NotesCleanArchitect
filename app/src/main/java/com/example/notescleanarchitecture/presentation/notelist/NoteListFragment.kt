package com.example.notescleanarchitecture.presentation.notelist

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.paging.LoadState
import com.example.model.Note
import com.example.notescleanarchitecture.R
import com.example.notescleanarchitecture.databinding.FragmentListBinding
import com.example.notescleanarchitecture.framework.NoteViewModel
import com.example.notescleanarchitecture.presentation.BaseFragment
import com.example.notescleanarchitecture.presentation.notedetail.NoteAdapter
import com.example.notescleanarchitecture.presentation.notedetail.NoteFragment.Companion.ARG_NOTE
import com.example.notescleanarchitecture.presentation.collectLifeCycleFlow
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NoteListFragment : BaseFragment<FragmentListBinding>(FragmentListBinding::inflate), NoteAdapter.OnNoteClickListener {

    private val viewModel: NoteViewModel by activityViewModels()
    private var noteAdapter = NoteAdapter(this)

    private var isInit = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        actionButton()
        Log.d("tuanminh", "onViewCreated: ")
        collectData()
    }

    private fun actionButton() {
        binding.buttonAddNote.setOnClickListener {
            goToDetails()
        }
        binding.swipeRefreshLayout.setOnRefreshListener {
            noteAdapter.refresh()
        }
    }

    private fun initView() {
        binding.apply {
            rvNotes.adapter = noteAdapter
        }
    }

    private fun collectData() {
        collectLifeCycleFlow(flow = viewModel.noteUiState, collector = {
            when (it) {
                NoteViewModel.NoteUiState.Loading -> {
                    binding.swipeRefreshLayout.isRefreshing = true
                    Log.d("tuanminh", "collectData: Loading")
                }

                is NoteViewModel.NoteUiState.GetNoteSuccess -> {}
                is NoteViewModel.NoteUiState.GetNotesSuccess -> {
                    Log.d("tuanminh", "collectData: success")
                    binding.swipeRefreshLayout.isRefreshing = false
                    noteAdapter.submitData(it.notes)
                }
            }
        })
        collectLifeCycleFlow(noteAdapter.loadStateFlow) {
            binding.progressBar.isVisible = it.source.append is LoadState.Loading
        }
        if(!isInit){
            viewModel.getAllNotes()
            isInit = true
        }
    }

    private fun goToDetails(id: Long = 0L) {
        navController.navigate(R.id.action_list_to_note)
    }

    override fun onNoteItemClick(note: Note) {
        val bundle = bundleOf(ARG_NOTE to note)
        navController.navigate(R.id.action_list_to_note, bundle)

    }
}