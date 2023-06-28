package com.example.notescleanarchitecture.extension

import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
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