package com.example.model

data class NotesFilterSettings(
    val deadlineTag: DeadlineTagFilter,
    val status: StatusFilter
)

enum class DeadlineTagFilter {
    ALL, TODAY, OVERDUE, UPCOMING
}

enum class StatusFilter {
    ALL, TODO, DONE
}

fun String.toDeadlineTagFilter(): DeadlineTagFilter {
    return when (this) {
        "TODAY" -> DeadlineTagFilter.TODAY
        "OVERDUE" -> DeadlineTagFilter.OVERDUE
        "UPCOMING" -> DeadlineTagFilter.UPCOMING
        else -> DeadlineTagFilter.ALL
    }
}

fun String.toStatusFilter(): StatusFilter {
    return when (this) {
        "TODO" -> StatusFilter.TODO
        "DONE" -> StatusFilter.DONE
        else -> StatusFilter.ALL
    }
}