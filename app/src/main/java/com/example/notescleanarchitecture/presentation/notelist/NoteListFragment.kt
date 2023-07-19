package com.example.notescleanarchitecture.presentation.notelist

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import com.example.model.Note
import com.example.notescleanarchitecture.R
import com.example.notescleanarchitecture.databinding.FragmentListBinding
import com.example.notescleanarchitecture.extension.asCollectFlow
import com.example.notescleanarchitecture.extension.collectLifeCycleFlow
import com.example.notescleanarchitecture.presentation.BaseFragment
import com.example.notescleanarchitecture.presentation.NoteViewModel
import com.example.notescleanarchitecture.utils.Constants.ARG_NOTE
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NoteListFragment : BaseFragment<FragmentListBinding>(FragmentListBinding::inflate), NoteAdapter.OnNoteClickListener {

    private val viewModel: NoteViewModel by activityViewModels()
    private var noteAdapter = NoteAdapter(this)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val intent = requireActivity().intent
        val note = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra(ARG_NOTE, Note::class.java)
        } else {
            intent.getParcelableExtra(ARG_NOTE)
        }

        if (note != null) {
            intent.putExtra(ARG_NOTE, null as Note?)
            val bundle = bundleOf(ARG_NOTE to note)
            navController.navigate(R.id.action_list_to_note, bundle)
        } else {
            initView()
            actionButton()
            collectData()
        }
    }

    private fun startNoteDetailFromNotification() {
        val intent = requireActivity().intent
        val note = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra(ARG_NOTE, Note::class.java)
        } else {
            intent.getParcelableExtra(ARG_NOTE)
        }
        note?.let {
            val bundle = bundleOf(ARG_NOTE to it)
            navController.navigate(R.id.action_list_to_note, bundle)
        }
        Log.d("tuanminh", "onViewCreated: $note")
    }

    private fun actionButton() {
        binding?.buttonAddNote?.setOnClickListener {
            goToDetails()
        }
        binding?.swipeRefreshLayout?.setOnRefreshListener {
            noteAdapter.refresh()
        }
        binding?.buttonSearch?.setOnClickListener {
            findNavController().navigate(R.id.action_list_to_search)
        }
        binding?.buttonFilter?.setOnClickListener {
            findNavController().navigate(R.id.action_list_to_filter)
        }
    }

    private fun initView() {
        binding?.apply {
            rvNotes.adapter = noteAdapter
        }
    }

    private fun collectData() {
        asCollectFlow(flow = viewModel.uiState, collector = {
            when(it){
                NoteViewModel.NoteUiState.FilterSettingsApply -> collectNotes()
            }
        })

        asCollectFlow(noteAdapter.loadStateFlow) {
//            Log.d("tuanminh", "collectData: loading:${noteAdapter.itemCount} - ${it.append} - ${it.refresh} -${it.source}")
            if (it.append is LoadState.NotLoading) {
                binding?.swipeRefreshLayout?.isRefreshing = false
            }
            binding?.tvCount?.text = noteAdapter.itemCount.toString()
            binding?.progressBar?.isVisible = it.source.append is LoadState.Loading
        }
        viewModel.init()
    }

    private fun collectNotes() {
        collectLifeCycleFlow(flow = viewModel.getAllNotes(), collector = {
            noteAdapter.submitData(it)
        })
    }

    private fun goToDetails() {
        navController.navigate(R.id.action_list_to_note)
    }

    override fun onNoteItemClick(note: Note) {
        val bundle = bundleOf(ARG_NOTE to note)
        navController.navigate(R.id.action_list_to_note, bundle)
    }
}