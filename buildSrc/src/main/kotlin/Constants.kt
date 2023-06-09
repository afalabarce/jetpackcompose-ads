object Constants {
    const val versionCode = 174
    const val versionName = "1.7.4"
    const val minSdk = 24
    const val targetAndCompilingSdk = 33
    const val composeVersion = "1.5.0-alpha04"
    const val composeCompilerVersion = "1.4.4"
    const val androidxCoreKtxVersion = "1.10.1"
    const val androidxAppCompatVersion = "1.6.1"
    const val composeMaterial3Version = "1.1.0"
    const val firebaseBomVersion = "30.3.1"
    const val firebaseAppCheckDebugVersion = "17.0.0"
    const val firebaseMessagingVersion = "23.1.2"
    const val gmsPlayServicesAdsVersion = "22.0.0"
    const val gmsPlayServicesAdsVersionIdentifierVersion = "18.0.1"
    const val lifeCycleRuntimeVersion = "2.6.1"
    const val activityComposeVersion = "1.7.1"
    const val constraintLayoutComposeVersion = "1.0.1"
}

object SonatypePublishingCommon {
    const val publishedGroupId = "io.github.afalabarce"
    const val libraryVersionId = Constants.versionCode
    const val libraryVersionCode = Constants.versionName
    const val gitPath = "github.com/afalabarce/jetpackcompose-admob.git"
    const val gitUrl = "https://$gitPath"
    const val developerId = "afalabarce"
    const val developerName = "Antonio Fdez. Alabarce"
    const val developerEmail = "afalabarce@gmail.com"
    const val licenseName = "The Apache Software License, Version 2.0"
    const val licenseUrl = "http://www.apache.org/licenses/LICENSE-2.0.txt"
    val allLicenses = arrayOf ("Apache-2.0")
}
object SonatypePublishingAdmob {

    const val libraryName = "jetpackcompose-admob"
    const val artifact = "jetpackcompose-admob"
    const val libraryDescription = "Another Project for Jetpack Compose Composable Library (for admob publishers)"
}

object SonatypePublishingWortise {
    const val libraryName = "jetpackcompose-wortise"
    const val artifact = "jetpackcompose-wortise"
    const val libraryDescription = "Another Project for Jetpack Compose Composable Library (for wortise publishers)"
}

object SonatypePublishingAppLovin {
    const val libraryName = "jetpackcompose-applovin"
    const val artifact = "jetpackcompose-applovin"
    const val libraryDescription = "Another Project for Jetpack Compose Composable Library (for applovin publishers)"
}