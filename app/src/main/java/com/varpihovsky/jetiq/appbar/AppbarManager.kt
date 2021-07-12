package com.varpihovsky.jetiq.appbar

import com.varpihovsky.core.FlowManager
import javax.inject.Inject

class AppbarManager @Inject constructor() : FlowManager<AppbarCommand>(AppbarCommand { })