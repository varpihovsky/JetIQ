package com.varpihovsky.core_nav.main

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

import com.varpihovsky.core.eventBus.EventBus
import com.varpihovsky.core_nav.dsl.NavigationControllerBuilder

/**
 * Simple way to store navigation controller with singleton.
 *
 * @author Vladyslav Podrezenko
 */
object JetNav {
    private var controller: NavigationController? = null

    /**
     * Creates controller only when it wasn't created before.
     */
    fun createControllerIfNotCreated(
        eventBus: EventBus,
        defaultRoute: String,
        builder: NavigationControllerBuilder.() -> Unit
    ) {
        if (controller == null) {
            controller = NavigationControllerBuilder(eventBus, defaultRoute).apply(builder).build()
        } else {
            controller?.onBusClear()
        }
    }

    /**
     * Returns created controller. Throws an exception when it wasn't created.
     */
    fun getController(): NavigationController = checkNotNull(controller)

    /**
     * Returns created controller or null if it wasn't created.
     */
    fun getControllerOrNull() = controller
}