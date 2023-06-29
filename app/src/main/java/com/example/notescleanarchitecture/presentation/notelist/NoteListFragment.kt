package com.example.notescleanarchitecture.presentation.notelist

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import com.example.model.Note
import com.example.notescleanarchitecture.R
import com.example.notescleanarchitecture.databinding.FragmentListBinding
import com.example.notescleanarchitecture.extension.asCollectFlow
import com.example.notescleanarchitecture.presentation.BaseFragment
import com.example.notescleanarchitecture.presentation.NoteViewModel
import com.example.notescleanarchitecture.presentation.notedetail.NoteAdapter
import com.example.notescleanarchitecture.presentation.notedetail.NoteFragment.Companion.ARG_NOTE
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NoteListFragment : BaseFragment<FragmentListBinding>(FragmentListBinding::inflate), NoteAdapter.OnNoteClickListener {

    private val viewModel: NoteViewModel by viewModels()
    private var noteAdapter = NoteAdapter(this)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        actionButton()
        collectData()
    }

    private fun actionButton() {
        binding.buttonAddNote.setOnClickListener {
            goToDetails()
        }
        binding.swipeRefreshLayout.setOnRefreshListener {
            noteAdapter.refresh()
        }
        binding.buttonSearch.setOnClickListener {
            findNavController().navigate(R.id.action_list_to_search)
        }
        binding.buttonFilter.setOnClickListener {
            findNavController().navigate(R.id.action_list_to_filter)
        }
    }

    private fun initView() {
        binding.apply {
            rvNotes.adapter = noteAdapter
        }
    }

    private fun collectData() {
        asCollectFlow(flow = viewModel.getAllNotes(), collector = {
            noteAdapter.submitData(it)
        })

        asCollectFlow(noteAdapter.loadStateFlow) {
            Log.d("tuanminh", "collectData: loading:${noteAdapter.itemCount} - ${it.append} - ${it.refresh} -${it.source}")
            /* if (noteAdapter.itemCount <= PAGE_SIZE) {
                 binding.rvNotes.scrollToPosition(0)
             }*/
            if (it.append is LoadState.NotLoading) {
                binding.swipeRefreshLayout.isRefreshing = false
            }
            binding.tvCount.text = noteAdapter.itemCount.toString()
            binding.progressBar.isVisible = it.source.append is LoadState.Loading
        }
    }

    private fun goToDetails() {
        navController.navigate(R.id.action_list_to_note)
    }

    override fun onNoteItemClick(note: Note) {
        val bundle = bundleOf(ARG_NOTE to note)
        navController.navigate(R.id.action_list_to_note, bundle)
    }
}