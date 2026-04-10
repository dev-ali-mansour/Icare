package eg.edu.cu.csds.icare.core.domain.util

sealed interface RequestState<out D, out E : Error> {
    data class Success<out D>(
        val data: D,
    ) : RequestState<D, Nothing>

    data class Error<out E : eg.edu.cu.csds.icare.core.domain.util.Error>(
        val error: E,
    ) : RequestState<Nothing, E>
}

inline fun <T, E : Error, R> RequestState<T, E>.map(map: (T) -> R): RequestState<R, E> =
    when (this) {
        is RequestState.Error -> RequestState.Error(error)
        is RequestState.Success -> RequestState.Success(map(data))
    }

fun <T, E : Error> RequestState<T, E>.asEmptyDataResult(): EmptyResult<E> = map { }

inline fun <T, E : Error> RequestState<T, E>.onSuccess(action: (T) -> Unit): RequestState<T, E> =
    when (this) {
        is RequestState.Error -> {
            this
        }

        is RequestState.Success -> {
            action(data)
            this
        }
    }

inline fun <T, E : Error> RequestState<T, E>.onError(action: (E) -> Unit): RequestState<T, E> =
    when (this) {
        is RequestState.Error -> {
            action(error)
            this
        }

        is RequestState.Success -> {
            this
        }
    }

typealias EmptyResult<E> = RequestState<Unit, E>
