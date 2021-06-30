package com.varpihovsky.jetiq.system.navigation

import com.varpihovsky.jetiq.system.FlowManager

class NavigationManager : FlowManager<NavigationCommand>(NavigationDirections.empty)