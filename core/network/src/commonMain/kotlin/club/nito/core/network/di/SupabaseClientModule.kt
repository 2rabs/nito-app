package club.nito.core.network.di

import club.nito.core.common.nitoJsonSettings
import club.nito.core.network.createHttpEngine
import club.nito.core.network.createNitoSupabaseClient
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.gotrue.Auth
import io.github.jan.supabase.gotrue.auth
import kotlinx.serialization.json.Json
import org.koin.core.module.Module
import org.koin.dsl.module

public val supabaseClientModule: Module = module {
    single<SupabaseClient> {
        createNitoSupabaseClient(
            httpClientEngine = createHttpEngine(
                buildConfig = get(),
            ),
            json = get(),
        )
    }
    single<Json> {
        nitoJsonSettings
    }
    single<Auth> {
        get<SupabaseClient>().auth
    }
}
