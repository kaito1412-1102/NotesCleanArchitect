package com.example.notescleanarchitecture.presentation.search

import android.os.Bundle
import android.view.View
import android.widget.SearchView.OnQueryTextListener
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.paging.LoadState
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
                    collectLifeCycleFlow(flow = viewModel.searchNotes(it), collector = { notes ->
                        noteAdapter.submitData(notes)
                    })
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })
        binding.rvNotes.adapter = noteAdapter
        binding.swipeRefreshLayout.isEnabled = false
        collectData()
    }

    private fun collectData() {
        collectLifeCycleFlow(noteAdapter.loadStateFlow) {
            if (it.refresh is LoadState.NotLoading) {
                binding.tvMsgNotFound.isVisible = noteAdapter.itemCount == 0
                binding.swipeRefreshLayout.isRefreshing = false
            }
            if (it.refresh is LoadState.Loading) {
                binding.swipeRefreshLayout.isRefreshing = true
            }
            binding.progressBar.isVisible = it.source.append is LoadState.Loading
        }
    }

    override fun onNoteItemClick(note: Note) {
        val bundle = bundleOf(NoteFragment.ARG_NOTE to note)
        navController.navigate(R.id.action_search_to_note, bundle)
    }
}