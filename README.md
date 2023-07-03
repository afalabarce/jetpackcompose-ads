# jetpackcompose-admob

Esta librería extiende a [JetpackComposeComponents](https://github.com/afalabarce/jetpackcompose), agregando los composables adecuados para mostrar
ads utilizando la plataforma de Admob.

Se exponen tres composables:

1. **AdmobInterstitialAdvertView**, muestra un intersticial clásico, a pantalla completa, su firma es la siguiente:

```kotlin
@Composable
fun AdmobInterstitialAdvertView(adUnitId: String, onInterstitial: (Boolean) -> Unit = {})
```

2. **AdmobRewardInterstitialAdvertView**,

```kotlin
@Composable
fun AdmobRewardInterstitialAdvertView(adUnitId: String, onRewardShown: () -> Unit, onReward: (Boolean) -> Unit = {})
```

3. **AdmobAdvertView**,

```kotlin
@Composable
fun AdmobAdvertView(adUnitId: String, modifier: Modifier = Modifier)
```

## Despliegue

**IMPORTANTE**: Debido a que se han agregado los composables para la gestión de ads, es preciso
que en el proyecto se realicen algunas acciones:
1. Agregar las siguientes dependencias en el build.gradle del proyecto (con versiones superiores, probablemente):

```groovy
implementation "com.google.firebase:firebase-appcheck-playintegrity:16.1.1"
implementation 'com.google.android.gms:play-services-ads:21.5.0'
```

2. Agregar en el Manifest.xml lo siguiente en la sección application (el id de ads proporcionado es de
   ejemplo, a partir de la [ayuda de admob](https://developers.google.com/admob/android/quick-start?hl=es#import_the_mobile_ads_sdk))
   en caso de disponer de una configuración válida, configurar adecuadamente:

```xml
<meta-data
    android:name="com.google.android.gms.ads.APPLICATION_ID"
    android:value="ca-app-pub-3940256099942544~3347511713"/> <!-- SAMPLE -->
```

3. Agregar en la MainActivity lo siguiente (en el método onCreate), a fin de "inicializar" el motor de admob.

```kotlin
MobileAds.initialize(this) {}
```

**¡ Y ESTO ES TODO!**

Como nota final, si deseas incluir este proyecto en tus apps, en tu build.gradle sólo deberás agregar lo siguiente:

```
implementation 'io.github.afalabarce:jetpackcompose-admob:1.7.4'
implementation 'io.github.afalabarce:jetpackcompose-wortise:1.7.4'
implementation 'io.github.afalabarce:jetpackcompose-applovin:1.7.4'
```

Si crees que estoy haciendo un buen trabajo y me merezco un café, puedes invitarme haciéndome un [PayPalMe!](https://www.paypal.com/paypalme/afalabarce)


