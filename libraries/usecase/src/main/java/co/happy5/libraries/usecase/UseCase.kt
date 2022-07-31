package co.happy5.libraries.usecase

import kotlinx.coroutines.*

abstract class UseCase<out T : Any, Params>(
    private val defaultDispatchers: CoroutineDispatcher = Dispatchers.Default,
    mainDispatchers: CoroutineDispatcher = Dispatchers.Main
) {
    protected var parentJob = SupervisorJob()
    private val localScope = CoroutineScope(mainDispatchers + parentJob)

    abstract suspend fun executeOnBackground(param: Params): T

    private suspend fun executeCatchError(param: Params): Result<T> =
        withContext(defaultDispatchers) {
            try {
                Success(executeOnBackground(param))
            } catch (throwable: Throwable) {
                Fail(throwable)
            }
        }

    fun execute(onSuccess: (T) -> Unit, onError: (Throwable) -> Unit, param: Params) {
        cancelJobs()
        localScope.launchCatchError(block = {
            when (val result = executeCatchError(param)) {
                is Success -> onSuccess(result.data)
                is Fail -> onError(result.throwable)
            }
        }) {
            if (it !is CancellationException)
                onError(it)
        }
    }

    fun cancelJobs() {
        localScope.coroutineContext.cancelChildren()
    }
}