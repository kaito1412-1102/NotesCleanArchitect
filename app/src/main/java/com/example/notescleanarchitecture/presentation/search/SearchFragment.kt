package com.example.notescleanarchitecture.presentation.search

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.SearchView.OnQueryTextListener
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import com.example.model.Note
import com.example.notescleanarchitecture.R
import com.example.notescleanarchitecture.databinding.FragmentSearchBinding
import com.example.notescleanarchitecture.extension.collectLifeCycleFlow
import com.example.notescleanarchitecture.presentation.BaseFragment
import com.example.notescleanarchitecture.presentation.NoteViewModel
import com.example.notescleanarchitecture.presentation.notedetail.NoteAdapter
import com.example.notescleanarchitecture.presentation.notedetail.NoteFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchFragment : BaseFragment<FragmentSearchBinding>(FragmentSearchBinding::inflate), NoteAdapter.OnNoteClickListener {

    private var noteAdapter = NoteAdapter(this)
    private val viewModel: NoteViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.searchView.setOnQueryTextListener(object : OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let {
                    viewModel.searchNotes(it)
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })
        binding.rvNotes.adapter = noteAdapter
        collectData()
        binding.buttonTest.setOnClickListener {
            Log.d("tuanminh", "collectData: success ${noteAdapter.snapshot().items.size}")
        }
    }

    private fun collectData() {
        collectLifeCycleFlow(flow = viewModel.noteUiState, collector = {
            when (it) {
                NoteViewModel.NoteUiState.Loading ->{
                    binding.swipeRefreshLayout.isRefreshing = true
                }
                is NoteViewModel.NoteUiState.SearchNotesSuccess -> {
                    binding.swipeRefreshLayout.isRefreshing = false
                    noteAdapter.submitData(it.notes)
                }

                else -> {}
            }
        })
    }

    override fun onNoteItemClick(note: Note) {
        val bundle = bundleOf(NoteFragment.ARG_NOTE to note)
        navController.navigate(R.id.action_search_to_note, bundle)
    }
}