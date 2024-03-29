plugins {
    id("nito.primitive.kmp")
    id("nito.primitive.kmp.android")
    id("nito.primitive.kmp.ios")
    id("nito.primitive.detekt")
}

android.namespace = "club.nito.core.model"

kotlin {
    explicitApi()

    sourceSets {
        commonMain {
            dependencies {
                implementation(libs.kotlinxCoroutinesCore)
                api(libs.kotlinxDatetime)
            }
        }
    }
}
