package com.notesmanager.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import coil.size.Scale
import  com.notesmanager.ui.theme.*
import com.notesmanager.ui.*


@Composable
fun LoginScreen(
    loginViewModel: LoginViewModel = viewModel(),
    onNavigateToRegistration: () -> Unit,
    onNavigateToForgotPassword: () -> Unit,
    onNavigateToAuthenticatedRoute: () -> Unit,
) {


    val loginState by remember {
        loginViewModel.loginState
    }
    if (loginState.isLoginSuccessful) {
        /**
         * Navigate to Authenticated navigation route
         * once login is successful
         */
        LaunchedEffect(key1 = true) {
            onNavigateToAuthenticatedRoute.invoke()
        }
    } else {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .navigationBarsPadding()
                .imePadding()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ElevatedCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(normalDimensions.paddingLarge)
            ) {
                Column(
                    modifier = Modifier
                        .padding(horizontal = normalDimensions.paddingLarge)
                        .padding(bottom = normalDimensions.paddingExtraLarge)
                ) {

                    /*
                    // Heading Jetpack Compose
                    MediumTitleText(
                        modifier = Modifier
                            .padding(top = normalDimensions.paddingLarge)
                            .fillMaxWidth(),
                        text = stringResource(id = com.notesmanager.R.string.jetpack_compose),
                        textAlign = TextAlign.Center
                    )
                    */

                    /*
                    // Login Logo
                    AsyncImage(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(128.dp)
                            .padding(top = normalDimensions.paddingSmall),
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(data =  com.notesmanager.R.drawable.ic_launcher_background)
                            .crossfade(enable = true)
                            .scale(Scale.FILL)
                            .build(),
                        contentDescription = stringResource(id = com.notesmanager.R.string.login_heading_text)
                    )

                    */

                    // Heading Login
                    TitleText(
                        modifier = Modifier.padding(top = normalDimensions.paddingLarge),
                        text = stringResource(id = com.notesmanager.R.string.login_heading_text)
                    )

                    // Login Inputs Composable
                    LoginInputs(
                        loginState = loginState, onEmailOrMobileChange = { inputString ->
                        loginViewModel.onUiEvent(
                            loginUiEvent = LoginUiEvent.EmailOrMobileChanged(
                                inputString
                            )
                        )
                    }, onPasswordChange = { inputString ->
                        loginViewModel.onUiEvent(
                            loginUiEvent = LoginUiEvent.PasswordChanged(
                                inputString
                            )
                        )
                    }, onSubmit = {
                        loginViewModel.onUiEvent(loginUiEvent = LoginUiEvent.Submit)
                    }, onForgotPasswordClick = onNavigateToForgotPassword
                    )

                }
            }

            /*
            Row(
                modifier = Modifier.padding(normalDimensions.paddingNormal),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Don't have an account?
                Text(text = stringResource(id = com.notesmanager.R.string.do_not_have_account))

                //Register
                Text(
                    modifier = Modifier
                        .padding(start = normalDimensions.paddingExtraSmall)
                        .clickable {
                            onNavigateToRegistration.invoke()
                        },
                    text = stringResource(id = com.notesmanager.R.string.register),
                    color = MaterialTheme.colorScheme.primary
                )
            }
            */
        }
    }
}