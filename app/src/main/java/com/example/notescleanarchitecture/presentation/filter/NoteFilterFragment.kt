package com.example.notescleanarchitecture.presentation.filter

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.example.notescleanarchitecture.databinding.FragmentNoteFilterBinding
import com.example.notescleanarchitecture.extension.asCollectFlow
import com.example.notescleanarchitecture.presentation.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NoteFilterFragment : BaseFragment<FragmentNoteFilterBinding>(FragmentNoteFilterBinding::inflate) {
    private val viewModel: NoteFilterViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        collectData()
    }

    private fun initView() {
        binding.apply {
            tvTypeDeadLineTag.set
        }
    }

    private fun collectData() {
        asCollectFlow(viewModel.uiState) {

        }
    }
}