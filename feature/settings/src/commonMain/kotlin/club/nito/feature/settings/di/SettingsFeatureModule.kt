package club.nito.feature.settings.di

import club.nito.feature.settings.SettingsScreenStateMachine
import org.koin.core.module.Module
import org.koin.dsl.module

public val settingsFeatureModule: Module = module {
    factory {
        SettingsScreenStateMachine(
            authStatusStream = get(),
            modifyPassword = get(),
            logout = get(),
            userMessageStateHolder = get(),
        )
    }
}
