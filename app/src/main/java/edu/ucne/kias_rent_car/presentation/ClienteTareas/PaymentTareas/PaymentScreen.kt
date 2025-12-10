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
import androidx.compose.ui.graphics.Color
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PaymentScreen(
    viewModel: PaymentViewModel = hiltViewModel(),
    vehicleId: String,
    onNavigateBack: () -> Unit,
    onPaymentSuccess: (String) -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(vehicleId) {
        viewModel.init(vehicleId)
    }

    LaunchedEffect(state.paymentSuccess) {
        if (state.paymentSuccess && state.reservacionId != null) {
            onPaymentSuccess(state.reservacionId!!)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "KIA'S RENT CAR",
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = scrimLight
                )
            )
        },
        containerColor = scrimLight
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 20.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            // Total a Pagar
            Text(
                text = "Total a Pagar",
                fontSize = 14.sp,
                color = Color.Gray
            )
            Text(
                text = "$${String.format("%.2f", state.total)}",
                fontSize = 36.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Método de Pago
            Text(
                text = "Método de Pago",
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = Color.White
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Opción Tarjeta
            PaymentMethodOption(
                icon = Icons.Default.AddCircle,
                title = "Tarjeta de Crédito/Débito",
                isSelected = state.metodoPago == MetodoPago.TARJETA,
                onClick = { viewModel.onEvent(PaymentEvent.MetodoPagoChanged(MetodoPago.TARJETA)) }
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Opción Billetera Digital
            PaymentMethodOption(
                icon = Icons.Default.AddCircle,
                title = "Billetera Digital",
                isSelected = state.metodoPago == MetodoPago.BILLETERA,
                onClick = { viewModel.onEvent(PaymentEvent.MetodoPagoChanged(MetodoPago.BILLETERA)) }
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Campos de tarjeta (solo si es tarjeta)
            if (state.metodoPago == MetodoPago.TARJETA) {
                // Número de Tarjeta
                Text(
                    text = "Número de Tarjeta",
                    fontSize = 14.sp,
                    color = Color.Gray
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = state.numeroTarjeta,
                    onValueChange = { newValue ->
                        // Solo permitir dígitos y máximo 16
                        val digitsOnly = newValue.filter { it.isDigit() }.take(16)
                        viewModel.onEvent(PaymentEvent.NumeroTarjetaChanged(digitsOnly))
                    },
                    placeholder = { Text("**** **** **** 1234", color = Color.Gray) },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        focusedBorderColor = Color.Gray,
                        unfocusedBorderColor = Color.Gray,
                        focusedContainerColor = Color(0xFF1E1E1E),
                        unfocusedContainerColor = Color(0xFF1E1E1E)
                    ),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    shape = RoundedCornerShape(12.dp),
                    singleLine = true,
                    visualTransformation = CreditCardVisualTransformation()
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Vencimiento y CVV
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Vencimiento",
                            fontSize = 14.sp,
                            color = Color.Gray
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedTextField(
                            value = state.vencimiento,
                            onValueChange = { newValue ->
                                // Solo permitir dígitos y máximo 4 (MMYY)
                                val digitsOnly = newValue.filter { it.isDigit() }.take(4)
                                viewModel.onEvent(PaymentEvent.VencimientoChanged(digitsOnly))
                            },
                            placeholder = { Text("MM/YY", color = Color.Gray) },
                            modifier = Modifier.fillMaxWidth(),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedTextColor = Color.White,
                                unfocusedTextColor = Color.White,
                                focusedBorderColor = Color.Gray,
                                unfocusedBorderColor = Color.Gray,
                                focusedContainerColor = Color(0xFF1E1E1E),
                                unfocusedContainerColor = Color(0xFF1E1E1E)
                            ),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            shape = RoundedCornerShape(12.dp),
                            singleLine = true,
                            visualTransformation = ExpirationDateVisualTransformation()
                        )
                    }

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "CVV",
                            fontSize = 14.sp,
                            color = Color.Gray
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedTextField(
                            value = state.cvv,
                            onValueChange = { newValue ->
                                // Solo permitir dígitos y máximo 4
                                val digitsOnly = newValue.filter { it.isDigit() }.take(4)
                                viewModel.onEvent(PaymentEvent.CvvChanged(digitsOnly))
                            },
                            placeholder = { Text("123", color = Color.Gray) },
                            modifier = Modifier.fillMaxWidth(),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedTextColor = Color.White,
                                unfocusedTextColor = Color.White,
                                focusedBorderColor = Color.Gray,
                                unfocusedBorderColor = Color.Gray,
                                focusedContainerColor = Color(0xFF1E1E1E),
                                unfocusedContainerColor = Color(0xFF1E1E1E)
                            ),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            shape = RoundedCornerShape(12.dp),
                            singleLine = true
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Nombre del Titular
                Text(
                    text = "Nombre del Titular",
                    fontSize = 14.sp,
                    color = Color.Gray
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = state.nombreTitular,
                    onValueChange = { newValue ->
                        val filtered = newValue.filter { it.isLetter() || it == ' ' }.take(30)
                        viewModel.onEvent(PaymentEvent.NombreTitularChanged(filtered))
                    },
                    placeholder = { Text("José Ramírez", color = Color.Gray) },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        focusedBorderColor = Color.Gray,
                        unfocusedBorderColor = Color.Gray,
                        focusedContainerColor = Color(0xFF1E1E1E),
                        unfocusedContainerColor = Color(0xFF1E1E1E)
                    ),
                    shape = RoundedCornerShape(12.dp),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Checkbox guardar tarjeta
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        checked = state.guardarTarjeta,
                        onCheckedChange = { viewModel.onEvent(PaymentEvent.GuardarTarjetaChanged(it)) },
                        colors = CheckboxDefaults.colors(
                            checkedColor = onErrorDark,
                            uncheckedColor = Color.Gray
                        )
                    )
                    Text(
                        text = "Guardar tarjeta para futuras compras",
                        fontSize = 14.sp,
                        color = Color.Gray
                    )
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            // Error
            state.error?.let { error ->
                Text(
                    text = error,
                    color = MaterialTheme.colorScheme.error,
                    fontSize = 14.sp,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }

            // Botón Pagar
            Button(
                onClick = { viewModel.procesarPago() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                enabled = !state.isLoading && state.isFormValid,
                colors = ButtonDefaults.buttonColors(
                    containerColor = onErrorDark,
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(28.dp)
            ) {
                if (state.isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = Color.White
                    )
                } else {
                    Text(
                        text = "Pagar $${String.format("%.2f", state.total)}",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Texto de seguridad
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Lock,
                    contentDescription = null,
                    tint = Color.Gray,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "Pago seguro - Transacción encriptada",
                    fontSize = 12.sp,
                    color = Color.Gray
                )
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}
class CreditCardVisualTransformation : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        val trimmed = text.text.take(16)
        var output = ""

        for (i in trimmed.indices) {
            output += trimmed[i]
            if ((i + 1) % 4 == 0 && i != 15) {
                output += " "
            }
        }

        val offsetMapping = object : OffsetMapping {
            override fun originalToTransformed(offset: Int): Int {
                if (offset <= 0) return 0
                val spacesBeforeOffset = (offset - 1) / 4
                return offset + spacesBeforeOffset
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
            if (i == 1 && trimmed.length > 2) {
                output += "/"
            }
        }

        val offsetMapping = object : OffsetMapping {
            override fun originalToTransformed(offset: Int): Int {
                return if (offset <= 2) offset else offset + 1
            }

            override fun transformedToOriginal(offset: Int): Int {
                return if (offset <= 2) offset else (offset - 1).coerceIn(0, trimmed.length)
            }
        }

        return TransformedText(AnnotatedString(output), offsetMapping)
    }
}

@Composable
fun PaymentMethodOption(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .then(
                if (isSelected) Modifier.border(
                    2.dp,
                    onErrorDark,
                    RoundedCornerShape(12.dp)
                ) else Modifier
            ),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF1E1E1E)
        ),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = onErrorDark,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = title,
                fontSize = 16.sp,
                color = Color.White,
                modifier = Modifier.weight(1f)
            )
            RadioButton(
                selected = isSelected,
                onClick = onClick,
                colors = RadioButtonDefaults.colors(
                    selectedColor = onErrorDark,
                    unselectedColor = Color.Gray
                )
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true, backgroundColor = 0xFF121212)
@Composable
private fun PaymentScreenPreview() {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "KIA'S RENT CAR",
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = {}) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = scrimLight
                )
            )
        },
        containerColor = scrimLight
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 20.dp)
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            Text(text = "Total a Pagar", fontSize = 14.sp, color = Color.Gray)
            Text(
                text = "$590.00",
                fontSize = 36.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Método de Pago",
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = Color.White
            )

            Spacer(modifier = Modifier.height(12.dp))

            PaymentMethodOption(
                icon = Icons.Default.AddCircle,
                title = "Tarjeta de Crédito/Débito",
                isSelected = true,
                onClick = {}
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(text = "Número de Tarjeta", fontSize = 14.sp, color = Color.Gray)
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = "4532 1234 5678 9012",
                onValueChange = {},
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    focusedContainerColor = Color(0xFF1E1E1E),
                    unfocusedContainerColor = Color(0xFF1E1E1E)
                ),
                shape = RoundedCornerShape(12.dp)
            )

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = {},
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(containerColor = onErrorDark),
                shape = RoundedCornerShape(28.dp)
            ) {
                Text(
                    text = "Pagar $590.00",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Lock,
                    contentDescription = null,
                    tint = Color.Gray,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "Pago seguro - Transacción encriptada",
                    fontSize = 12.sp,
                    color = Color.Gray
                )
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}