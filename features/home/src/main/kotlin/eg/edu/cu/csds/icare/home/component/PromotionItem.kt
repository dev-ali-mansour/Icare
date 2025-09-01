package eg.edu.cu.csds.icare.home.component

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import coil.compose.AsyncImage
import coil.request.ImageRequest
import eg.edu.cu.csds.icare.core.domain.model.Promotion
import eg.edu.cu.csds.icare.core.ui.theme.BurntOrange
import eg.edu.cu.csds.icare.core.ui.theme.CARD_ROUND_CORNER_SIZE
import eg.edu.cu.csds.icare.core.ui.theme.GRID_ITEM_WIDTH
import eg.edu.cu.csds.icare.core.ui.theme.L_PADDING
import eg.edu.cu.csds.icare.core.ui.theme.PROMOTION_ITEM_HEIGHT
import eg.edu.cu.csds.icare.core.ui.theme.XS_PADDING
import eg.edu.cu.csds.icare.core.ui.theme.helveticaFamily
import eg.edu.cu.csds.icare.home.R
import eg.edu.cu.csds.icare.core.ui.R as CoreR

@Composable
fun PromotionItem(promotion: Promotion) {
    Card(
        modifier =
            Modifier
                .width(GRID_ITEM_WIDTH)
                .height(PROMOTION_ITEM_HEIGHT),
        shape = RoundedCornerShape(CARD_ROUND_CORNER_SIZE),
    ) {
        ConstraintLayout(modifier = Modifier.fillMaxSize()) {
            val (image, percentage) = createRefs()
            AsyncImage(
                model =
                    ImageRequest
                        .Builder(LocalContext.current)
                        .data(promotion.imageUrl)
                        .crossfade(true)
                        .build(),
                placeholder = painterResource(CoreR.drawable.placeholder),
                contentDescription = null,
                modifier =
                    Modifier.constrainAs(image) {
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        bottom.linkTo(parent.bottom)
                        width = Dimension.fillToConstraints
                        height = Dimension.fillToConstraints
                    },
                error = painterResource(R.drawable.features_home_promo_placeholder),
                contentScale = ContentScale.Crop,
            )

            Card(
                modifier =
                    Modifier
                        .constrainAs(percentage) {
                            top.linkTo(parent.top, margin = L_PADDING)
                            start.linkTo(parent.start)
                        },
                shape =
                    RoundedCornerShape(
                        topEnd = CARD_ROUND_CORNER_SIZE,
                        bottomEnd = CARD_ROUND_CORNER_SIZE,
                    ),
                colors =
                    CardDefaults.cardColors(
                        containerColor = BurntOrange,
                    ),
            ) {
                Text(
                    text = promotion.discount,
                    modifier = Modifier.padding(XS_PADDING),
                    fontWeight = FontWeight.Bold,
                    fontSize = MaterialTheme.typography.bodyMedium.fontSize,
                    fontFamily = helveticaFamily,
                    color = Color.White,
                    maxLines = 1,
                )
            }
        }
    }
}
