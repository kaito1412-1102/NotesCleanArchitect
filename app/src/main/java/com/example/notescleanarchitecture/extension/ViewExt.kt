package com.example.notescleanarchitecture.extension

import android.widget.NumberPicker
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.model.OptionItem
import com.example.notescleanarchitecture.R
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.Job
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.launch

fun <T> Fragment.collectLifeCycleFlow(flow: Flow<T>, collector: FlowCollector<T>) {
    viewLifecycleOwner.lifecycleScope.launch {
        viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
            flow.collect(collector)
        }
    }
}

fun <T> Fragment.jobCollectLifeCycleFlow(flow: Flow<T>, collector: FlowCollector<T>):Job {
    return viewLifecycleOwner.lifecycleScope.launch {
        ensureActive()
        viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
            flow.collect(collector)
        }
    }
}

fun <T> Fragment.asCollectFlow(flow: Flow<T>, collector: FlowCollector<T>) {
    val job = lifecycleScope.launch {
        flow.collect(collector)
    }
    val observer = LifecycleEventObserver { _, event ->
        if (event == Lifecycle.Event.ON_DESTROY) {
            job.cancel()
        }
    }
    viewLifecycleOwner.lifecycle.removeObserver(observer)
    viewLifecycleOwner.lifecycle.addObserver(observer)
}

fun <T> Fragment.showSelectDialog(
    title: String,
    list: List<OptionItem<T>>,
    currentValue: OptionItem<T>?,
    onSelected: (selectedItem: OptionItem<T>) -> Unit
) {
    var picker: NumberPicker? = null
    val dialogView =
        MaterialAlertDialogBuilder(requireContext(), R.style.ThemeOverlay_App_MaterialAlertDialog)
            .setTitle(title)
            .setView(R.layout.fragment_selected_dialog)
            .setNegativeButton(resources.getString(R.string.cancel_button)) { _, _ ->
                // Respond to neutral button press
            }
            .setPositiveButton(resources.getString(R.string.ok_button)) { _, _ ->
                picker?.let {
                    onSelected.invoke(list[it.value])
                }

            }.show()
    picker = dialogView.findViewById(R.id.picker)
    picker?.apply {
        minValue = 0
        maxValue = list.size - 1
        displayedValues = list.map {
            it.displayName
        }.toTypedArray()
        wrapSelectorWheel = false
        value = if (list.indexOf(currentValue) == -1) {
            0
        } else {
            list.indexOf(currentValue)
        }
    }
}