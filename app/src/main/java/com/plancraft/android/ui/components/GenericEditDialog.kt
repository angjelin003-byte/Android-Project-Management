package com.plancraft.android.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.plancraft.android.ui.theme.SlateSurface
import java.util.Locale

@Composable
fun GenericEditDialog(
    title: String,
    fields: Map<String, String>,
    onDismiss: () -> Unit,
    onSave: (Map<String, String>) -> Unit
) {
    var state by remember { mutableStateOf(fields) }
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Edit $title", color = Color.White) },
        text = {
            LazyColumn(modifier = Modifier.fillMaxWidth().heightIn(max = 400.dp)) {
                items(state.keys.toList()) { key ->
                    OutlinedTextField(
                        value = state[key] ?: "",
                        onValueChange = { newVal -> state = state.toMutableMap().apply { put(key, newVal) } },
                        label = { Text(key.replaceFirstChar { it.uppercase() }) },
                        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
                    )
                }
            }
        },
        confirmButton = { Button(onClick = { onSave(state) }) { Text("Save") } },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Cancel") } },
        containerColor = SlateSurface
    )
}
