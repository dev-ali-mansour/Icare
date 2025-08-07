package eg.edu.cu.csds.icare.core.ui.util

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import eg.edu.cu.csds.icare.core.domain.util.Constants.IMAGE_QUALITY
import java.io.ByteArrayOutputStream

fun Bitmap.toBase64(): String =
    Base64.encodeToString(
        ByteArrayOutputStream()
            .apply {
                compress(Bitmap.CompressFormat.PNG, IMAGE_QUALITY, this)
            }.toByteArray(),
        Base64.NO_WRAP,
    )

fun String.toBitmap(): Bitmap? =
    runCatching {
        val decodedBytes: ByteArray = Base64.decode(this, Base64.DEFAULT)
        BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
    }.getOrNull()
