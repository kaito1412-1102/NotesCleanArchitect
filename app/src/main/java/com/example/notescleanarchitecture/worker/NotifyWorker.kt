package com.example.notescleanarchitecture.worker

import android.Manifest
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.hilt.work.HiltWorker
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.data.repository.NoteRepository
import com.example.domain.R
import com.example.model.DeadlineTagFilter
import com.example.model.Note
import com.example.model.StatusFilter
import com.example.notescleanarchitecture.presentation.MainActivity
import com.example.notescleanarchitecture.presentation.notedetail.NoteFragment.Companion.ARG_NOTE
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import java.util.Calendar
import java.util.concurrent.TimeUnit

@HiltWorker
class NotifyWorker @AssistedInject constructor(
    @Assisted private val appContext: Context,
    @Assisted workerParameters: WorkerParameters,
    private val repository: NoteRepository,
) : Worker(appContext, workerParameters) {

    override fun doWork(): Result {
        Log.d("tuanminh", "doWork: ")
        val notes = repository.getAllNoteForNotification(deadlineTagFilter = DeadlineTagFilter.TODAY, statusFilter = StatusFilter.TODO)
        notes.forEach { note ->
            showNotification(note)
        }
        return Result.success()
    }

    private fun showNotification(note: Note) {
        val notificationIntent = Intent(appContext, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            putExtra(ARG_NOTE, note) // Thêm thông tin cần truyền vào intent
        }
        val pendingIntent = PendingIntent.getActivity(appContext, note.id.toInt(), notificationIntent, PendingIntent.FLAG_IMMUTABLE)

        val builder = NotificationCompat.Builder(appContext, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_note)
            .setContentTitle(note.title)
            .setContentText("This note will expire today")
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        with(NotificationManagerCompat.from(appContext)) {
            // notificationId is a unique int for each notification that you must define
            if (ActivityCompat.checkSelfPermission(appContext, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED) {
//                Log.d("tuanminh", "showNotification: ")
                notify(note.id.toInt(), builder.build())
                return
            } else {
                Log.d("tuanminh", "no notification permission")
            }
        }
    }


    companion object {
        const val CHANNEL_ID = "CHANNEL_ID"

        //        fun startNotifyWork() = OneTimeWorkRequestBuilder<NotifyWorker>().build()
        fun startNotifyWork() = PeriodicWorkRequestBuilder<NotifyWorker>(1, TimeUnit.DAYS)
            .setInitialDelay(calculateInitialDelay(), TimeUnit.MILLISECONDS)
            .build()

        private fun calculateInitialDelay(): Long {
            val currentTime = System.currentTimeMillis()
            val calendar = Calendar.getInstance().apply {
                timeInMillis = currentTime
                set(Calendar.HOUR_OF_DAY, 9)
                set(Calendar.MINUTE, 0)
                set(Calendar.SECOND, 0)
                set(Calendar.MILLISECOND, 0)
            }
            val targetTime = calendar.timeInMillis

            val delayTime = if (targetTime <= currentTime) {
                targetTime + TimeUnit.DAYS.toMillis(1) - currentTime
            } else {
                targetTime - currentTime
            }
            Log.d("tuanminh", "calculateInitialDelay: $delayTime")
            return delayTime
        }
    }
}