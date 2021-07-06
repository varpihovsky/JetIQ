package com.varpihovsky.jetiq.system.data_transfer

import com.varpihovsky.jetiq.system.JetIQViewModel
import kotlin.reflect.KClass

open class ViewModelData<T, C : JetIQViewModel>(val data: T, val sender: KClass<C>)

