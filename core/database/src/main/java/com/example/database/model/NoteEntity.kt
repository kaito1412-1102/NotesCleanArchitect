package com.example.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.model.Note
import com.example.model.toStatus

@Entity(tableName = "note")
data class NoteEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,

    val title: String,
    val content: String,

    @ColumnInfo(name = "creation_date")
    val creationTime: Long,

    @ColumnInfo(name = "update_time")
    val updateTime: Long,

    @ColumnInfo(name = "deadline_time")
    val deadline: Long,

    @ColumnInfo(name = "status")
    val status: String,
) {
    companion object {
        fun fromNote(note: Note) = NoteEntity(
            id = note.id,
            title = note.title,
            content = note.content,
            creationTime = note.creationTime,
            updateTime = note.updateTime,
            deadline = note.deadline,
            status = note.status.toString()
        )

    }

    fun toNote() = Note(
        id = id,
        title = title,
        content = content,
        creationTime = creationTime,
        updateTime = updateTime,
        deadline = deadline,
        status = status.toStatus()
    )
}