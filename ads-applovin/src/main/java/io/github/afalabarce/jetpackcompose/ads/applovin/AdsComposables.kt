package io.github.afalabarce.jetpackcompose.ads.applovin

import android.app.Activity
import android.os.Handler
import android.os.Looper
import android.view.ViewGroup
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.applovin.mediation.MaxAd
import com.applovin.mediation.MaxAdListener
import com.applovin.mediation.MaxAdViewAdListener
import com.applovin.mediation.MaxError
import com.applovin.mediation.MaxReward
import com.applovin.mediation.MaxRewardedAdListener
import com.applovin.mediation.ads.MaxAdView
import com.applovin.mediation.ads.MaxInterstitialAd
import com.applovin.mediation.ads.MaxRewardedAd
import java.util.concurrent.TimeUnit
import kotlin.math.min
import kotlin.math.pow

@Composable
fun AppLovinRewardAdvertView(adUnitId: String, onReward: (Boolean) -> Unit = {}){
    val context = LocalContext.current
    var retryAttempt = 0

    LaunchedEffect(key1 = "Reward Word"){
        MaxRewardedAd.getInstance(adUnitId, context as Activity).apply {
            setListener(object : MaxRewardedAdListener {
                override fun onAdLoaded(ad: MaxAd?) {
                    retryAttempt = 0
                    this@apply.showAd()

                }

                override fun onAdDisplayed(ad: MaxAd?) {

                }

                override fun onAdHidden(ad: MaxAd?) {
                }

                override fun onAdClicked(ad: MaxAd?) {
                    onReward(true)
                }

                override fun onAdLoadFailed(adUnitId: String?, error: MaxError?) {
                    // Rewarded ad failed to load
                    // We recommend retrying with exponentially higher delays up to a maximum delay (in this case 64 seconds)

                    // Rewarded ad failed to load
                    // We recommend retrying with exponentially higher delays up to a maximum delay (in this case 64 seconds)
                    if (retryAttempt < 10) {
                        retryAttempt++
                        val delayMillis: Long = TimeUnit.SECONDS.toMillis(
                            2.0.pow(6.coerceAtMost(retryAttempt).toDouble()).toLong()
                        )

                        Handler(Looper.getMainLooper()).postDelayed(
                            { this@apply.loadAd() },
                            delayMillis
                        )
                    }
                }

                override fun onAdDisplayFailed(ad: MaxAd?, error: MaxError?) {
                    // Rewarded ad failed to display. We recommend loading the next ad
                    this@apply.loadAd()
                }

                override fun onRewardedVideoStarted(ad: MaxAd?) {

                }

                override fun onRewardedVideoCompleted(ad: MaxAd?) {

                }

                override fun onUserRewarded(ad: MaxAd?, reward: MaxReward?) {
                    onReward(reward != null)
                }
            })
            this.loadAd()
        }

    }
}

@Composable
fun AppLovinInterstitialAdvertView(adUnitId: String, onReward: (Boolean) -> Unit = {}){
    val context = LocalContext.current
    var retryAttempt = 0

    LaunchedEffect(key1 = "InterstitialAd"){
        MaxInterstitialAd(adUnitId, context as Activity).apply {

            setListener(object : MaxAdListener {
                override fun onAdLoaded(ad: MaxAd?) {
                    retryAttempt = 0
                    this@apply.showAd()

                }

                override fun onAdDisplayed(ad: MaxAd?) {

                }

                override fun onAdHidden(ad: MaxAd?) {
                }

                override fun onAdClicked(ad: MaxAd?) {
                    onReward(true)
                }

                override fun onAdLoadFailed(adUnitId: String?, error: MaxError?) {
                    // Rewarded ad failed to load
                    // We recommend retrying with exponentially higher delays up to a maximum delay (in this case 64 seconds)

                    // Rewarded ad failed to load
                    // We recommend retrying with exponentially higher delays up to a maximum delay (in this case 64 seconds)
                    if (retryAttempt < 10) {
                        retryAttempt++
                        val delayMillis: Long = TimeUnit.SECONDS.toMillis(
                            2.0.pow(6.coerceAtMost(retryAttempt).toDouble()).toLong()
                        )

                        Handler(Looper.getMainLooper()).postDelayed(
                            { this@apply.loadAd() },
                            delayMillis
                        )
                    }
                }

                override fun onAdDisplayFailed(ad: MaxAd?, error: MaxError?) {
                    // Rewarded ad failed to display. We recommend loading the next ad
                    this@apply.loadAd()
                }
            })
            this.loadAd()
        }

    }
}

@Composable
fun AppLovinAdvertView(adUnitId: String, modifier: Modifier = Modifier){
    val isInEditMode = LocalInspectionMode.current
    var retryAttempt = 0

    Column(modifier = modifier.height(54.dp)) {
        if (isInEditMode) {
            Text(
                modifier = modifier
                    .background(Color.Red)
                    .padding(horizontal = 2.dp, vertical = 6.dp),
                textAlign = TextAlign.Center,
                color = Color.White,
                text = "Advert Here",
            )
        } else {
            AndroidView(
                modifier = modifier,
                factory = { context ->
                    MaxAdView(adUnitId, context).apply {
                        layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                        setListener(object : MaxAdViewAdListener {
                            override fun onAdLoaded(ad: MaxAd?) {
                                retryAttempt = 0
                                startAutoRefresh()
                            }

                            override fun onAdDisplayed(ad: MaxAd?) {

                            }

                            override fun onAdHidden(ad: MaxAd?) {

                            }

                            override fun onAdClicked(ad: MaxAd?) {

                            }

                            override fun onAdLoadFailed(adUnitId: String?, error: MaxError?) {
                                // We recommend retrying with exponentially higher delays up to a maximum delay (in this case 64 seconds)

                                // Rewarded ad failed to load
                                // We recommend retrying with exponentially higher delays up to a maximum delay (in this case 64 seconds)
                                retryAttempt++
                                val delayMillis: Long = TimeUnit.SECONDS.toMillis(
                                    2.0.pow(min(6, retryAttempt).toDouble()).toLong()
                                )

                                Handler(Looper.getMainLooper()).postDelayed({ this@apply.loadAd() }, delayMillis)
                            }

                            override fun onAdDisplayFailed(ad: MaxAd?, error: MaxError?) {
                                this@apply.loadAd()
                            }

                            override fun onAdExpanded(ad: MaxAd?) {

                            }

                            override fun onAdCollapsed(ad: MaxAd?) {

                            }

                        })
                        loadAd()
                    }
                }
            )
        }
    }
}

