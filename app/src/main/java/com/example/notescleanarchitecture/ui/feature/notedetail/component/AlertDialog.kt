package com.example.notescleanarchitecture.ui.feature.notedetail.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.AlertDialogDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.notescleanarchitecture.R

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun CustomAlertDialog(
    openDialog: MutableState<Boolean>,
    title: String, msg: String,
    positiveButtonText: String = stringResource(id = R.string.ok_button),
    negativeButtonText: String? = null,
    positiveButtonClick: (() -> Unit)? = null,
    negativeButtonClick: (() -> Unit)? = null
) {
    // Create a mutable state to hold the visibility of the dialog
    if (openDialog.value) {

        AlertDialog(
            onDismissRequest = {
                // Dismiss the dialog when the user clicks outside the dialog or on the back
                // button. If you want to disable that functionality, simply use an empty
                // onDismissRequest.
                openDialog.value = false
            }
        ) {
            Surface(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .fillMaxWidth()
                    .wrapContentHeight(),
                shape = MaterialTheme.shapes.large,
                tonalElevation = AlertDialogDefaults.TonalElevation
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = title,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = msg,
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                    Row(
                        modifier = Modifier.align(Alignment.End)
                    ) {
                        negativeButtonText?.let {
                            TextButton(
                                onClick = {
                                    openDialog.value = false
                                    negativeButtonClick?.invoke()
                                },
                            ) {
                                Text(it)
                            }
                            Spacer(modifier = Modifier.width(20.dp))
                        }
                        TextButton(
                            onClick = {
                                openDialog.value = false
                                positiveButtonClick?.invoke()
                            },
                        ) {
                            Text(positiveButtonText)
                        }
                    }
                }
            }
        }
    }
}