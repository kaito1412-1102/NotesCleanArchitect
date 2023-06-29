package com.example.datastore

import androidx.datastore.core.DataStore
import com.example.model.DeadlineTagFilter
import com.example.model.NotesFilterSettings
import com.example.model.StatusFilter
import com.example.model.toDeadlineTagFilter
import com.example.model.toStatusFilter
import com.example.notescleanarchitecture.NotesFilterPreferences
import com.example.notescleanarchitecture.copy
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class NotesPreferenceDataSource @Inject constructor(private val notesPreferences: DataStore<NotesFilterPreferences>) {
    val notesFilterSettings = notesPreferences.data.map {
        NotesFilterSettings(
            deadlineTag = it.deadlineTag.toDeadlineTagFilter(),
            status = it.status.toStatusFilter()
        )
    }

    suspend fun saveFilter(deadlineTagFilter: DeadlineTagFilter, statusFilter: StatusFilter) {
        notesPreferences.updateData {
            it.copy {
                this.deadlineTag = deadlineTagFilter.toString()
                this.status = statusFilter.toString()
            }
        }
    }
}