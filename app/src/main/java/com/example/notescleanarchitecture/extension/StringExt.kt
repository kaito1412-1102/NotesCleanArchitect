package com.example.notescleanarchitecture.extension

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun Long.formatDate(): String {
    val dateFormat = SimpleDateFormat("MMM dd yy, HH:mm:ss", Locale.getDefault())
    return dateFormat.format(Date(this))
}

fun Long.formatDateStyle1(): String {
    val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    return dateFormat.format(Date(this))
}