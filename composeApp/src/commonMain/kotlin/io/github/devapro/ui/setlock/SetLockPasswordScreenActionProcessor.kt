package io.github.devapro.ui.setlock

import io.github.devapro.core.mvi.ActionProcessor
import io.github.devapro.core.mvi.CoroutineContextProvider
import io.github.devapro.core.mvi.Reducer
import io.github.devapro.data.LockManager
import io.github.devapro.ui.setlock.factory.SetLockPasswordScreenInitStateFactory
import io.github.devapro.ui.setlock.model.SetLockPasswordScreenAction
import io.github.devapro.ui.setlock.model.SetLockPasswordScreenEvent
import io.github.devapro.ui.setlock.model.SetLockPasswordScreenState
import io.github.devapro.ui.setlock.reducer.InitScreenReducer
import io.github.devapro.ui.setlock.reducer.OnBackClickedReducer
import io.github.devapro.ui.setlock.reducer.OnConfirmPasswordChangedReducer
import io.github.devapro.ui.setlock.reducer.OnCurrentPasswordChangedReducer
import io.github.devapro.ui.setlock.reducer.OnNewPasswordChangedReducer
import io.github.devapro.ui.setlock.reducer.OnRemovePasswordClickedReducer
import io.github.devapro.ui.setlock.reducer.OnSaveClickedReducer
import io.github.devapro.ui.setlock.reducer.OnToggleConfirmPasswordVisibilityReducer
import io.github.devapro.ui.setlock.reducer.OnToggleCurrentPasswordVisibilityReducer
import io.github.devapro.ui.setlock.reducer.OnToggleNewPasswordVisibilityReducer
import io.github.devapro.ui.welcome.factory.WelcomeScreenInitStateFactory
import io.github.devapro.ui.welcome.model.WelcomeScreenAction
import io.github.devapro.ui.welcome.model.WelcomeScreenEvent
import io.github.devapro.ui.welcome.model.WelcomeScreenState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class SetLockPasswordScreenActionProcessor(
    private val initStateFactory: SetLockPasswordScreenInitStateFactory,
    private val coroutineContextProvider: CoroutineContextProvider,
    reducers: Set<Reducer<SetLockPasswordScreenAction, SetLockPasswordScreenState, SetLockPasswordScreenAction, SetLockPasswordScreenEvent>>,
) : ActionProcessor<
        SetLockPasswordScreenState,
        SetLockPasswordScreenAction,
        SetLockPasswordScreenEvent>(
    reducers = reducers,
    initStateFactory = initStateFactory,
    coroutineDispatcher = coroutineContextProvider.default
)