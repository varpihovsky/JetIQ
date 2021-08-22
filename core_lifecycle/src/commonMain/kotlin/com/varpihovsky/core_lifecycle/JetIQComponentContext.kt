package com.varpihovsky.core_lifecycle

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.Router
import com.arkivanov.decompose.childContext
import com.arkivanov.essenty.parcelable.Parcelable
import kotlin.reflect.KClass

interface JetIQComponentContext : ComponentContext {
    val bottomBarController: BottomBarController
    val appBarController: AppBarController
    val exceptionController: ExceptionController
    val mainNavigationController: MainNavigationController
}

class DefaultJetIQComponentContext(
    componentContext: ComponentContext,
    override val bottomBarController: BottomBarController,
    override val appBarController: AppBarController,
    override val exceptionController: ExceptionController,
    override val mainNavigationController: MainNavigationController
) : JetIQComponentContext, ComponentContext by componentContext

fun <C : Parcelable, T : Any> ComponentContext.jetIQRouter(
    initialConfiguration: () -> C,
    initialBackStack: () -> List<C> = ::emptyList,
    configurationClass: KClass<out C>,
    key: String = "JetIQRouter",
    handleBackButton: Boolean = false,
    childFactory: (configuration: C, JetIQComponentContext) -> T,
    bottomBarController: BottomBarController,
    appBarController: AppBarController,
    exceptionController: ExceptionController,
    mainNavigationController: MainNavigationController
): Router<C, T> =
    router(
        initialConfiguration = initialConfiguration,
        initialBackStack = initialBackStack,
        configurationClass = configurationClass,
        key = key,
        handleBackButton = handleBackButton
    ) { configuration, componentContext ->
        childFactory(
            configuration,
            DefaultJetIQComponentContext(
                componentContext = componentContext,
                bottomBarController = bottomBarController,
                appBarController = appBarController,
                exceptionController = exceptionController,
                mainNavigationController = mainNavigationController
            )
        )
    }

fun <C : Parcelable, T : Any> JetIQComponentContext.router(
    initialConfiguration: () -> C,
    initialBackStack: () -> List<C> = ::emptyList,
    configurationClass: KClass<out C>,
    key: String = "JetIQChildRouter",
    handleBackButton: Boolean = false,
    childFactory: (configuration: C, JetIQComponentContext) -> T,
    mainNavigationController: MainNavigationController
): Router<C, T> =
    router(
        initialConfiguration = initialConfiguration,
        initialBackStack = initialBackStack,
        configurationClass = configurationClass,
        key = key,
        handleBackButton = handleBackButton
    ) { configuration, componentContext ->
        childFactory(
            configuration,
            DefaultJetIQComponentContext(
                componentContext = componentContext,
                bottomBarController = bottomBarController,
                appBarController = appBarController,
                exceptionController = exceptionController,
                mainNavigationController = mainNavigationController
            )
        )
    }

fun JetIQComponentContext.childContext(key: String): JetIQComponentContext = DefaultJetIQComponentContext(
    childContext(key),
    bottomBarController,
    appBarController,
    exceptionController,
    mainNavigationController
)