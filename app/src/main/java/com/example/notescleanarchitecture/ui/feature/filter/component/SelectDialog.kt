package com.example.notescleanarchitecture.ui.feature.filter.component

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.AlertDialogDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.OptionItem
import com.example.notescleanarchitecture.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <T> ShowSelectDialog(
    openDialog: MutableState<Boolean>,
    title: String,
    listOption: List<OptionItem<T>>,
    currentOption:  MutableState<OptionItem<T>>,
    selectedOption: (OptionItem<T>) -> Unit
) {

    if (openDialog.value) {
        AlertDialog(onDismissRequest = {
            openDialog.value = false
        }) {
            Surface(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(), shape = MaterialTheme.shapes.large, tonalElevation = AlertDialogDefaults.TonalElevation
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = title,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                    Spacer(modifier = Modifier.height(10.dp))

                    listOption.forEach {option->
//                        Log.d("tuanminh", "ShowSelectDialog: ${currentOption.value.value}- ${option.value}")
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Checkbox(checked = currentOption.value.value == option.value, onCheckedChange = { checkValue ->
                                if (checkValue) {
                                    currentOption.value = option
                                }
                            })
                            Text(text = option.displayName, modifier = Modifier.padding(start = 10.dp))
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))
                    Row(
                        modifier = Modifier.align(Alignment.End)
                    ) {
                        TextButton(
                            onClick = {
                                openDialog.value = false
                            },
                        ) {
                            Text(text = stringResource(id = R.string.cancel_button))
                        }
                        Spacer(modifier = Modifier.width(20.dp))
                        TextButton(
                            onClick = {
                                selectedOption.invoke(currentOption.value)
                                openDialog.value = false
                            },
                        ) {
                            Text(text = stringResource(id = R.string.ok_button))
                        }
                    }
                }
            }
        }
    }
}