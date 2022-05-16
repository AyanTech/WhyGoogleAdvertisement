object Build {
    private const val gradleBuildTools = "7.2.0"
    const val buildTools = "com.android.tools.build:gradle:${gradleBuildTools}"
    const val kotlinGradlePlugin = "org.jetbrains.kotlin:kotlin-gradle-plugin:${Kotlin.version}"
    private const val jitpackGradleBuildVersion = "2.1"

    const val jitpackGradlePlugin =
        "com.github.dcendents:android-maven-gradle-plugin:${jitpackGradleBuildVersion}"


}
