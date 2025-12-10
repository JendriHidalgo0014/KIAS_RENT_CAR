package edu.ucne.kias_rent_car.presentation.LoginTareas

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import edu.ucne.kias_rent_car.ui.theme.onErrorDark
import edu.ucne.kias_rent_car.ui.theme.scrimLight

@Composable
fun RegistroDialog(
    viewModel: RegistroViewModel = hiltViewModel(),
    onDismiss: () -> Unit,
    onRegistroExitoso: () -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val focusManager = LocalFocusManager.current

    LaunchedEffect(state.registroExitoso) {
        if (state.registroExitoso) {
            onRegistroExitoso()
        }
    }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
            color = scrimLight
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Crear Cuenta",
                    style = MaterialTheme.typography.headlineSmall.copy(
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                )

                Spacer(modifier = Modifier.height(20.dp))

                // Nombre
                OutlinedTextField(
                    value = state.nombre,
                    onValueChange = { viewModel.onEvent(RegistroUiEvent.NombreChanged(it)) },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Nombre", color = Color.Gray) },
                    placeholder = { Text("Tu nombre completo", color = Color.Gray) },
                    leadingIcon = {
                        Icon(Icons.Default.Person, "Nombre", tint = Color.Gray)
                    },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        focusedBorderColor = onErrorDark,
                        unfocusedBorderColor = Color.Gray,
                        cursorColor = onErrorDark
                    ),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Next
                    ),
                    keyboardActions = KeyboardActions(
                        onNext = { focusManager.moveFocus(FocusDirection.Down) }
                    ),
                    shape = RoundedCornerShape(12.dp)
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Email
                OutlinedTextField(
                    value = state.email,
                    onValueChange = { viewModel.onEvent(RegistroUiEvent.EmailChanged(it)) },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Email", color = Color.Gray) },
                    placeholder = { Text("tu@email.com", color = Color.Gray) },
                    leadingIcon = {
                        Icon(Icons.Default.Email, "Email", tint = Color.Gray)
                    },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        focusedBorderColor = onErrorDark,
                        unfocusedBorderColor = Color.Gray,
                        cursorColor = onErrorDark
                    ),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Email,
                        imeAction = ImeAction.Next
                    ),
                    keyboardActions = KeyboardActions(
                        onNext = { focusManager.moveFocus(FocusDirection.Down) }
                    ),
                    shape = RoundedCornerShape(12.dp)
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Password
                OutlinedTextField(
                    value = state.password,
                    onValueChange = { viewModel.onEvent(RegistroUiEvent.PasswordChanged(it)) },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Contraseña", color = Color.Gray) },
                    placeholder = { Text("Mínimo 4 caracteres", color = Color.Gray) },
                    leadingIcon = {
                        Icon(Icons.Default.Lock, "Password", tint = Color.Gray)
                    },
                    trailingIcon = {
                        IconButton(onClick = { viewModel.onEvent(RegistroUiEvent.TogglePasswordVisibility) }) {
                        }
                    },
                    visualTransformation = if (state.passwordVisible)
                        VisualTransformation.None
                    else
                        PasswordVisualTransformation(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        focusedBorderColor = onErrorDark,
                        unfocusedBorderColor = Color.Gray,
                        cursorColor = onErrorDark
                    ),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Password,
                        imeAction = ImeAction.Next
                    ),
                    keyboardActions = KeyboardActions(
                        onNext = { focusManager.moveFocus(FocusDirection.Down) }
                    ),
                    shape = RoundedCornerShape(12.dp)
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Confirm Password
                OutlinedTextField(
                    value = state.confirmPassword,
                    onValueChange = { viewModel.onEvent(RegistroUiEvent.ConfirmPasswordChanged(it)) },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Confirmar Contraseña", color = Color.Gray) },
                    placeholder = { Text("Repite tu contraseña", color = Color.Gray) },
                    leadingIcon = {
                        Icon(Icons.Default.Lock, "Confirm", tint = Color.Gray)
                    },
                    visualTransformation = PasswordVisualTransformation(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        focusedBorderColor = onErrorDark,
                        unfocusedBorderColor = Color.Gray,
                        cursorColor = onErrorDark
                    ),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Password,
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            focusManager.clearFocus()
                            if (state.esFormularioValido()) {
                                viewModel.onEvent(RegistroUiEvent.Registrar)
                            }
                        }
                    ),
                    shape = RoundedCornerShape(12.dp)
                )

                // Error
                state.error?.let { error ->
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = error,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall,
                        textAlign = TextAlign.Center
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    OutlinedButton(
                        onClick = onDismiss,
                        modifier = Modifier.weight(1f),
                        enabled = !state.isLoading,
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = Color.White
                        )
                    ) {
                        Text("Cancelar")
                    }

                    Button(
                        onClick = { viewModel.onEvent(RegistroUiEvent.Registrar) },
                        modifier = Modifier.weight(1f),
                        enabled = !state.isLoading && state.esFormularioValido(),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = onErrorDark,
                            contentColor = Color.White
                        )
                    ) {
                        if (state.isLoading) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(20.dp),
                                color = Color.White
                            )
                        } else {
                            Text("Registrar")
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF121212)
@Composable
private fun RegistroDialogPreview() {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        shape = RoundedCornerShape(16.dp),
        color = scrimLight
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Crear Cuenta",
                style = MaterialTheme.typography.headlineSmall.copy(
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            )

            Spacer(modifier = Modifier.height(20.dp))

            OutlinedTextField(
                value = "Juan Pérez",
                onValueChange = {},
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Nombre", color = Color.Gray) },
                leadingIcon = {
                    Icon(Icons.Default.Person, "Nombre", tint = Color.Gray)
                },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    focusedBorderColor = onErrorDark,
                    unfocusedBorderColor = Color.Gray
                ),
                shape = RoundedCornerShape(12.dp)
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = "juan@email.com",
                onValueChange = {},
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Email", color = Color.Gray) },
                leadingIcon = {
                    Icon(Icons.Default.Email, "Email", tint = Color.Gray)
                },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    focusedBorderColor = onErrorDark,
                    unfocusedBorderColor = Color.Gray
                ),
                shape = RoundedCornerShape(12.dp)
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = "••••••••",
                onValueChange = {},
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Contraseña", color = Color.Gray) },
                leadingIcon = {
                    Icon(Icons.Default.Lock, "Password", tint = Color.Gray)
                },
                visualTransformation = PasswordVisualTransformation(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    focusedBorderColor = onErrorDark,
                    unfocusedBorderColor = Color.Gray
                ),
                shape = RoundedCornerShape(12.dp)
            )

            Spacer(modifier = Modifier.height(24.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedButton(
                    onClick = {},
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White)
                ) {
                    Text("Cancelar")
                }

                Button(
                    onClick = {},
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = onErrorDark,
                        contentColor = Color.White
                    )
                ) {
                    Text("Registrar")
                }
            }
        }
    }
}