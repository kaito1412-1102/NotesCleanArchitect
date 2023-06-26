package com.example.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Note(
    val id: Long = 0,
    val title: String,
    val content: String,
    val creationTime: Long,
    val updateTime: Long,
    val deadline: Long,
    val status: Status
) : Parcelable

enum class DeadlineTag(val title: String, val colorTag: Int, val bgColor: Int) {
    TODAY("Today", R.color.deadline_tag_today, R.color.bg_note_today),
    UPCOMING("Upcoming", R.color.deadline_tag_upcoming, R.color.bg_note_upcoming),
    OVERDUE("Overdue", R.color.deadline_tag_overdue, R.color.bg_note_overdue)
}

fun String.toDeadlineTag(): DeadlineTag {
    return when (this) {
        "Today" -> DeadlineTag.TODAY
        "Upcoming" -> DeadlineTag.UPCOMING
        else -> DeadlineTag.OVERDUE
    }
}

enum class Status(val color: Int) {
    DONE(R.color.status_done),
    TODO(R.color.status_todo)
}

fun String.toStatus(): Status {
    return when (this) {
        "DONE" -> Status.DONE
        else -> Status.TODO
    }
}
