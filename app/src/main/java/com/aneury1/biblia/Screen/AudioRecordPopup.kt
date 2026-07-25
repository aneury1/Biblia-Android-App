package com.aneury1.biblia.Screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog

@Composable
fun AudioRecordPopup(
    isVisible: Boolean,
    onDismiss: () -> Unit,
    onStartRecording: () -> Unit,
    onStopRecording: () -> Unit
) {
    if (!isVisible) return

    Dialog(onDismissRequest = onDismiss) {
        Column(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.surface)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                text = "Record Audio",
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row {
                Button(onClick = onStartRecording) {
                    Text("Start")
                }

                Spacer(modifier = Modifier.width(8.dp))

                Button(onClick = onStopRecording) {
                    Text("Stop")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(onClick = onDismiss) {
                Text("Close")
            }
        }
    }
}