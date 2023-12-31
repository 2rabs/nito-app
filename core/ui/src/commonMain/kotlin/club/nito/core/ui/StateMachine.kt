package club.nito.core.ui

import androidx.compose.runtime.Composable
import kotlinx.coroutines.CoroutineScope
import moe.tlaster.precompose.koin.koinViewModel
import moe.tlaster.precompose.stateholder.LocalStateHolder
import moe.tlaster.precompose.stateholder.StateHolder
import moe.tlaster.precompose.viewmodel.ViewModel
import moe.tlaster.precompose.viewmodel.viewModelScope
import org.koin.compose.LocalKoinScope
import org.koin.core.parameter.ParametersDefinition
import org.koin.core.qualifier.Qualifier
import org.koin.core.scope.Scope
import kotlin.reflect.KClass

/**
 * [StateMachine]
 */
public open class StateMachine : ViewModel()

public val StateMachine.stateMachineScope: CoroutineScope
    get() = viewModelScope

@Composable
public fun <T : StateMachine> koinStateMachine(
    stateMachineClass: KClass<T>,
    qualifier: Qualifier? = null,
    stateHolder: StateHolder = checkNotNull(LocalStateHolder.current) {
        "No StateHolder was provided via LocalStateHolder"
    },
    key: String? = null,
    scope: Scope = LocalKoinScope.current,
    parameters: ParametersDefinition? = null,
): T = koinViewModel(
    vmClass = stateMachineClass,
    qualifier = qualifier,
    stateHolder = stateHolder,
    key = key,
    scope = scope,
    parameters = parameters,
)
