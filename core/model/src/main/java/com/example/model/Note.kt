package com.example.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Note(
    val title: String,
    val content: String,
    val creationTime: Long,
    val updateTime: Long,
    val id: Long = 0
) : Parcelable