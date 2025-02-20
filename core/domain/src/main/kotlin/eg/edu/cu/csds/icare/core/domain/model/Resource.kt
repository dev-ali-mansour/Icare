package eg.edu.cu.csds.icare.core.domain.model

sealed class Resource<T>(
    val data: T? = null,
    val error: Throwable? = null,
) {
    class Unspecified<T>(
        data: T? = null,
    ) : Resource<T>(data)

    class Loading<T>(
        data: T? = null,
    ) : Resource<T>(data)

    class Success<T>(
        data: T,
    ) : Resource<T>(data)

    class Error<T>(
        throwable: Throwable?,
    ) : Resource<T>(null, throwable)
}
