package com.example.notescleanarchitecture.worker

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log

class NotifyService : Service() {
    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
        NotifyNote.initialize(applicationContext)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d("tuanminh", "onStartCommand: ")
        return START_STICKY
    }
}