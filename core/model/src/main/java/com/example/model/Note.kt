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
    val deadlineTag: DeadlineTag,
    val status: Status
) : Parcelable

enum class DeadlineTag {
    TODAY, UPCOMING, OVERDUE
}

enum class Status {
    DONE, TODO
}