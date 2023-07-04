package com.example.notescleanarchitecture.ui.feature.notedetail.component

import android.util.Log
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import com.example.notescleanarchitecture.extension.formatDateStyle1
import com.maxkeppeker.sheets.core.models.base.UseCaseState
import com.maxkeppeler.sheets.calendar.models.CalendarConfig
import com.maxkeppeler.sheets.calendar.models.CalendarSelection
import com.maxkeppeler.sheets.calendar.models.CalendarStyle
import java.time.ZoneId

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalendarDialog(calendarState: UseCaseState, onDateSelected: (date: Long) -> Unit) {
    com.maxkeppeler.sheets.calendar.CalendarDialog(
        state = calendarState,
        config = CalendarConfig(
            yearSelection = true,
            monthSelection = true,
            style = CalendarStyle.MONTH,
        ),
        selection = CalendarSelection.Dates { newDates ->
            val zoneId = ZoneId.systemDefault()
            val zonedDateTime = newDates[0].atStartOfDay(zoneId)
            onDateSelected.invoke(zonedDateTime.toInstant().toEpochMilli())
            Log.d("tuanminh", "NoteDetailScreen: ${zonedDateTime.toInstant().toEpochMilli().formatDateStyle1()}")
        },
    )
}
