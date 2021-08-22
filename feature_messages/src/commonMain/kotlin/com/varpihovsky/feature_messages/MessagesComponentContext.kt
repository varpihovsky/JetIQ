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
package com.varpihovsky.feature_messages

import com.arkivanov.decompose.Router
import com.arkivanov.essenty.parcelable.Parcelable
import com.varpihovsky.core_lifecycle.JetIQComponentContext
import com.varpihovsky.core_lifecycle.childContext
import com.varpihovsky.core_lifecycle.jetIQRouter
import kotlin.reflect.KClass

internal interface MessagesComponentContext : JetIQComponentContext {
    val navigationController: MessagesNavigationController
}

internal class DefaultMessagesComponentContext(
    jetIQComponentContext: JetIQComponentContext,
    override val navigationController: MessagesNavigationController
) : MessagesComponentContext, JetIQComponentContext by jetIQComponentContext

internal fun <C : Parcelable, T : Any> JetIQComponentContext.messagesRouter(
    initialConfiguration: () -> C,
    initialBackStack: () -> List<C> = ::emptyList,
    configurationClass: KClass<out C>,
    key: String = "DefaultRouter",
    handleBackButton: Boolean = false,
    navigationController: MessagesNavigationController,
    childFactory: (configuration: C, MessagesComponentContext) -> T
): Router<C, T> = jetIQRouter(
    initialConfiguration = initialConfiguration,
    initialBackStack = initialBackStack,
    configurationClass = configurationClass,
    key = key,
    handleBackButton = handleBackButton,
    bottomBarController = bottomBarController,
    appBarController = appBarController,
    exceptionController = exceptionController,
    mainNavigationController = mainNavigationController,
    childFactory = { configuration, jetIQComponentContext ->
        childFactory(
            configuration,
            DefaultMessagesComponentContext(jetIQComponentContext, navigationController)
        )
    }
)

internal fun MessagesComponentContext.childContext(key: String): MessagesComponentContext =
    DefaultMessagesComponentContext(childContext(key), navigationController)