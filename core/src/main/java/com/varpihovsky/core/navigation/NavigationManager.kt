package com.varpihovsky.core.navigation

import com.varpihovsky.core.FlowManager
import javax.inject.Inject

class NavigationManager @Inject constructor() :
    FlowManager<NavigationCommand>(NavigationDirections.empty)