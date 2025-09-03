package eg.edu.cu.csds.icare.core.ui.view

import android.os.Build
import android.os.Build.VERSION.SDK_INT
import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import coil.ImageLoader
import coil.compose.rememberAsyncImagePainter
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import coil.request.ImageRequest
import eg.edu.cu.csds.icare.core.ui.R

@Composable
fun GifImageLoader(
    modifier: Modifier = Modifier,
    @DrawableRes
    drawableRes: Int = R.drawable.core_ui_success,
) {
    val context = LocalContext.current
    val imageLoader =
        ImageLoader
            .Builder(context)
            .components {
                if (SDK_INT >= Build.VERSION_CODES.P) {
                    add(ImageDecoderDecoder.Factory())
                } else {
                    add(GifDecoder.Factory())
                }
            }.build()
    Image(
        painter =
            rememberAsyncImagePainter(
                ImageRequest
                    .Builder(context)
                    .data(data = drawableRes)
                    .apply(block = {
                        size(coil.size.Size.ORIGINAL)
                    })
                    .build(),
                imageLoader = imageLoader,
            ),
        contentDescription = null,
        modifier = modifier.fillMaxWidth(),
    )
}
