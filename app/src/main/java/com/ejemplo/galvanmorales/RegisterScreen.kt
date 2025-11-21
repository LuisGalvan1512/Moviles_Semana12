package com.ejemplo.galvanmorales

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.google.firebase.auth.FirebaseAuth

@Composable
fun RegisterScreen(
    onRegisterSuccess: () -> Unit,
    onNavigateBack: () -> Unit
) {
    val auth = FirebaseAuth.getInstance()
    val context = LocalContext.current

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Crear cuenta ✨",
                style = MaterialTheme.typography.headlineMedium
            )

            Spacer(Modifier.height(16.dp))

            // Campo de Correo Electrónico
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Correo electrónico") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            // Campo de Contraseña
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Contraseña") },
                visualTransformation = PasswordVisualTransformation(),
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            // Campo de Confirmar Contraseña
            OutlinedTextField(
                value = confirmPassword,
                onValueChange = { confirmPassword = it },
                label = { Text("Confirmar contraseña") },
                visualTransformation = PasswordVisualTransformation(),
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(16.dp))

            // Botón de Registro
            Button(
                onClick = {
                    // Validación de campos vacíos
                    if (email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                        Toast.makeText(
                            context,
                            "Complete todos los campos",
                            Toast.LENGTH_SHORT
                        ).show()
                        return@Button
                    }

                    // Validación de contraseñas coincidentes
                    if (password != confirmPassword) {
                        Toast.makeText(
                            context,
                            "Las contraseñas no coinciden",
                            Toast.LENGTH_SHORT
                        ).show()
                        return@Button
                    }

                    // Validación de longitud mínima
                    if (password.length < 6) {
                        Toast.makeText(
                            context,
                            "Mínimo 6 caracteres",
                            Toast.LENGTH_SHORT
                        ).show()
                        return@Button
                    }

                    // Crear usuario en Firebase
                    isLoading = true
                    auth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener { task ->
                            isLoading = false
                            if (task.isSuccessful) {
                                Toast.makeText(
                                    context,
                                    "Usuario creado ✅",
                                    Toast.LENGTH_SHORT
                                ).show()
                                onRegisterSuccess()
                            } else {
                                Toast.makeText(
                                    context,
                                    "Error: ${task.exception?.message}",
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                        }
                },
                enabled = !isLoading,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(if (isLoading) "Creando..." else "Registrarse")
            }

            // Botón para volver al Login
            TextButton(onClick = onNavigateBack) {
                Text("¿Ya tienes cuenta? Volver al login")
            }
        }
    }
}
