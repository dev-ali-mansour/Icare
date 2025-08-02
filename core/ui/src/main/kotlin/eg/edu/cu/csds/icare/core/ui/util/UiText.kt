package eg.edu.cu.csds.icare.core.ui.util

import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource

sealed interface UiText {
    data class DynamicString(
        val value: String,
    ) : UiText

    class StringResourceId(
        @param:StringRes val id: Int,
        val args: Array<Any> = arrayOf(),
    ) : UiText

    @Composable
    fun asString(): String =
        when (this) {
            is DynamicString -> value
            is StringResourceId -> stringResource(id = id, formatArgs = args)
        }
}
