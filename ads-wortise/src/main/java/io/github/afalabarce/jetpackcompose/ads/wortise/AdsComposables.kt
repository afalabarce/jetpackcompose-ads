package io.github.afalabarce.jetpackcompose.ads.wortise

import android.util.Log
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
import com.wortise.ads.AdError
import com.wortise.ads.AdSize
import com.wortise.ads.banner.BannerAd
import com.wortise.ads.interstitial.InterstitialAd
import com.wortise.ads.rewarded.RewardedAd
import com.wortise.ads.rewarded.models.Reward

@Composable
fun WortiseAdvertView(modifier: Modifier = Modifier, adUnitId: String){
    val context = LocalContext.current
    val isInEditMode = LocalInspectionMode.current
    var retryAttempt: Int = 0

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
                    BannerAd(context).apply {
                        layoutParams = ViewGroup.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT
                        )
                        adSize = AdSize.HEIGHT_50
                        this.adUnitId = adUnitId

                        loadAd()
                    }
                }
            )
        }
    }
}

@Composable
fun WortiseInterstitialAdvertView(adUnitId: String, onReward: (Boolean) -> Unit = {}){
    val context = LocalContext.current

    LaunchedEffect(key1 = "Reward Word"){
        InterstitialAd(
            context,
            adUnitId
        ).apply {
            listener = object : InterstitialAd.Listener {
                override fun onInterstitialLoaded(ad: InterstitialAd) {
                    super.onInterstitialLoaded(ad)
                    if (ad.isAvailable)
                        this@apply.showAd()
                }


                override fun onInterstitialFailed(ad: InterstitialAd, error: AdError) {
                    super.onInterstitialFailed(ad, error)
                    Log.e("WortiseRewardError", error.toString())
                }
            }
        }.also {
            it.loadAd()
            //it.showAd()
        }
    }
}

@Composable
fun WortiseRewardAdvertView(adUnitId: String, onReward: (Boolean) -> Unit = {}){
    val context = LocalContext.current

    LaunchedEffect(key1 = "Reward Word"){
        RewardedAd(
            context,
            adUnitId
        ).apply {
            listener = object : RewardedAd.Listener {
                override fun onRewardedLoaded(ad: RewardedAd) {
                    if (ad.isAvailable)
                        this@apply.showAd()
                }

                override fun onRewardedFailed(
                    ad: RewardedAd,
                    error: AdError
                ) {
                    Log.e("WortiseRewardError", error.toString())
                }

                override fun onRewardedCompleted(
                    ad: RewardedAd,
                    reward: Reward
                ) {
                    onReward(true)
                }
            }
        }.also {
            it.loadAd()
            //it.showAd()
        }
    }
}

 