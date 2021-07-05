package com.varpihovsky.jetiq.screens.auth

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.varpihovsky.jetiq.R
import com.varpihovsky.jetiq.ui.compose.CenterLayout
import com.varpihovsky.jetiq.ui.compose.CenterLayoutItem
import com.varpihovsky.jetiq.ui.compose.ErrorDialog

@Composable
fun Auth(
    viewModel: AuthViewModel
) {
    val login by viewModel.data.login.observeAsState("")
    val password by viewModel.data.password.observeAsState("")
    val passwordHidden by viewModel.data.passwordHidden.observeAsState(true)
    val progressShown by viewModel.data.progressShown.observeAsState(false)

    val exception by viewModel.data.exceptions.collectAsState()
    exception?.message?.let {
        ErrorDialog(message = it, onDismiss = viewModel::onExceptionProcessed)
    }

    Auth(
        loginValue = login,
        loginOnChange = viewModel::onLoginChange,
        passwordValue = password,
        passwordOnChange = viewModel::onPasswordChange,
        onLoginClick = viewModel::onLogin,
        passwordHidden = passwordHidden,
        onPasswordHiddenChange = viewModel::onPasswordHiddenChange,
        progressShown = progressShown
    )
}

@Composable
fun Auth(
    loginValue: String,
    loginOnChange: (String) -> Unit,
    passwordValue: String,
    passwordOnChange: (String) -> Unit,
    onLoginClick: () -> Unit,
    passwordHidden: Boolean,
    onPasswordHiddenChange: (Boolean) -> Unit,
    progressShown: Boolean
) {
    CenterLayout {
        CenterLayoutItem {
            Text(text = "JetIQ", style = MaterialTheme.typography.h1)
        }
        CenterLayoutItem(modifier = Modifier.padding(9.dp)) {
            AuthField(
                value = loginValue,
                onValueChange = loginOnChange,
                placeholderText = "Логін"
            )
        }
        CenterLayoutItem(modifier = Modifier.padding(9.dp)) {
            val visualTransformation = if (passwordHidden) {
                VisualTransformation { text ->
                    TransformedText(
                        AnnotatedString(String(text.map { '*' }.toCharArray())),
                        OffsetMapping.Identity
                    )
                }
            } else {
                VisualTransformation.None
            }

            AuthField(
                value = passwordValue,
                onValueChange = passwordOnChange,
                placeholderText = "Пароль",
                trailingIcon = {
                    PasswordFieldIcon(
                        checked = passwordHidden,
                        onCheckedChange = onPasswordHiddenChange
                    )
                },
                visualTransformation = visualTransformation
            )
        }
        CenterLayoutItem(modifier = Modifier.padding(9.dp)) {
            AuthButton(
                onClick = onLoginClick,
                text = "Увійти",
                progressShown = progressShown
            )
        }
    }
}

@Composable
fun PasswordFieldIcon(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
) {
    IconToggleButton(
        checked = checked,
        onCheckedChange = onCheckedChange
    ) {
        if (checked) {
            Icon(
                painter = painterResource(id = R.drawable.ic_baseline_remove_red_eye_24),
                contentDescription = null
            )
        } else {
            Icon(
                painter = painterResource(id = R.drawable.ic_baseline_password_24),
                contentDescription = null
            )
        }
    }
}

@Composable
fun AuthField(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit,
    placeholderText: String = "",
    trailingIcon: @Composable (() -> Unit)? = null,
    visualTransformation: VisualTransformation = VisualTransformation.None
) {
    TextField(
        modifier = modifier
            .fillMaxWidth(0.9f)
            .height(60.dp),
        value = value,
        placeholder = {
            CenterLayout {
                Text(
                    text = placeholderText,
                    style = MaterialTheme.typography.h6.copy(fontSize = 18.sp)
                )
            }
        },
        maxLines = 1,
        shape = MaterialTheme.shapes.large,
        trailingIcon = trailingIcon,
        colors = TextFieldDefaults.textFieldColors(
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent
        ),
        visualTransformation = visualTransformation,
        onValueChange = onValueChange
    )
}

@Composable
fun AuthButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    text: String,
    progressShown: Boolean,
) {
    Button(
        modifier = modifier
            .fillMaxWidth(0.9f)
            .height(60.dp),
        shape = MaterialTheme.shapes.large,
        onClick = onClick
    ) {
        if (progressShown) {
            CircularProgressIndicator(color = Color.White)
        } else {
            Text(text = text, style = MaterialTheme.typography.button.copy(fontSize = 21.sp))
        }
    }
}