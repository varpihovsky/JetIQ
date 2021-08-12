/* JetIQ
 * Copyright © 2021 Vladyslav Podrezenko
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package com.varpihovsky.feature_auth

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.varpihovsky.core_lifecycle.emptyAppbar
import com.varpihovsky.core_ui.compose.foundation.CenterLayout
import com.varpihovsky.core_ui.compose.foundation.CenterLayoutItem
import com.varpihovsky.core_ui.compose.widgets.PasswordFieldIcon

@Composable
fun Auth(
    viewModel: AuthViewModel
) {
    viewModel.emptyAppbar()

    Auth(
        loginValue = viewModel.data.login.value,
        loginOnChange = viewModel::onLoginChange,
        passwordValue = viewModel.data.password.value,
        passwordOnChange = viewModel::onPasswordChange,
        onLoginClick = viewModel::onLogin,
        passwordHidden = viewModel.data.passwordHidden.value,
        onPasswordHiddenChange = viewModel::onPasswordHiddenChange,
        progressShown = viewModel.data.progressShown.value
    )
}

@Composable
private fun Auth(
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
                        AnnotatedString(text.map { '*' }.toCharArray().concatToString()),
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