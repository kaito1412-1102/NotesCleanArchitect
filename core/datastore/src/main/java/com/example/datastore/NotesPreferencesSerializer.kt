package com.example.datastore

import androidx.datastore.core.CorruptionException
import androidx.datastore.core.Serializer
import androidx.datastore.preferences.protobuf.InvalidProtocolBufferException
import com.example.notescleanarchitecture.NotesFilterPreferences
import java.io.InputStream
import java.io.OutputStream
import javax.inject.Inject

class NotesPreferencesSerializer @Inject constructor() : Serializer<NotesFilterPreferences>{
    override val defaultValue: NotesFilterPreferences
        get() = NotesFilterPreferences.getDefaultInstance()

    override suspend fun readFrom(input: InputStream): NotesFilterPreferences {
        try {
            // readFrom is already called on the data store background thread
            return NotesFilterPreferences.parseFrom(input)
        } catch (exception: InvalidProtocolBufferException) {
            throw CorruptionException("Cannot read proto.", exception)
        }
    }

    override suspend fun writeTo(t: NotesFilterPreferences, output: OutputStream) {
        // writeTo is already called on the data store background thread
        t.writeTo(output)
    }
}