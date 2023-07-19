package com.example.notescleanarchitecture.alarm

import android.Manifest
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.content.pm.PackageManager
import android.os.IBinder
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.data.repository.NoteRepository
import com.example.domain.R
import com.example.model.DeadlineTagFilter
import com.example.model.Note
import com.example.model.StatusFilter
import com.example.notescleanarchitecture.presentation.MainActivity
import com.example.notescleanarchitecture.utils.Constants
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@AndroidEntryPoint
class NotifyService : Service() {
    @Inject
    lateinit var repository: NoteRepository

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d("tuanminh", "onStartCommand: ")
        CoroutineScope(Dispatchers.IO).launch {
            val notes = repository.getAllNoteForNotification(deadlineTagFilter = DeadlineTagFilter.TODAY, statusFilter = StatusFilter.TODO)
            withContext(Dispatchers.Main) {
                notes.forEach { note ->
                    showNotification(note)
                }
            }
        }
        return START_NOT_STICKY
    }

    private fun showNotification(note: Note) {
        val notificationIntent = Intent(applicationContext, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            putExtra(Constants.ARG_NOTE, note) // Thêm thông tin cần truyền vào intent
        }
        val pendingIntent = PendingIntent.getActivity(applicationContext, note.id.toInt(), notificationIntent, PendingIntent.FLAG_IMMUTABLE)

        val builder = NotificationCompat.Builder(applicationContext, Constants.CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_note)
            .setContentTitle(note.title)
            .setContentText("This note will expire today")
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        with(NotificationManagerCompat.from(applicationContext)) {
            // notificationId is a unique int for each notification that you must define
            if (ActivityCompat.checkSelfPermission(applicationContext, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED) {
                notify(note.id.toInt(), builder.build())
                return
            } else {
                Log.d("tuanminh", "no notification permission")
            }
        }
    }
}