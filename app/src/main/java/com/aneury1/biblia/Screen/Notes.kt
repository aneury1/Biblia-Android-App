package com.aneury1.biblia.Screen
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.Composable

@Composable
fun AddNewNote(
    onGuardar: (
        titulo: String,
        libro: String,
        capitulo: String,
        versiculo: String,
        nota: String
    ) -> Unit = { _, _, _, _, _ -> },
    onCancelar: () -> Unit ={}
) {
    var titulo by remember { mutableStateOf("") }
    var libro by remember { mutableStateOf("") }
    var capitulo by remember { mutableStateOf("") }
    var versiculo by remember { mutableStateOf("") }
    var nota by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        Text(
            text = "Agregar Nota",
            style = MaterialTheme.typography.headlineSmall
        )

        OutlinedTextField(
            value = titulo,
            onValueChange = { titulo = it },
            label = { Text("Título") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {

            OutlinedTextField(
                value = libro,
                onValueChange = { libro = it },
                label = { Text("Libro") },
                modifier = Modifier.weight(2f),
                singleLine = true
            )

            OutlinedTextField(
                value = capitulo,
                onValueChange = { capitulo = it },
                label = { Text("Capítulo") },
                modifier = Modifier.weight(1f),
                singleLine = true
            )

            OutlinedTextField(
                value = versiculo,
                onValueChange = { versiculo = it },
                label = { Text("Versículo") },
                modifier = Modifier.weight(1f),
                singleLine = true
            )
        }

        OutlinedTextField(
            value = nota,
            onValueChange = { nota = it },
            label = { Text("Notas") },
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp),
            minLines = 6,
            maxLines = 10
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {

            OutlinedButton(
                onClick = onCancelar
            ) {
                Text("Cancelar")
            }

            Spacer(modifier = Modifier.width(8.dp))

            Button(
                onClick = {
                    onGuardar(
                        titulo,
                        libro,
                        capitulo,
                        versiculo,
                        nota
                    )
                }
            ) {
                Text("Guardar")
            }
        }
    }
}