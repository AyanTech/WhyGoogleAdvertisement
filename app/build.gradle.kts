plugins {
    id(Plugins.androidApplication)
    kotlin(KotlinPlugins.android)
}

android {
    compileSdk = Application.compileSdk

    defaultConfig {
        applicationId = Application.appId
        minSdk = Application.minSdk
        targetSdk = Application.targetSdk
        versionCode = Application.versionCode
        versionName = Application.versionName

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8

    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {

    implementation(AndroidX.coreKtx)
    compileOnly(ad.AdmobAdvertisement.admobAdvertisement)
    implementation(AndroidX.appCompatActivity)
    implementation(MaterialDesign.materialDesign)
    implementation(Messaging.messaging)
    implementation(AndroidX.constraintLayout)
    implementation(project(mapOf("path" to ":whyoogle-ads")))
    testImplementation(Junit.junit)
    androidTestImplementation(Junit.junitTestExt)
    androidTestImplementation(Junit.junitTestExtKtx)
    androidTestImplementation(Espresso.espresso)

}