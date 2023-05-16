package com.example.notescleanarchitecture.presentation

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun Long.formatDate(): String {
    val dateFormat = SimpleDateFormat("MMM dd yy, HH:mm:ss", Locale.getDefault())
    return dateFormat.format(Date(this))
}