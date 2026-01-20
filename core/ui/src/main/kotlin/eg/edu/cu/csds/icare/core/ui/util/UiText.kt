package eg.edu.cu.csds.icare.core.ui.util

import android.content.Context
import androidx.annotation.StringRes
import androidx.compose.runtime.Stable

@Stable
sealed interface UiText {
    data class DynamicString(
        val value: String,
    ) : UiText

    class StringResourceId(
        @param:StringRes val id: Int,
        val args: Array<Any> = arrayOf(),
    ) : UiText

    fun asString(context: Context): String =
        when (this) {
            is DynamicString -> value
            is StringResourceId -> context.getString(id, args)
        }
}
