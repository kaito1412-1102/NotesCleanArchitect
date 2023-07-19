package com.example.notescleanarchitecture.presentation

import android.Manifest
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.data.ultils.Constants
import com.example.notescleanarchitecture.R
import com.example.notescleanarchitecture.alarm.AlarmReceiver
import com.example.notescleanarchitecture.extension.showAlert
import com.example.notescleanarchitecture.alarm.NotifyService
import dagger.hilt.android.AndroidEntryPoint
import java.util.Calendar

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (Build.VERSION.SDK_INT >= 33) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED) {
//                startService(Intent(this, NotifyService::class.java))
                setupAlarmService()
            } else if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.POST_NOTIFICATIONS)) {
                showAlert(
                    title = getString(R.string.title_permission_requested),
                    message = getString(R.string.message_notification_permission_requested),
                    positiveButtonText = getString(R.string.ok_button),
                    onPositiveButtonClick = {
                        requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                    }
                )
            } else {
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        } else {
            setupAlarmService()
        }
    }

    private fun setupAlarmService() {
        val alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager

        val intent = Intent(this, AlarmReceiver::class.java)

        val pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_IMMUTABLE)

        val calendar: Calendar = Calendar.getInstance()
        calendar.timeInMillis = System.currentTimeMillis()
        calendar.set(Calendar.HOUR_OF_DAY, Constants.HOUR_SHOW_REMINDER) // Giờ để kích hoạt thông báo

        calendar.set(Calendar.MINUTE, Constants.MINUTES_SHOW_REMINDER) // Phút để kích hoạt thông báo
        calendar.set(Calendar.SECOND, Constants.SECOND_SHOW_REMINDER) // Giây để kích hoạt thông báo

        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, AlarmManager.INTERVAL_DAY, pendingIntent);
        alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC, calendar.timeInMillis, pendingIntent)
    }

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (isGranted) {
                Log.d("tuanminh", "Notification permission is granted")
                startService(Intent(this, NotifyService::class.java))
            } else {
                Log.d("tuanminh", "Notification permission is not granted")
                showAlert(
                    title = getString(R.string.title_permission_requested),
                    message = getString(R.string.message_notification_permission_requested),
                    positiveButtonText = getString(R.string.ok_button),
                    onPositiveButtonClick = {
                        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                        val uri = Uri.fromParts("package", packageName, null)
                        intent.data = uri
                        startActivity(intent)
                    }
                )
            }
        }
}