# WhyGoogle Advertisement

## **Here You Can Found Some Useful Information About How :**

1. **Add This Library To Your Project**
2. **This Library Works**

## Add Library To Your Project **(Kotlin DSL)** 

1. ### **build.gradle.kts**

**Refer to the following page for the latest version : [Jitpack Page](https://jitpack.io/#alitafreshi/why-google-advertisement)**

```php
dependencies {

    implementation(com.github.alitafreshi:why-google-advertisement:$advertisementVersion)

}
```

**In projects that we have admob advertisement we should add the admob dependency individualy like below :**

```php
dependencies {

   "playstoreImplementation"(com.google.android.gms:play-services-ads:$admobAdVersion)

}
```
**Important Note: Don't Forget To Init [AdvertisementCore](https://github.com/alitafreshi/why-google-advertisement/blob/main/whyoogle-ads/src/main/java/ir/tafreshiali/whyoogle_ads/AdvertisementCore.kt),[AdiveryAdvertisementKey](https://github.com/alitafreshi/why-google-advertisement/blob/main/whyoogle-ads/src/main/java/ir/tafreshiali/whyoogle_ads/datasource/shared_preference/AdiveryAdvertisementKey.kt),[AdmobAdvertisementKey](https://github.com/alitafreshi/why-google-advertisement/blob/main/whyoogle-ads/src/main/java/ir/tafreshiali/whyoogle_ads/datasource/shared_preference/AdmobAdvertisementKey.kt) and [ApplicationAdvertisementType](https://github.com/alitafreshi/why-google-advertisement/blob/main/whyoogle-ads/src/main/java/ir/tafreshiali/whyoogle_ads/datasource/shared_preference/ApplicationAdvertisementType.kt) Like Below In Application Class :**

```kotlin
override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
        AdvertisementCore.init(application = this)
        AdiveryAdvertisementKey.init(this)
        AdmobAdvertisementKey.init(this)
        ApplicationAdvertisementType.init(this)
    }
```


2. ### **Adding Your Project To Firebase And Admob**


![goole play json file](https://github.com/alitafreshi/why-google-advertisement/blob/main/screen_shots/google_play_screen_shot.png)


## Library Working Progress Diagram



![library wrking progress diagram](https://github.com/alitafreshi/why-google-advertisement/blob/main/screen_shots/Ayan%20Advertisement%20Diagram.svg)
