package eg.edu.cu.csds.icare.feature.appointment.component

import android.content.Context
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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import eg.edu.cu.csds.icare.core.data.util.getFormattedDate
import eg.edu.cu.csds.icare.core.data.util.getFormattedTime
import eg.edu.cu.csds.icare.core.domain.util.getNextDaysTimestamps
import eg.edu.cu.csds.icare.core.ui.R.drawable
import eg.edu.cu.csds.icare.core.ui.theme.DATE_SELECTOR_HEIGHT
import eg.edu.cu.csds.icare.core.ui.theme.DATE_SELECTOR_ITEM_SIZE
import eg.edu.cu.csds.icare.core.ui.theme.IcareTheme
import eg.edu.cu.csds.icare.core.ui.theme.S_PADDING
import eg.edu.cu.csds.icare.core.ui.theme.XS_PADDING
import eg.edu.cu.csds.icare.core.ui.theme.helveticaFamily
import eg.edu.cu.csds.icare.core.ui.theme.textColor
import eg.edu.cu.csds.icare.core.ui.util.tooling.preview.PreviewArabicLightDark
import eg.edu.cu.csds.icare.feature.appointment.R

@Composable
fun TimeSlotSelector(
    slots: List<Long>,
    selectedSlot: Long,
    onSlotSelected: (Long) -> Unit,
    modifier: Modifier = Modifier,
) {
    val context: Context = LocalContext.current
    ConstraintLayout(
        modifier
            .fillMaxWidth()
            .height(DATE_SELECTOR_HEIGHT)
            .padding(XS_PADDING),
    ) {
        val (title, prev, dateSelector, next) = createRefs()

        Text(
            text = stringResource(R.string.feature_appointments_book_your_appointment_time),
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

        Icon(
            painter = painterResource(drawable.core_ui_baseline_arrow_back_ios_24),
            tint = MaterialTheme.colorScheme.onSurface,
            contentDescription = null,
            modifier =
                Modifier
                    .constrainAs(prev) {
                        top.linkTo(title.bottom)
                        start.linkTo(parent.start)
                        bottom.linkTo(parent.bottom)
                    }.height(DATE_SELECTOR_ITEM_SIZE),
        )
        Icon(
            painter = painterResource(drawable.core_ui_baseline_arrow_forward_ios_24),
            tint = MaterialTheme.colorScheme.onSurface,
            contentDescription = null,
            modifier =
                Modifier
                    .constrainAs(next) {
                        top.linkTo(prev.top)
                        end.linkTo(parent.end)
                        bottom.linkTo(parent.bottom)
                    }.height(DATE_SELECTOR_ITEM_SIZE),
        )
        LazyRow(
            horizontalArrangement = Arrangement.Absolute.spacedBy(XS_PADDING),
            modifier =
                Modifier.constrainAs(dateSelector) {
                    top.linkTo(prev.top)
                    start.linkTo(prev.end, margin = S_PADDING)
                    end.linkTo(next.start, margin = S_PADDING)
                    width = Dimension.fillToConstraints
                },
        ) {
            items(slots) { date ->
                val isSelected = date == selectedSlot
                val textColor =
                    if (isSelected) {
                        MaterialTheme.colorScheme.onPrimary
                    } else {
                        MaterialTheme.colorScheme.onSurface
                    }

                Card(
                    modifier =
                        Modifier
                            .clickable {
                                onSlotSelected(date)
                            },
                    colors =
                        CardDefaults.cardColors(
                            containerColor =
                                if (isSelected) {
                                    MaterialTheme.colorScheme.primary
                                } else {
                                    MaterialTheme.colorScheme.surface
                                },
                        ),
                    elevation = CardDefaults.cardElevation(XS_PADDING),
                ) {
                    Column(
                        modifier =
                            Modifier
                                .padding(XS_PADDING)
                                .size(DATE_SELECTOR_ITEM_SIZE),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                    ) {
                        Text(
                            text = date.getFormattedDate(context, "EEEE"),
                            style = MaterialTheme.typography.titleMedium,
                            color = textColor,
                        )
                        Text(
                            text = date.getFormattedDate(context, "dd/MM"),
                            style = MaterialTheme.typography.bodyMedium,
                            color = textColor,
                        )
                        Text(
                            text = date.getFormattedTime(context),
                            style = MaterialTheme.typography.bodyMedium,
                            color = textColor,
                        )
                    }
                }
            }
        }
    }
}

@PreviewLightDark
@PreviewArabicLightDark
@Composable
internal fun TimeSlotSelectorPreview() {
    IcareTheme {
        val slots = getNextDaysTimestamps(count = 14)
        Box(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.background),
        ) {
            TimeSlotSelector(
                slots = slots,
                selectedSlot = slots.first(),
                onSlotSelected = {},
            )
        }
    }
}
