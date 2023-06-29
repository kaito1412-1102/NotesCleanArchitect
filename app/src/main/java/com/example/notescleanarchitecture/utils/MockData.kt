package com.example.notescleanarchitecture.utils

import android.content.Context
import com.example.model.DeadlineTagFilter
import com.example.model.OptionItem
import com.example.model.StatusFilter
import com.example.notescleanarchitecture.R

object MockData {
    fun deadlineTagFilterOptions(context: Context): List<OptionItem<DeadlineTagFilter>> {
        return listOf(
            OptionItem(displayName = context.getString(R.string.deadline_tag_all_type), value = DeadlineTagFilter.ALL),
            OptionItem(displayName = context.getString(R.string.deadline_tag_today_type), value = DeadlineTagFilter.TODAY),
            OptionItem(displayName = context.getString(R.string.deadline_tag_upcoming_type), value = DeadlineTagFilter.UPCOMING),
            OptionItem(displayName = context.getString(R.string.deadline_tag_overdue_type), value = DeadlineTagFilter.OVERDUE),
            )
    }

    fun statusFilterOptions(context: Context): List<OptionItem<StatusFilter>> {
        return listOf(
            OptionItem(displayName = context.getString(R.string.status_all_type), value = StatusFilter.ALL),
            OptionItem(displayName = context.getString(R.string.status_todo_type), value = StatusFilter.TODO),
            OptionItem(displayName = context.getString(R.string.status_done_type), value = StatusFilter.DONE),
        )
    }
}