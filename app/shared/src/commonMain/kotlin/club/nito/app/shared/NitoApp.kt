package club.nito.app.shared

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import club.nito.app.shared.di.appModule
import club.nito.app.shared.di.featureModules
import club.nito.app.shared.di.nitoDateFormatterModule
import club.nito.app.shared.di.userMessageStateHolderModule
import club.nito.core.data.di.dataModule
import club.nito.core.database.di.databaseModule
import club.nito.core.datastore.di.dataStoreModule
import club.nito.core.designsystem.theme.NitoTheme
import club.nito.core.domain.di.useCaseModule
import club.nito.core.network.di.remoteDataSourceModule
import club.nito.core.network.di.supabaseClientModule
import club.nito.core.ui.koinStateMachine
import co.touchlab.kermit.Logger
import co.touchlab.kermit.koin.KermitKoinLogger
import moe.tlaster.precompose.PreComposeApp
import moe.tlaster.precompose.flow.collectAsStateWithLifecycle
import org.koin.compose.KoinApplication
import org.koin.dsl.KoinAppDeclaration

@Composable
fun NitoApp(
    modifier: Modifier = Modifier,
    shouldKeepOnScreen: (Boolean) -> Unit = {},
    initKoin: KoinAppDeclaration = {},
) {
    PreComposeApp {
        KoinApplication(
            application = {
                logger(
                    KermitKoinLogger(Logger.withTag("koin")),
                )

                modules(
                    nitoDateFormatterModule,
                    userMessageStateHolderModule,

                    supabaseClientModule,
                    remoteDataSourceModule,
//                fakeRemoteDataSourceModule,
                    dataStoreModule,
                    databaseModule,
                    dataModule,
                    useCaseModule,

                    appModule,
                    *featureModules.toTypedArray(),
                )

                initKoin(this)
            },
        ) {
            val stateMachine = koinStateMachine(NitoAppStateMachine::class)
            val uiState = stateMachine.uiState.collectAsStateWithLifecycle()
            LaunchedEffect(uiState.value) {
                shouldKeepOnScreen((uiState.value is NitoAppUiState.Success).not())
            }

            when (val state = uiState.value) {
                NitoAppUiState.Loading -> {}
                is NitoAppUiState.Success -> NitoTheme {
                    Surface(
                        modifier = modifier.fillMaxSize(),
                        color = MaterialTheme.colorScheme.background,
                    ) {
                        NitoNavHost(
                            authStatus = state.authStatus,
                        )
                    }
                }
            }
        }
    }
}
