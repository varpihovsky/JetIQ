package com.varpihovsky.jetiq.system.navigation

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.varpihovsky.core.navigation.NavigationDirections
import com.varpihovsky.core.navigation.NavigationManager
import com.varpihovsky.jetiq.testCore.JetIQTest
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class NavigationManagerTest : JetIQTest() {
    private lateinit var navigationManager: NavigationManager

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setup() {
        navigationManager = NavigationManager()
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `Test command received`() = runBlockingTest {
        navigationManager.manage(NavigationDirections.profile)
        assertEquals(navigationManager.commands.first(), NavigationDirections.profile)
    }
}