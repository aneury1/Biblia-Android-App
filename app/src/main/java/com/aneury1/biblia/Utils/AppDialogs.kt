package com.aneury1.biblia.Utils

import android.util.Log
import androidx.compose.runtime.Composable
import com.aneury1.biblia.Data.BibleNote
import com.aneury1.biblia.Data.insertNote
import com.aneury1.biblia.Screen.TextInputDialog

/// TBI: next to implement from various part of the code.
@Composable
fun ShowCommentDialog(
    onDismiss: () -> Unit
) {
    TextInputDialog(
        title = "Agregar comentario",
        initialText = "",
        onConfirm = { enteredText ->
            Log.d("Dialog", "User entered: $enteredText")
            onDismiss()
        },
        onDismiss = {
            onDismiss()
        })
}