package com.example.notescleanarchitecture.presentation.filter

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.model.DeadlineTagFilter
import com.example.model.OptionItem
import com.example.model.StatusFilter
import com.example.notescleanarchitecture.R
import com.example.notescleanarchitecture.databinding.FragmentNoteFilterBinding
import com.example.notescleanarchitecture.extension.asCollectFlow
import com.example.notescleanarchitecture.extension.showSelectDialog
import com.example.notescleanarchitecture.presentation.BaseFragment
import com.example.notescleanarchitecture.utils.MockData
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NoteFilterFragment : BaseFragment<FragmentNoteFilterBinding>(FragmentNoteFilterBinding::inflate) {
    private val viewModel: NoteFilterViewModel by viewModels()

    private var currentDeadlineTagFilter = DeadlineTagFilter.ALL
    private var currentStatusFilter = StatusFilter.ALL

    private var deadlineTagFilterOptions = listOf<OptionItem<DeadlineTagFilter>>()
    private var statusFilterOptions = listOf<OptionItem<StatusFilter>>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        collectData()
    }

    private fun initView() {
        deadlineTagFilterOptions = MockData.deadlineTagFilterOptions(requireContext())
        statusFilterOptions = MockData.statusFilterOptions(requireContext())

        binding?.apply {
            tvTypeDeadLineTag.setOnClickListener {
                val currentOption = deadlineTagFilterOptions.firstOrNull {
                    it.value == currentDeadlineTagFilter
                }
                showSelectDialog(
                    getString(R.string.title_deadline_tag),
                    deadlineTagFilterOptions, currentOption ?: OptionItem(getString(R.string.deadline_tag_all_type), DeadlineTagFilter.ALL)
                ) {
                    tvTypeDeadLineTag.text = it.displayName
                    currentDeadlineTagFilter = it.value
                }
            }

            tvTypeStatus.setOnClickListener {
                val currentOption = statusFilterOptions.firstOrNull {
                    it.value == currentStatusFilter
                }
                showSelectDialog(
                    getString(R.string.title_status),
                    statusFilterOptions, currentOption ?: OptionItem(getString(R.string.status_all_type), StatusFilter.ALL)
                ) {
                    tvTypeStatus.text = it.displayName
                    currentStatusFilter = it.value
                }
            }

            btnCancel.setOnClickListener {
                findNavController().popBackStack()
            }

            btnApply.setOnClickListener {
                Log.d("tuanminh", "initView: $currentStatusFilter -$currentDeadlineTagFilter")
                viewModel.saveFilter(currentDeadlineTagFilter, currentStatusFilter)
            }
        }
    }

    private fun collectData() {
        asCollectFlow(viewModel.uiState) {
            when (it) {
                is NoteFilterViewModel.NoteFilterUiState.FilterState -> {
                    currentDeadlineTagFilter = it.deadlineTag
                    currentStatusFilter = it.status
                    updateStatusAndDeadlineTagUi()
                }

                NoteFilterViewModel.NoteFilterUiState.SaveFilterDone -> {
                    findNavController().popBackStack()
                }
            }

        }
        viewModel.getFilterSettings()
    }

    private fun updateStatusAndDeadlineTagUi() {
        val deadlineTagOption = deadlineTagFilterOptions.firstOrNull {
            it.value == currentDeadlineTagFilter
        }
        binding?.tvTypeDeadLineTag?.text = deadlineTagOption?.displayName ?: getString(R.string.deadline_tag_all_type)

        val statusOption = statusFilterOptions.firstOrNull {
            it.value == currentStatusFilter
        }
        binding?.tvTypeStatus?.text = statusOption?.displayName ?: getString(R.string.status_all_type)
    }
}