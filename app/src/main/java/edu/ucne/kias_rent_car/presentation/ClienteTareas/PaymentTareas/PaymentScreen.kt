package edu.ucne.kias_rent_car.presentation.PaymentTareas

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import edu.ucne.kias_rent_car.ui.theme.onErrorDark
import edu.ucne.kias_rent_car.ui.theme.scrimLight

@Composable
fun PaymentScreen(
    viewModel: PaymentViewModel = hiltViewModel(),
    vehicleId: String,
    onNavigateBack: () -> Unit,
    onPaymentSuccess: (String) -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(vehicleId) {
        viewModel.onEvent(PaymentUiEvent.Init(vehicleId))
    }

    LaunchedEffect(state.paymentSuccess) {
        if (state.paymentSuccess && state.reservacionId != null) {
            onPaymentSuccess(state.reservacionId!!)
        }
    }

    PaymentBody(
        state = state,
        onEvent = { event ->
            when (event) {
                PaymentUiEvent.NavigateBack -> onNavigateBack()
                else -> viewModel.onEvent(event)
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PaymentBody(
    state: PaymentUiState,
    onEvent: (PaymentUiEvent) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("KIA'S RENT CAR", color = MaterialTheme.colorScheme.onSurface, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { onEvent(PaymentUiEvent.NavigateBack) }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Volver", tint = MaterialTheme.colorScheme.onSurface)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = scrimLight)
            )
        },
        containerColor = scrimLight
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 20.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            Text("Total a Pagar", fontSize = 14.sp, color = MaterialTheme.colorScheme.outline)
            Text(
                text = "$${String.format("%.2f", state.total)}",
                fontSize = 36.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text("Método de Pago", fontSize = 16.sp, fontWeight = FontWeight.Medium, color = MaterialTheme.colorScheme.onSurface)

            Spacer(modifier = Modifier.height(12.dp))

            PaymentMethodOption(
                icon = Icons.Default.AddCircle,
                title = "Tarjeta de Crédito/Débito",
                isSelected = state.metodoPago == MetodoPago.TARJETA,
                onClick = { onEvent(PaymentUiEvent.OnMetodoPagoChange(MetodoPago.TARJETA)) }
            )

            Spacer(modifier = Modifier.height(8.dp))

            PaymentMethodOption(
                icon = Icons.Default.AddCircle,
                title = "Billetera Digital",
                isSelected = state.metodoPago == MetodoPago.BILLETERA,
                onClick = { onEvent(PaymentUiEvent.OnMetodoPagoChange(MetodoPago.BILLETERA)) }
            )

            Spacer(modifier = Modifier.height(24.dp))

            if (state.metodoPago == MetodoPago.TARJETA) {
                Text("Número de Tarjeta", fontSize = 14.sp, color = MaterialTheme.colorScheme.outline)
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = state.numeroTarjeta,
                    onValueChange = { onEvent(PaymentUiEvent.OnNumeroTarjetaChange(it)) },
                    placeholder = { Text("**** **** **** 1234", color = MaterialTheme.colorScheme.outline) },
                    modifier = Modifier.fillMaxWidth(),
                    colors = textFieldColors(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    shape = RoundedCornerShape(12.dp),
                    singleLine = true,
                    visualTransformation = CreditCardVisualTransformation()
                )

                Spacer(modifier = Modifier.height(16.dp))

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text("Vencimiento", fontSize = 14.sp, color = MaterialTheme.colorScheme.outline)
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedTextField(
                            value = state.vencimiento,
                            onValueChange = { onEvent(PaymentUiEvent.OnVencimientoChange(it)) },
                            placeholder = { Text("MM/YY", color = MaterialTheme.colorScheme.outline) },
                            modifier = Modifier.fillMaxWidth(),
                            colors = textFieldColors(),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            shape = RoundedCornerShape(12.dp),
                            singleLine = true,
                            visualTransformation = ExpirationDateVisualTransformation()
                        )
                    }

                    Column(modifier = Modifier.weight(1f)) {
                        Text("CVV", fontSize = 14.sp, color = MaterialTheme.colorScheme.outline)
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedTextField(
                            value = state.cvv,
                            onValueChange = { onEvent(PaymentUiEvent.OnCvvChange(it)) },
                            placeholder = { Text("123", color = MaterialTheme.colorScheme.outline) },
                            modifier = Modifier.fillMaxWidth(),
                            colors = textFieldColors(),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            shape = RoundedCornerShape(12.dp),
                            singleLine = true
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text("Nombre del Titular", fontSize = 14.sp, color = MaterialTheme.colorScheme.outline)
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = state.nombreTitular,
                    onValueChange = { onEvent(PaymentUiEvent.OnNombreTitularChange(it)) },
                    placeholder = { Text("José Ramírez", color = MaterialTheme.colorScheme.outline) },
                    modifier = Modifier.fillMaxWidth(),
                    colors = textFieldColors(),
                    shape = RoundedCornerShape(12.dp),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(16.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(
                        checked = state.guardarTarjeta,
                        onCheckedChange = { onEvent(PaymentUiEvent.OnGuardarTarjetaChange(it)) },
                        colors = CheckboxDefaults.colors(checkedColor = onErrorDark, uncheckedColor = MaterialTheme.colorScheme.outline)
                    )
                    Text("Guardar tarjeta para futuras compras", fontSize = 14.sp, color = MaterialTheme.colorScheme.outline)
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = { onEvent(PaymentUiEvent.ProcesarPago) },
                modifier = Modifier.fillMaxWidth().height(56.dp),
                enabled = !state.isLoading && state.isFormValid,
                colors = ButtonDefaults.buttonColors(containerColor = onErrorDark),
                shape = RoundedCornerShape(28.dp)
            ) {
                if (state.isLoading) {
                    CircularProgressIndicator(Modifier.size(24.dp), color = MaterialTheme.colorScheme.onPrimary)
                } else {
                    Text("Pagar $${String.format("%.2f", state.total)}", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Lock, null, tint = MaterialTheme.colorScheme.outline, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text("Pago seguro - Transacción encriptada", fontSize = 12.sp, color = MaterialTheme.colorScheme.outline)
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
private fun PaymentMethodOption(
    icon: ImageVector,
    title: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .then(if (isSelected) Modifier.border(2.dp, onErrorDark, RoundedCornerShape(12.dp)) else Modifier),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(modifier = Modifier.fillMaxWidth().padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Icon(icon, null, tint = onErrorDark, modifier = Modifier.size(24.dp))
            Spacer(modifier = Modifier.width(12.dp))
            Text(title, fontSize = 16.sp, color = MaterialTheme.colorScheme.onSurface, modifier = Modifier.weight(1f))
            RadioButton(
                selected = isSelected,
                onClick = onClick,
                colors = RadioButtonDefaults.colors(selectedColor = onErrorDark, unselectedColor = MaterialTheme.colorScheme.outline)
            )
        }
    }
}

@Composable
private fun textFieldColors() = OutlinedTextFieldDefaults.colors(
    focusedTextColor = MaterialTheme.colorScheme.onSurface,
    unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
    focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
    unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
    focusedBorderColor = MaterialTheme.colorScheme.outline,
    unfocusedBorderColor = MaterialTheme.colorScheme.outline
)

class CreditCardVisualTransformation : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        val trimmed = text.text.take(16)
        var output = ""
        for (i in trimmed.indices) {
            output += trimmed[i]
            if ((i + 1) % 4 == 0 && i != 15) output += " "
        }
        val offsetMapping = object : OffsetMapping {
            override fun originalToTransformed(offset: Int): Int {
                if (offset <= 0) return 0
                return offset + (offset - 1) / 4
            }
            override fun transformedToOriginal(offset: Int): Int {
                if (offset <= 0) return 0
                val spacesBeforeOffset = when {
                    offset <= 4 -> 0
                    offset <= 9 -> 1
                    offset <= 14 -> 2
                    else -> 3
                }
                return (offset - spacesBeforeOffset).coerceIn(0, trimmed.length)
            }
        }
        return TransformedText(AnnotatedString(output), offsetMapping)
    }
}

class ExpirationDateVisualTransformation : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        val trimmed = text.text.take(4)
        var output = ""
        for (i in trimmed.indices) {
            output += trimmed[i]
            if (i == 1 && trimmed.length > 2) output += "/"
        }
        val offsetMapping = object : OffsetMapping {
            override fun originalToTransformed(offset: Int) = if (offset <= 2) offset else offset + 1
            override fun transformedToOriginal(offset: Int) = if (offset <= 2) offset else (offset - 1).coerceIn(0, trimmed.length)
        }
        return TransformedText(AnnotatedString(output), offsetMapping)
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF121212)
@Composable
private fun PaymentBodyPreview() {
    val state = PaymentUiState(total = 590.0, metodoPago = MetodoPago.TARJETA, isFormValid = true)
    PaymentBody(state) {}
}