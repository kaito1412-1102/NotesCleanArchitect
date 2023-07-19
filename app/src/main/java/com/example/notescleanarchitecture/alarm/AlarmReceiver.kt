package com.example.notescleanarchitecture.alarm

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.example.data.ultils.Constants
import java.util.Calendar

class AlarmReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)
        Log.d("tuanminh", "onReceive: $hour - $minute")
        if (hour == Constants.HOUR_SHOW_REMINDER && minute == Constants.MINUTES_SHOW_REMINDER) {
            context?.startService(Intent(context, NotifyService::class.java))
        }
    }
}
