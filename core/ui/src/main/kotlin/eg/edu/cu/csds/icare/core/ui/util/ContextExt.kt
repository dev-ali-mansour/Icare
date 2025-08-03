package eg.edu.cu.csds.icare.core.ui.util

import android.annotation.SuppressLint
import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Context.RECEIVER_EXPORTED
import android.content.ContextWrapper
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.ApplicationInfo
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.location.LocationManager
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri
import android.os.Build
import android.os.PowerManager
import android.provider.Settings
import android.widget.Toast
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.result.ActivityResult
import androidx.browser.customtabs.CustomTabColorSchemeParams
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.content.ContextCompat
import androidx.core.location.LocationManagerCompat
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import eg.edu.cu.csds.icare.core.data.BuildConfig
import eg.edu.cu.csds.icare.core.domain.model.AccessTokenExpiredException
import eg.edu.cu.csds.icare.core.domain.model.SettingsItem
import eg.edu.cu.csds.icare.core.domain.model.UserNotAuthorizedException
import eg.edu.cu.csds.icare.core.domain.util.Constants
import eg.edu.cu.csds.icare.core.ui.R
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

val Context.activity: Activity
    get() {
        var context = this
        while (context is ContextWrapper) {
            if (context is Activity) return context
            context = context.baseContext
        }
        error("No activity found in context hierarchy!")
    }

fun Context.isInternetAvailable(): Boolean {
    val connectivityManager =
        getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val networkCapabilities = connectivityManager.activeNetwork ?: return false
    val actNw =
        connectivityManager.getNetworkCapabilities(networkCapabilities) ?: return false
    return when {
        actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
        actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
        actNw.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
        else -> false
    }
}

val Context.isLocationEnabled: Boolean
    get() {
        val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return LocationManagerCompat.isLocationEnabled(locationManager)
    }

fun Context.getSettingsItems(): ArrayList<SettingsItem> =
    arrayListOf(
        SettingsItem(Constants.CHANGE_LANGUAGE_CODE, getString(R.string.choose_app_lang)),
        SettingsItem(Constants.SHARE_CODE, getString(R.string.app_share)),
        SettingsItem(Constants.RATE_CODE, getString(R.string.app_rate)),
        SettingsItem(Constants.CONTACT_CODE, getString(R.string.contact_us)),
        SettingsItem(Constants.ERROR_REPORTING_CODE, getString(R.string.report_us)),
        SettingsItem(Constants.APPS_CODE, getString(R.string.our_apps)),
        SettingsItem(Constants.POLICY_CODE, getString(R.string.privacy_policy)),
        SettingsItem(Constants.ABOUT_CODE, getString(R.string.about_us)),
    )

fun Context.getErrorMessage(error: Throwable?): String =
    when (error) {
        is SocketTimeoutException, is ConnectException, is UnknownHostException, is FirebaseNetworkException ->
            getString(R.string.error_server)

        is UserNotAuthorizedException, is AccessTokenExpiredException ->
            getString(R.string.error_user_not_authorized)

        is FirebaseAuthUserCollisionException -> getString(R.string.error_user_collision)
        is FirebaseAuthInvalidUserException -> getString(R.string.error_invalid_credentials)
//        is CustomerNotFoundException -> getString(R.string.customer_error)

        else -> error?.message.toString()
    }

fun Context.appShare() {
    runCatching {
        val sendIntent = Intent()
        sendIntent.action = Intent.ACTION_SEND
        sendIntent.type = "text/plain"
        sendIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.app_name))
        val builder = StringBuilder("${getString(R.string.share_app_text)}\n")
        builder.append(getString(R.string.app_url))
        builder.append(packageName)
        sendIntent.putExtra(
            Intent.EXTRA_TEXT,
            builder.toString(),
        )
        startActivity(
            Intent.createChooser(
                sendIntent,
                getString(R.string.app_share),
            ),
        )
    }.onFailure { t -> t.printStackTrace() }
}

fun Context.appRate() {
    runCatching {
        startActivity(
            Intent(
                Intent.ACTION_VIEW,
                Uri.parse("${getString(R.string.app_market_url)}$packageName"),
            ),
        )
    }.onFailure { t ->
        when (t) {
            is ActivityNotFoundException ->
                startActivity(
                    Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("${getString(R.string.app_url)}$packageName"),
                    ),
                )

            else -> t.printStackTrace()
        }
    }
}

fun Context.contactUs() {
    runCatching {
        val supportMail = arrayOf(getString(R.string.support_email))
        val intent =
            Intent(Intent.ACTION_SENDTO).apply {
                data = Uri.parse("mailto:")
                putExtra(Intent.EXTRA_EMAIL, supportMail)
                putExtra(
                    Intent.EXTRA_SUBJECT,
                    "${getString(R.string.app_name)} - ${getString(R.string.contact_us)}",
                )
            }
        startActivity(intent)
    }.onFailure { t ->
        when (t) {
            is ActivityNotFoundException ->
                Toast
                    .makeText(
                        applicationContext,
                        getString(R.string.no_email_client),
                        Toast.LENGTH_SHORT,
                    ).show()

            else -> t.printStackTrace()
        }
    }
}

fun Context.reportError() {
    runCatching {
        val pInfo: PackageInfo =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                packageManager.getPackageInfo(packageName, PackageManager.PackageInfoFlags.of(0))
            } else {
                packageManager.getPackageInfo(packageName, 0)
            }
        val builder =
            StringBuilder("${getString(R.string.app_version)}${pInfo.versionName}\n")
        builder.append("${getString(R.string.android_version)}${Build.VERSION.SDK_INT}\n")
        builder.append("${getString(R.string.enter_your_phone)}\n\n")
        builder.append("${getString(R.string.input_error_details)}\n")
        val supportMail = arrayOf(getString(R.string.support_email))

        val intent =
            Intent(Intent.ACTION_SENDTO).apply {
                data = Uri.parse("mailto:")
                putExtra(Intent.EXTRA_EMAIL, supportMail)
                putExtra(
                    Intent.EXTRA_SUBJECT,
                    "${getString(R.string.app_name)} - ${getString(R.string.report_us)}",
                )
                putExtra(Intent.EXTRA_TEXT, builder.toString())
            }
        startActivity(intent)
    }.onFailure { t ->
        when (t) {
            is ActivityNotFoundException ->
                Toast
                    .makeText(
                        applicationContext,
                        getString(R.string.no_email_client),
                        Toast.LENGTH_SHORT,
                    ).show()

            else -> t.printStackTrace()
        }
    }
}

fun Context.showOurApps() {
    runCatching {
        startActivity(
            Intent(
                Intent.ACTION_VIEW,
                Uri.parse(getString(R.string.our_apps_market_url)),
            ),
        )
    }.onFailure { t ->
        when (t) {
            is ActivityNotFoundException ->
                startActivity(
                    Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse(getString(R.string.our_apps_url)),
                    ),
                )

            else -> t.printStackTrace()
        }
    }
}

fun Context.showPrivacyPolicy() {
    openLink(getString(R.string.privacy_policy_url))
}

fun Context.isBatteryOptimized(): Boolean {
    val powerManager = getSystemService(Context.POWER_SERVICE) as PowerManager
    val name = packageName
    return !powerManager.isIgnoringBatteryOptimizations(name)
}

fun Context.checkBattery() {
    if (isBatteryOptimized()) {
        val name = resources.getString(R.string.app_name)
        Toast
            .makeText(
                this,
                "Battery optimization -> All apps -> $name -> Don't optimize",
                Toast.LENGTH_LONG,
            ).show()
        val intent = Intent(Settings.ACTION_IGNORE_BATTERY_OPTIMIZATION_SETTINGS)
        startActivity(intent)
    }
}

fun Context.isDebuggable(): Boolean = applicationContext.applicationInfo.flags and ApplicationInfo.FLAG_DEBUGGABLE != 0

@SuppressLint("UnspecifiedRegisterReceiverFlag")
fun Context.registerReceiver(
    intentFilter: IntentFilter,
    onReceive: (intent: Intent) -> Unit,
): BroadcastReceiver {
    val receiver =
        object : BroadcastReceiver() {
            override fun onReceive(
                context: Context,
                intent: Intent,
            ) {
                onReceive(intent)
            }
        }
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        this.registerReceiver(receiver, intentFilter, RECEIVER_EXPORTED)
    } else {
        registerReceiver(receiver, intentFilter)
    }
    return receiver
}

fun Context.shareFile(
    uri: Uri,
    type: String,
) {
    runCatching {
        val sendIntent = Intent()
        sendIntent.action = Intent.ACTION_SEND
        sendIntent.type = type
        sendIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.app_name))
        sendIntent.putExtra(Intent.EXTRA_STREAM, uri)
        startActivity(
            Intent.createChooser(
                sendIntent,
                getString(R.string.share),
            ),
        )
    }.onFailure { t -> t.printStackTrace() }
}

fun Context.openLink(url: String) {
    runCatching {
        val linkUri = Uri.parse(url)
        val intentBuilder = CustomTabsIntent.Builder()
        val params =
            CustomTabColorSchemeParams
                .Builder()
                .setNavigationBarColor(ContextCompat.getColor(this, R.color.blue_500))
                .setToolbarColor(ContextCompat.getColor(this, R.color.blue_500))
                .setSecondaryToolbarColor(ContextCompat.getColor(this, R.color.blue_500))
                .build()
        intentBuilder.apply {
            setColorSchemeParams(CustomTabsIntent.COLOR_SCHEME_DARK, params)
            setStartAnimations(
                this@openLink,
                android.R.anim.slide_out_right,
                android.R.anim.slide_in_left,
            )
            setExitAnimations(
                this@openLink,
                android.R.anim.slide_in_left,
                android.R.anim.slide_out_right,
            )
        }
        val customTabsIntent = intentBuilder.build()
        customTabsIntent.launchUrl(this, linkUri)
    }.onFailure {
        Toast.makeText(this, it.localizedMessage, Toast.LENGTH_SHORT).show()
    }
}

fun Context.signInWithGoogle(launcher: ManagedActivityResultLauncher<Intent, ActivityResult>) {
    val gso =
        GoogleSignInOptions
            .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(BuildConfig.WEB_CLIENT_ID)
            .requestEmail()
            .build()
    val googleSignInClient = GoogleSignIn.getClient(this, gso)
    googleSignInClient.revokeAccess()
    launcher.launch(googleSignInClient.signInIntent)
}

fun Context.linkGoogleAccount(launcher: ManagedActivityResultLauncher<Intent, ActivityResult>) {
    val gso =
        GoogleSignInOptions
            .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(BuildConfig.WEB_CLIENT_ID)
            .requestEmail()
            .build()
    val googleSignInClient = GoogleSignIn.getClient(this, gso)
    googleSignInClient.revokeAccess()
    launcher.launch(googleSignInClient.signInIntent)
}
