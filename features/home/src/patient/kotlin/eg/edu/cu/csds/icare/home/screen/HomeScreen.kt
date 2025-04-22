package eg.edu.cu.csds.icare.home.screen

import android.app.Activity
import android.content.Context
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.firebase.auth.FirebaseAuth
import eg.edu.cu.csds.icare.admin.screen.clinic.ClinicViewModel
import eg.edu.cu.csds.icare.appointment.AppointmentsViewModel
import eg.edu.cu.csds.icare.core.ui.MainViewModel
import eg.edu.cu.csds.icare.core.ui.navigation.Screen
import eg.edu.cu.csds.icare.core.ui.navigation.Screen.Profile
import eg.edu.cu.csds.icare.core.ui.theme.M_PADDING
import eg.edu.cu.csds.icare.core.ui.theme.S_PADDING
import eg.edu.cu.csds.icare.core.ui.theme.contentColor
import eg.edu.cu.csds.icare.core.ui.theme.dialogTint
import eg.edu.cu.csds.icare.core.ui.theme.helveticaFamily
import eg.edu.cu.csds.icare.core.ui.theme.tintColor
import eg.edu.cu.csds.icare.core.ui.util.MediaHelper
import eg.edu.cu.csds.icare.core.ui.util.getBasicInfo
import eg.edu.cu.csds.icare.home.HomeViewModel
import eg.edu.cu.csds.icare.home.R
import kotlin.system.exitProcess

@Composable
internal fun HomeScreen(
    firebaseAuth: FirebaseAuth,
    mediaHelper: MediaHelper,
    mainViewModel: MainViewModel,
    homeViewModel: HomeViewModel,
    appointmentsViewModel: AppointmentsViewModel,
    clinicViewModel: ClinicViewModel,
    navigateToScreen: (Screen) -> Unit,
    onError: suspend (Throwable?) -> Unit,
    context: Context = LocalContext.current,
) {
    val appVersion: String =
        context.packageManager
            .getPackageInfo(context.packageName, 0)
            .versionName ?: ""
    var openDialog by homeViewModel.openDialog
    var isPlayed by homeViewModel.isPlayed
    val userResource by mainViewModel.currentUserFlow.collectAsStateWithLifecycle()
    val topDoctorsRes by clinicViewModel.topDoctorsResFlow.collectAsStateWithLifecycle()
    val appointmentsRes by appointmentsViewModel.appointmentsResFlow.collectAsStateWithLifecycle()
    val promotionRes by homeViewModel.promotionResFlow.collectAsStateWithLifecycle()
    val statusList by appointmentsViewModel.statusListState

    BackHandler {
        openDialog = true
    }

    userResource.data?.let {
        LaunchedEffect(key1 = isPlayed) {
            if (!isPlayed) {
                mediaHelper.play(R.raw.welcome)
                isPlayed = true
            }
        }
    }

    LaunchedEffect(key1 = Unit) {
        appointmentsViewModel.getPatientAppointments(context)
        clinicViewModel.listTopDoctors()
    }

    Column(
        modifier =
            Modifier
                .fillMaxSize(),
    ) {
        if (openDialog) {
            Dialog(
                onDismissRequest = {
                    openDialog = false
                },
            ) {
                Surface(
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .wrapContentHeight(),
                    shape = RoundedCornerShape(size = S_PADDING),
                ) {
                    Column(modifier = Modifier.padding(all = M_PADDING)) {
                        Row(
                            modifier =
                                Modifier
                                    .fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement =
                                Arrangement.spacedBy(
                                    space = 6.dp,
                                    alignment = Alignment.Start,
                                ),
                        ) {
                            Text(
                                text = stringResource(id = R.string.exit_dialog_title),
                                color = dialogTint.copy(alpha = 0.6f),
                                fontSize = MaterialTheme.typography.headlineSmall.fontSize,
                                fontWeight = FontWeight.Bold,
                                fontFamily = helveticaFamily,
                                overflow = TextOverflow.Ellipsis,
                            )
                        }

                        Text(
                            text = stringResource(id = R.string.exit_dialog),
                            color = dialogTint.copy(alpha = 0.6f),
                            fontSize = MaterialTheme.typography.titleMedium.fontSize,
                            fontFamily = helveticaFamily,
                            fontWeight = FontWeight.Bold,
                            overflow = TextOverflow.Ellipsis,
                        )
                        Spacer(modifier = Modifier.height(S_PADDING))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement =
                                Arrangement.spacedBy(
                                    space = 10.dp,
                                    alignment = Alignment.End,
                                ),
                        ) {
                            Box(
                                modifier =
                                    Modifier
                                        .clickable {
                                            openDialog = false
                                        }.border(
                                            width = 1.dp,
                                            color = contentColor,
                                            shape = RoundedCornerShape(S_PADDING),
                                        ).padding(
                                            top = 6.dp,
                                            bottom = 8.dp,
                                            start = 24.dp,
                                            end = 24.dp,
                                        ),
                            ) {
                                Text(
                                    text = stringResource(id = android.R.string.cancel),
                                    color = dialogTint,
                                    fontFamily = helveticaFamily,
                                )
                            }

                            Box(
                                modifier =
                                    Modifier
                                        .background(
                                            color = tintColor,
                                            shape = RoundedCornerShape(S_PADDING),
                                        ).border(
                                            width = 1.dp,
                                            color = contentColor,
                                            shape = RoundedCornerShape(S_PADDING),
                                        ).clickable {
                                            openDialog = false
                                            (context as Activity).finish()
                                            exitProcess(0)
                                        }.padding(
                                            top = 6.dp,
                                            bottom = 8.dp,
                                            start = 24.dp,
                                            end = 24.dp,
                                        ),
                            ) {
                                Text(
                                    text = stringResource(id = android.R.string.ok),
                                    color = Color.White,
                                    fontFamily = helveticaFamily,
                                )
                            }
                        }
                    }
                }
            }
        }
        HomeContent(
            firebaseUser = firebaseAuth.currentUser.getBasicInfo,
            userResource = userResource,
            topDoctorsRes = topDoctorsRes,
            appointmentsRes = appointmentsRes,
            promotionsRes = promotionRes,
            appVersion = appVersion,
            statusList = statusList,
            onUserClicked = { navigateToScreen(Profile) },
            onPromotionClicked = {},
            onServiceClicked = { navigateToScreen(it) },
            onDoctorClicked = { },
            onError = { onError(it) },
        )
    }
}
