package com.varpihovsky.jetiq.ui.appbar

import com.varpihovsky.jetiq.system.FlowManager
import javax.inject.Inject

class AppbarManager @Inject constructor() : FlowManager<AppbarCommand>(AppbarCommand { })