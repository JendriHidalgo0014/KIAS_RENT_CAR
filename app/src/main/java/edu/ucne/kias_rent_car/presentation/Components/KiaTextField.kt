package edu.ucne.kias_rent_car.presentation.Components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

data class KiaTextFieldConfig(
    val placeholder: String? = null,
    val enabled: Boolean = true,
    val readOnly: Boolean = false,
    val singleLine: Boolean = true,
    val minHeight: Dp? = null,
    val keyboardType: KeyboardType = KeyboardType.Text,
    val visualTransformation: VisualTransformation = VisualTransformation.None,
    val isError: Boolean = false,
    val supportingText: String? = null
)

@Composable
fun KiaTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    config: KiaTextFieldConfig = KiaTextFieldConfig(),
    trailingIcon: @Composable (() -> Unit)? = null,
    leadingIcon: @Composable (() -> Unit)? = null
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        placeholder = config.placeholder?.let { { Text(it, color = MaterialTheme.colorScheme.outline) } },
        modifier = modifier
            .fillMaxWidth()
            .then(if (config.minHeight != null) Modifier.heightIn(min = config.minHeight) else Modifier),
        enabled = config.enabled,
        readOnly = config.readOnly,
        singleLine = config.singleLine,
        keyboardOptions = KeyboardOptions(keyboardType = config.keyboardType),
        visualTransformation = config.visualTransformation,
        trailingIcon = trailingIcon,
        leadingIcon = leadingIcon,
        isError = config.isError,
        supportingText = config.supportingText?.let { { Text(it) } },
        colors = kiaTextFieldColors(),
        shape = RoundedCornerShape(12.dp)
    )
}