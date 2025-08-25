package io.github.devapro.droid.edit.factory

import io.github.devapro.droid.core.mvi.InitStateFactory
import io.github.devapro.droid.edit.model.AddEditPasswordScreenState

class AddEditPasswordScreenInitStateFactory : InitStateFactory<AddEditPasswordScreenState> {

    override fun createInitState(): AddEditPasswordScreenState {
        return AddEditPasswordScreenState.Loading
    }
} 