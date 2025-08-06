package com.kmno.tmdb.presentation.auth

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTagsAsResourceId
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.kmno.tmdb.R
import com.kmno.tmdb.domain.auth.AuthRepository
import com.kmno.tmdb.utils.UserPreferences

/**
 * Created by Kamran Nourinezhad on 24 June-6 2025.
 * Copyright (c)  2025 MCI.
 */
@Composable
fun LoginScreen(
    viewModel: LoginViewModel,
    navController: NavHostController
) {

    val email by viewModel.email.collectAsState()
    val password by viewModel.password.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.errorMessage.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            "Sign In",
            modifier = Modifier.testTag("sign_in_title"),
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(Modifier.height(16.dp))

        OutlinedTextField(
            value = email,
            onValueChange = { viewModel.onEmailChanged(it) },
            label = { Text(stringResource(R.string.email_label)) },
            modifier = Modifier
                .fillMaxWidth()
                .testTag("email_field")
                .semantics { testTagsAsResourceId = true }
        )

        Spacer(Modifier.height(8.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { viewModel.onPasswordChanged(it) },
            label = { Text(stringResource(R.string.password_label)) },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier
                .fillMaxWidth()
                .testTag("password_field")
                .semantics { testTagsAsResourceId = true }
        )

        Spacer(Modifier.height(16.dp))

        error?.let { errorMsg ->
            Text(
                text = errorMsg,
                color = Color.Red,
                modifier = Modifier
                    .testTag("error_message")
            )
            Spacer(Modifier.height(8.dp))
        }

        Button(
            onClick = {
                viewModel.login {
                    navController.navigate("main") {
                        popUpTo("auth") { inclusive = true }
                    }
                }
            },
            enabled = !isLoading,
            modifier = Modifier
                .fillMaxWidth()
                .testTag("login_button")
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    Modifier
                        .size(20.dp)
                        .testTag("progress_indicator"),
                    strokeWidth = 2.dp
                )
            } else {
                Text("Login")
            }
        }
    }
}

@SuppressLint("ViewModelConstructorInComposable")
@Preview
@Composable
fun LoginScreenPreview() {
    val navController = rememberNavController()
    val fakeAuthRepository = object : AuthRepository {
        override suspend fun login(email: String, password: String): Result<Unit> {
            return Result.success(Unit)
        }
    }
    val fakeUserPreferences = UserPreferences(LocalContext.current)
    val viewModel = LoginViewModel(fakeAuthRepository, fakeUserPreferences)
    LoginScreen(
        viewModel = viewModel,
        navController = navController
    )
}