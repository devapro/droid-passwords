package io.github.devapro.ui.edit.factory

import io.github.devapro.core.mvi.InitStateFactory
import io.github.devapro.ui.edit.model.AddEditPasswordScreenState

class AddEditPasswordScreenInitStateFactory : InitStateFactory<AddEditPasswordScreenState> {

    override fun createInitState(): AddEditPasswordScreenState {
        return AddEditPasswordScreenState.Loading
    }
} 