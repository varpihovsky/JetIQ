package com.varpihovsky.jetiq.system.navigation

import com.varpihovsky.jetiq.system.FlowManager
import javax.inject.Inject

class NavigationManager @Inject constructor() :
    FlowManager<NavigationCommand>(NavigationDirections.empty)