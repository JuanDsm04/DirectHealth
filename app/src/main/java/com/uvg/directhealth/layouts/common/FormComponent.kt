package com.uvg.directhealth.layouts.common

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp

@Composable
fun FormComponent(
    textId: Int,
    textFieldId: Int,
    keyboardType: KeyboardType,
    value: String,
    onValueChange: (String) -> Unit,
    singleLine: Boolean,
    trailingIcon: (@Composable (() -> Unit))? = null,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    readOnly: Boolean = false,
    expanded: Boolean = false,
    onExpandedChange: (Boolean) -> Unit = {},
    dropdownItems: List<String> = emptyList(),
    onDropdownSelect: (String) -> Unit = {},
    isError: Boolean = false,
    supportingText: @Composable() (() -> Unit) = {}
) {
    Column {
        Text(
            text = stringResource(id = textId),
            style = MaterialTheme.typography.bodyLarge.copy(
                fontWeight = FontWeight.SemiBold
            )
        )
        Spacer(modifier = Modifier.height(10.dp))
        OutlinedTextField(
            isError = isError,
            supportingText = supportingText,
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier
                .fillMaxWidth()
                .clickable { if (dropdownItems.isNotEmpty()) onExpandedChange(true) },
            placeholder = {
                Text(
                    text = stringResource(id = textFieldId),
                    style = MaterialTheme.typography.bodyMedium
                )
            },
            textStyle = MaterialTheme.typography.bodyMedium,
            readOnly = readOnly,
            shape = RoundedCornerShape(10.dp),
            singleLine = singleLine,
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = keyboardType
            ),
            trailingIcon = trailingIcon
                ?: if (dropdownItems.isNotEmpty()) {
                    {
                        IconButton(onClick = { onExpandedChange(!expanded) }) {
                            Icon(
                                imageVector = Icons.Default.ArrowDropDown,
                                contentDescription = null
                            )
                        }
                    }
                } else null,
            visualTransformation = visualTransformation
        )
        if (dropdownItems.isNotEmpty()) {
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { onExpandedChange(false) },
                modifier = Modifier
                    .height(200.dp)
            ) {
                dropdownItems.forEach { item ->
                    DropdownMenuItem(
                        text = { Text(item) },
                        onClick = {
                            onDropdownSelect(item)
                            onExpandedChange(false)
                        }
                    )
                }
            }
        }
    }
}