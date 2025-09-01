package eg.edu.cu.csds.icare.appointment.screen

import android.content.Context
import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import eg.edu.cu.csds.icare.appointment.R
import eg.edu.cu.csds.icare.core.domain.util.getNextDaysTimestamps
import eg.edu.cu.csds.icare.core.ui.theme.CARD_ROUND_CORNER_SIZE
import eg.edu.cu.csds.icare.core.ui.theme.DATE_SELECTOR_HEIGHT
import eg.edu.cu.csds.icare.core.ui.theme.DATE_SELECTOR_ITEM_HEIGHT
import eg.edu.cu.csds.icare.core.ui.theme.DATE_SELECTOR_ITEM_WIDTH
import eg.edu.cu.csds.icare.core.ui.theme.S_PADDING
import eg.edu.cu.csds.icare.core.ui.theme.XS_PADDING
import eg.edu.cu.csds.icare.core.ui.theme.backgroundColor
import eg.edu.cu.csds.icare.core.ui.theme.helveticaFamily
import eg.edu.cu.csds.icare.core.ui.theme.textColor
import eg.edu.cu.csds.icare.core.data.util.getFormattedDate
import eg.edu.cu.csds.icare.core.data.util.getFormattedTime
import eg.edu.cu.csds.icare.core.ui.R as CoreR

@Composable
fun TimeSlotSelector(
    dates: List<Long>,
    selectedSlot: Long,
    onSlotSelected: (Long) -> Unit,
    modifier: Modifier = Modifier,
    context: Context = LocalContext.current,
) {
    ConstraintLayout(
        modifier
            .fillMaxWidth()
            .height(DATE_SELECTOR_HEIGHT)
            .padding(XS_PADDING),
    ) {
        val (title, prev, dateSelector, next) = createRefs()

        Text(
            text = stringResource(R.string.features_appointments_book_your_appointment_time),
            modifier =
                Modifier.constrainAs(title) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                },
            color = textColor,
            fontSize = MaterialTheme.typography.titleMedium.fontSize,
            fontFamily = helveticaFamily,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )

        Image(
            painter = painterResource(CoreR.drawable.baseline_arrow_back_ios_24),
            contentDescription = null,
            modifier =
                Modifier
                    .constrainAs(prev) {
                        top.linkTo(title.bottom)
                        start.linkTo(parent.start)
                        bottom.linkTo(parent.bottom)
                    }.height(DATE_SELECTOR_ITEM_HEIGHT),
            contentScale = ContentScale.Fit,
        )
        Image(
            painter = painterResource(CoreR.drawable.baseline_arrow_forward_ios_24),
            contentDescription = null,
            modifier =
                Modifier
                    .constrainAs(next) {
                        top.linkTo(prev.top)
                        end.linkTo(parent.end)
                        bottom.linkTo(parent.bottom)
                    }.height(DATE_SELECTOR_ITEM_HEIGHT),
            contentScale = ContentScale.Fit,
        )
        LazyRow(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier =
                Modifier.constrainAs(dateSelector) {
                    top.linkTo(prev.top)
                    start.linkTo(prev.end, margin = S_PADDING)
                    end.linkTo(next.start, margin = S_PADDING)
                    width = Dimension.fillToConstraints
                },
        ) {
            items(dates) { date ->
                var isSelected = date == selectedSlot

                Column(
                    modifier =
                        Modifier
                            .padding(XS_PADDING)
                            .clip(RoundedCornerShape(CARD_ROUND_CORNER_SIZE))
                            .background(
                                if (isSelected) {
                                    MaterialTheme.colorScheme.primary
                                } else {
                                    MaterialTheme.colorScheme.surfaceVariant
                                },
                            ).clickable {
                                onSlotSelected(date)
                            }.padding(S_PADDING)
                            .size(
                                width = DATE_SELECTOR_ITEM_WIDTH,
                                height = DATE_SELECTOR_ITEM_HEIGHT,
                            ),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                ) {
                    Text(
                        text = date.getFormattedDate(context, "EEEE"),
                        style = MaterialTheme.typography.titleMedium,
                        color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurface,
                    )
                    Text(
                        text = date.getFormattedDate(context, "dd/MM"),
                        style = MaterialTheme.typography.bodyMedium,
                        color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurface,
                    )
                    Text(
                        text = date.getFormattedTime(context),
                        style = MaterialTheme.typography.bodyMedium,
                        color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurface,
                    )
                }
            }
        }
    }
}

@Preview(name = "TimeSlotSelectorPreview", showBackground = true)
@Preview(name = "TimeSlotSelectorPreviewAr", showBackground = true, locale = "ar")
@Preview(
    name = "TimeSlotSelectorPreviewDark",
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
)
@Preview(
    name = "TimeSlotSelectorPreviewDarkAr",
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    locale = "ar",
)
@Composable
internal fun TimeSlotSelectorPreview() {
    Box(
        modifier =
            Modifier
                .fillMaxWidth()
                .background(backgroundColor),
    ) {
        TimeSlotSelector(
            dates = getNextDaysTimestamps(14),
            selectedSlot = System.currentTimeMillis(),
            onSlotSelected = {},
        )
    }
}
