package edu.ucne.kias_rent_car.presentation.LoginTareas

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import edu.ucne.kias_rent_car.R
import edu.ucne.kias_rent_car.ui.theme.onErrorDark
import edu.ucne.kias_rent_car.ui.theme.scrimLight

@Composable
fun RegistroScreen(
    viewModel: RegistroViewModel = hiltViewModel(),
    onRegistroExitoso: () -> Unit,
    onNavigateBack: () -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(state.registroExitoso) {
        if (state.registroExitoso) {
            onRegistroExitoso()
        }
    }

    RegistroBody(state, viewModel::onEvent, onNavigateBack)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegistroBody(
    state: RegistroUiState,
    onEvent: (RegistroUiEvent) -> Unit,
    onNavigateBack: () -> Unit = {}
) {
    val focusManager = LocalFocusManager.current
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(state.userMessage) {
        state.userMessage?.let { message ->
            snackbarHostState.showSnackbar(message)
            onEvent(RegistroUiEvent.UserMessageShown)
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver",
                            tint = MaterialTheme.colorScheme.onBackground
                        )
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
                .padding(horizontal = 24.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            Image(
                painter = painterResource(id = R.drawable.kias_rent_car),
                contentDescription = "Logo",
                modifier = Modifier.size(150.dp)
            )

            Text(
                text = "Crear Cuenta",
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )
            )

            Spacer(modifier = Modifier.height(24.dp))

            OutlinedTextField(
                value = state.nombre,
                onValueChange = { onEvent(RegistroUiEvent.OnNombreChange(it)) },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Nombre completo", color = Color.Gray) },
                placeholder = { Text("Ingresa tu nombre", color = Color.Gray) },
                leadingIcon = { Icon(Icons.Default.Person, null, tint = Color.Gray) },
                colors = textFieldColors(),
                singleLine = true,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                keyboardActions = KeyboardActions(onNext = { focusManager.moveFocus(FocusDirection.Down) }),
                shape = RoundedCornerShape(12.dp)
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = state.email,
                onValueChange = { onEvent(RegistroUiEvent.OnEmailChange(it)) },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Email", color = Color.Gray) },
                placeholder = { Text("ejemplo@gmail.com", color = Color.Gray) },
                leadingIcon = { Icon(Icons.Default.Email, null, tint = Color.Gray) },
                isError = state.emailError != null,
                supportingText = { state.emailError?.let { Text(it) } },
                colors = textFieldColors(),
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(onNext = { focusManager.moveFocus(FocusDirection.Down) }),
                shape = RoundedCornerShape(12.dp)
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = state.telefono,
                onValueChange = { onEvent(RegistroUiEvent.OnTelefonoChange(it)) },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Teléfono (opcional)", color = Color.Gray) },
                placeholder = { Text("Ingresa tu teléfono", color = Color.Gray) },
                leadingIcon = { Icon(Icons.Default.Phone, null, tint = Color.Gray) },
                colors = textFieldColors(),
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(onNext = { focusManager.moveFocus(FocusDirection.Down) }),
                shape = RoundedCornerShape(12.dp)
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = state.password,
                onValueChange = { onEvent(RegistroUiEvent.OnPasswordChange(it)) },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Contraseña", color = Color.Gray) },
                placeholder = { Text("Mínimo 4 caracteres", color = Color.Gray) },
                leadingIcon = { Icon(Icons.Default.Lock, null, tint = Color.Gray) },
                trailingIcon = {
                    IconButton(onClick = { onEvent(RegistroUiEvent.TogglePasswordVisibility) }) {
                        Icon(
                            imageVector = if (state.passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                            contentDescription = null,
                            tint = Color.Gray
                        )
                    }
                },
                visualTransformation = if (state.passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                colors = textFieldColors(),
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(onNext = { focusManager.moveFocus(FocusDirection.Down) }),
                shape = RoundedCornerShape(12.dp)
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = state.confirmPassword,
                onValueChange = { onEvent(RegistroUiEvent.OnConfirmPasswordChange(it)) },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Confirmar Contraseña", color = Color.Gray) },
                placeholder = { Text("Repite tu contraseña", color = Color.Gray) },
                leadingIcon = { Icon(Icons.Default.Lock, null, tint = Color.Gray) },
                visualTransformation = PasswordVisualTransformation(),
                colors = textFieldColors(),
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(onDone = {
                    focusManager.clearFocus()
                    onEvent(RegistroUiEvent.Registrar)
                }),
                shape = RoundedCornerShape(12.dp)
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = { onEvent(RegistroUiEvent.Registrar) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                enabled = !state.isLoading,
                colors = ButtonDefaults.buttonColors(containerColor = onErrorDark),
                shape = RoundedCornerShape(12.dp)
            ) {
                if (state.isLoading) {
                    CircularProgressIndicator(modifier = Modifier.size(24.dp), color = Color.White)
                } else {
                    Text("Registrarse", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("¿Ya tienes cuenta? ", color = Color.Gray, fontSize = 14.sp)
                TextButton(onClick = onNavigateBack) {
                    Text("Inicia Sesión", color = onErrorDark, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                }
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
private fun textFieldColors() = OutlinedTextFieldDefaults.colors(
    focusedTextColor = MaterialTheme.colorScheme.onBackground,
    unfocusedTextColor = MaterialTheme.colorScheme.onBackground,
    focusedBorderColor = onErrorDark,
    unfocusedBorderColor = Color.Gray,
    cursorColor = onErrorDark
)

@Preview(showBackground = true, backgroundColor = 0xFF121212)
@Composable
private fun RegistroBodyPreview() {
    MaterialTheme {
        val state = RegistroUiState(
            nombre = "Juan Pérez",
            email = "juan@email.com",
            telefono = "8091234567"
        )
        RegistroBody(state, {})
    }
}