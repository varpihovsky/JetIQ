package com.varpihovsky.jetiq.back.model

import com.varpihovsky.jetiq.back.db.managers.ConfidentialDatabaseManager
import com.varpihovsky.jetiq.back.dto.Confidential
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

abstract class ConfidentModel constructor(
    private val confidentialDatabaseManager: ConfidentialDatabaseManager
) {
    private var confidential: Confidential? = null

    init {
        GlobalScope.launch(Dispatchers.IO) { collectConfidential() }
    }

    private suspend fun collectConfidential() {
        confidentialDatabaseManager.getConfidential().collect {
            confidential = it
        }
    }

    protected fun requireConfidential(): Confidential {
        while (confidential == null);
        return confidential!!
    }
}