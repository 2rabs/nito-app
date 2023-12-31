package club.nito.feature.auth.login

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import club.nito.core.designsystem.component.CenterAlignedTopAppBar
import club.nito.core.designsystem.component.Scaffold
import club.nito.core.designsystem.component.Text
import club.nito.core.ui.koinStateMachine
import club.nito.core.ui.message.SnackbarMessageEffect
import club.nito.feature.auth.component.PasswordTextField

@Composable
public fun LoginRoute(
    viewModel: LoginScreenStateMachine = koinStateMachine(LoginScreenStateMachine::class),
    onLoggedIn: () -> Unit = {},
    onRegisterClick: () -> Unit = {},
    hideTopAppBar: Boolean = false,
) {
    viewModel.event.collectAsState(initial = null).value?.let {
        LaunchedEffect(it.hashCode()) {
            when (it) {
                LoginScreenEvent.NavigateToRegister -> onRegisterClick()
                LoginScreenEvent.LoggedIn -> onLoggedIn()
            }
            viewModel.consume(it)
        }
    }

    val uiState by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    SnackbarMessageEffect(
        snackbarHostState = snackbarHostState,
        userMessageStateHolder = viewModel.userMessageStateHolder,
    )

    LoginScreen(
        uiState = uiState,
        snackbarHostState = snackbarHostState,
        dispatch = viewModel::dispatch,
        hideTopAppBar = hideTopAppBar,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun LoginScreen(
    uiState: LoginScreenUiState,
    snackbarHostState: SnackbarHostState,
    dispatch: (LoginScreenIntent) -> Unit = {},
    hideTopAppBar: Boolean = false,
) {
    Scaffold(
        topBar = {
            if (hideTopAppBar.not()) {
                CenterAlignedTopAppBar(
                    title = {
                        Text(
                            text = "サインイン",
                        )
                    },
                )
            }
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        content = { padding ->
            Column(
                modifier = Modifier
                    .padding(padding)
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                OutlinedTextField(
                    value = uiState.email,
                    onValueChange = { dispatch(LoginScreenIntent.ChangeInputEmail(it)) },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text(text = "Email") },
                    placeholder = { Text(text = "xxxxxx@nito.club") },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Email,
                        imeAction = ImeAction.Next,
                    ),
                    singleLine = true,
                )
                PasswordTextField(
                    modifier = Modifier.fillMaxWidth(),
                    password = uiState.password,
                    onPasswordChange = { dispatch(LoginScreenIntent.ChangeInputPassword(it)) },
                    passwordVisible = uiState.isVisiblePassword,
                    onPasswordVisibleChange = { dispatch(LoginScreenIntent.ChangePasswordVisible(it)) },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Password,
                        imeAction = ImeAction.Done,
                    ),
                )
                Button(
                    modifier = Modifier
                        .fillMaxWidth(),
                    onClick = { dispatch(LoginScreenIntent.ClickSignIn) },
                    enabled = uiState.isSignInButtonEnabled,
                ) {
                    Text(
                        text = "Sign In",
                    )
                }
            }
        },
    )
}
