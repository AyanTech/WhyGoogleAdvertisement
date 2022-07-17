plugins {
    id(Plugins.androidLibrary)
    kotlin(KotlinPlugins.android)
    id(KotlinPlugins.maven)
}

android {
    compileSdk = Application.compileSdk

    defaultConfig {
        minSdk = Application.minSdk
        targetSdk = Application.targetSdk

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
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

    buildFeatures {
        viewBinding = true
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
    implementation(AndroidX.appCompatActivity)
    implementation(MaterialDesign.materialDesign)
    implementation(AndroidX.constraintLayout)
    testImplementation(Junit.junit)
    androidTestImplementation(Junit.junitTestExt)
    androidTestImplementation(Junit.junitTestExtKtx)
    androidTestImplementation(Espresso.espresso)

    //Ayan - Advertisement
    api(ad.AdmobAdvertisement.admobAdvertisement)
    api(ad.AdiveryAdvertisement.advertisement)

    //Ayan - Why google
    implementation(ayan.WhyGoogle.whyGoogle)

    //Ayan - NetWorking
    implementation(ayan.Networking.gson)
    implementation(ayan.Networking.gsonConvertor)
    implementation(ayan.Networking.okhttp)
    implementation(ayan.Networking.pishkhanNetworking)

    //Ayan - Core
    implementation(ayan.PishkhanCore.pishkhanCore)


}

afterEvaluate {
    publishing {
        publications {
            // Creates a Maven publication called "release".
            register("release", MavenPublication::class) {
                // Applies the component for the release build variant.
                // NOTE : Delete this line code if you publish Native Java / Kotlin Library
                from(components["release"])


                // Library Package Name (Example : "com.frogobox.androidfirstlib")
                // NOTE : Different GroupId For Each Library / Module, So That Each Library Is Not Overwritten
                groupId = "com.github.alitafreshi"

                // Library Name / Module Name (Example : "androidfirstlib")
                // NOTE : Different ArtifactId For Each Library / Module, So That Each Library Is Not Overwritten
                artifactId = "whygoogle-ads"

                // Version Library Name (Example : "1.0.0")
                version = "0.0.23"
            }
        }
    }
}
