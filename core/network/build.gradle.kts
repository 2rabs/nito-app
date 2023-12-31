plugins {
    id("nito.primitive.kmp")
    id("nito.primitive.kmp.android")
    id("nito.primitive.kmp.ios")
    id("nito.primitive.kmp.js")
    id("nito.primitive.detekt")
    id("nito.primitive.kmp.serialization")
}

android.namespace = "club.nito.core.network"

kotlin {
    explicitApi()

    sourceSets {
        commonMain {
            dependencies {
                implementation(projects.core.common)
                implementation(projects.core.model)

                implementation(libs.kotlinxCoroutinesCore)

                implementation(libs.koin)
                implementation(libs.kermit)

                implementation(libs.okIo)
                implementation(libs.ktorClientCore)
                implementation(libs.ktorKotlinxSerialization)
                implementation(libs.ktorClientContentNegotiation)

                implementation(libs.supabaseGotrue)
                implementation(libs.supabasePostgrest)
                implementation(libs.supabaseRealtime)

                implementation(libs.multiplatformSettingsNoArg)
                implementation(libs.multiplatformSettingsCoroutines)
            }
        }

        androidMain {
            dependencies {
                implementation(libs.ktorClientOkHttp)
                implementation(libs.okHttpLoggingInterceptor)
                implementation(libs.firebaseRemoteConfig)
            }
        }

        iosMain {
            dependencies {
                implementation(libs.ktorClientDarwin)
            }
        }
    }
}
