package eg.edu.cu.csds.icare.home.screen.admin

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.constraintlayout.compose.ConstraintLayout
import eg.edu.cu.csds.icare.core.domain.model.AdminStatistics
import eg.edu.cu.csds.icare.core.ui.theme.L_PADDING
import eg.edu.cu.csds.icare.core.ui.theme.M_PADDING
import eg.edu.cu.csds.icare.core.ui.theme.STAT_CARD_SIZE
import eg.edu.cu.csds.icare.core.ui.theme.S_PADDING
import eg.edu.cu.csds.icare.core.ui.theme.SkyAccent
import eg.edu.cu.csds.icare.core.ui.theme.U_PADDING
import eg.edu.cu.csds.icare.core.ui.theme.XS_PADDING
import eg.edu.cu.csds.icare.core.ui.theme.backgroundColor
import eg.edu.cu.csds.icare.core.ui.theme.buttonBackgroundColor
import eg.edu.cu.csds.icare.core.ui.theme.helveticaFamily
import eg.edu.cu.csds.icare.core.ui.theme.textColor
import eg.edu.cu.csds.icare.core.ui.view.AnimatedButton
import eg.edu.cu.csds.icare.home.R
import eg.edu.cu.csds.icare.core.ui.R as CoreR

@Composable
fun AdminContent(
    stats: AdminStatistics,
    onSectionsAdminClicked: () -> Unit,
    modifier: Modifier = Modifier,
) {
    ConstraintLayout(
        modifier =
            modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(M_PADDING),
    ) {
        val (
            overview, usersCard, pending, confirmedCard, completedCard, cancelledCard,
            doctorsCard, pharmaciesCard, labsCard, scanCentersCard, button,
        ) = createRefs()

        Text(
            text = stringResource(R.string.overview),
            modifier =
                Modifier.constrainAs(overview) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                },
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            fontFamily = helveticaFamily,
            color = textColor,
        )
        StatCard(
            title = stringResource(CoreR.string.doctors),
            value = stats.doctors.toString(),
            modifier =
                Modifier.constrainAs(doctorsCard) {
                    top.linkTo(overview.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                },
        )

        StatCard(
            title = stringResource(R.string.users),
            value = stats.totalUsers.toString(),
            modifier =
                Modifier.constrainAs(usersCard) {
                    top.linkTo(doctorsCard.top)
                    start.linkTo(parent.start)
                    end.linkTo(doctorsCard.start)
                },
        )

        StatCard(
            title = stringResource(CoreR.string.pharmacies),
            value = stats.pharmacies.toString(),
            modifier =
                Modifier.constrainAs(pharmaciesCard) {
                    top.linkTo(doctorsCard.top)
                    start.linkTo(doctorsCard.end)
                    end.linkTo(parent.end)
                },
        )

        StatCard(
            title = stringResource(CoreR.string.scan_centers),
            value = stats.scanCenters.toString(),
            modifier =
                Modifier.constrainAs(scanCentersCard) {
                    top.linkTo(usersCard.bottom)
                    start.linkTo(usersCard.start)
                    end.linkTo(usersCard.end)
                },
        )

        StatCard(
            title = stringResource(CoreR.string.lab_centers),
            value = stats.labCenters.toString(),
            modifier =
                Modifier.constrainAs(labsCard) {
                    top.linkTo(doctorsCard.bottom)
                    start.linkTo(doctorsCard.start)
                    end.linkTo(doctorsCard.end)
                },
        )

        StatCard(
            title = stringResource(CoreR.string.pending),
            value = stats.pending.toString(),
            modifier =
                Modifier.constrainAs(pending) {
                    top.linkTo(pharmaciesCard.bottom)
                    start.linkTo(pharmaciesCard.start)
                    end.linkTo(pharmaciesCard.end)
                },
        )

        StatCard(
            title = stringResource(CoreR.string.confirmed),
            value = stats.confirmed.toString(),
            modifier =
                Modifier.constrainAs(confirmedCard) {
                    top.linkTo(scanCentersCard.bottom)
                    start.linkTo(scanCentersCard.start)
                    end.linkTo(scanCentersCard.end)
                },
        )

        StatCard(
            title = stringResource(CoreR.string.cancelled),
            value = stats.cancelled.toString(),
            modifier =
                Modifier.constrainAs(cancelledCard) {
                    top.linkTo(pending.bottom)
                    start.linkTo(pending.start)
                    end.linkTo(pending.end)
                },
        )

        StatCard(
            title = stringResource(CoreR.string.completed),
            value = stats.completed.toString(),
            modifier =
                Modifier.constrainAs(completedCard) {
                    top.linkTo(labsCard.bottom)
                    start.linkTo(labsCard.start)
                    end.linkTo(labsCard.end)
                },
        )

        AnimatedButton(
            modifier =
                Modifier
                    .constrainAs(button) {
                        top.linkTo(completedCard.bottom, margin = M_PADDING)
                        start.linkTo(completedCard.start)
                        end.linkTo(completedCard.end)
                    }.fillMaxWidth(fraction = 0.6f),
            text = stringResource(CoreR.string.sections_admin),
            color = buttonBackgroundColor,
            onClick = { onSectionsAdminClicked() },
        )
    }
}

@Composable
fun StatCard(
    title: String,
    value: String,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier =
            modifier
                .size(STAT_CARD_SIZE)
                .padding(XS_PADDING),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(XS_PADDING),
    ) {
        Column(
            modifier = Modifier.padding(vertical = L_PADDING, horizontal = U_PADDING),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = title,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                fontSize = MaterialTheme.typography.bodySmall.fontSize,
                fontFamily = helveticaFamily,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                color = Color.Black,
                modifier = Modifier.fillMaxWidth(),
            )

            Spacer(modifier = Modifier.height(S_PADDING))

            Text(
                text = value,
                fontSize = MaterialTheme.typography.bodyLarge.fontSize,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth(),
                color = SkyAccent,
            )
        }
    }
}

@Preview(showBackground = true)
@Preview(locale = "ar", showBackground = true)
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Preview(locale = "ar", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
internal fun AdminContentPreview() {
    Box(
        modifier =
            Modifier
                .fillMaxSize()
                .background(color = backgroundColor)
                .padding(XS_PADDING),
    ) {
        AdminContent(
            stats =
                AdminStatistics(
                    totalUsers = 2500,
                    doctors = 150,
                    pharmacies = 35,
                    scanCenters = 25,
                    labCenters = 15,
                    pending = 4590,
                    confirmed = 178,
                    completed = 2850,
                    cancelled = 12,
                ),
            onSectionsAdminClicked = {},
        )
    }
}
