// build.gradle (nivel superior)
plugins {
    alias(libs.plugins.android.application) apply false
}

allprojects {
    repositories {
        google()
        mavenCentral()
    }
}
