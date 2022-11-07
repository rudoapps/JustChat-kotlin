package es.rudo.firebasechat.utils

import androidx.annotation.VisibleForTesting
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

abstract class CoroutineAppRepository {
    companion object {
        @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
        var DISPATCHER: CoroutineDispatcher = Dispatchers.IO
    }

    protected suspend fun <T> coroutineBackground(task: () -> T): T = withContext(DISPATCHER) {
        try {
            task()
        } catch (e: Throwable) {
            throw Exception(e)
        }
    }
}
