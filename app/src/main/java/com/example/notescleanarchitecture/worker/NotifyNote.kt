package com.example.notescleanarchitecture.worker

import android.content.Context
import android.util.Log
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.WorkManager

object NotifyNote {
    fun initialize(context: Context) {
        WorkManager.getInstance(context).apply {
            Log.d("tuanminh", "NotifyNote initialize: ")
//            enqueueUniquePeriodicWork("daily_notification", ExistingPeriodicWorkPolicy.UPDATE, NotifyWorker.startNotifyWork())
            enqueue(NotifyWorker.startNotifyWork())
        }
    }
}