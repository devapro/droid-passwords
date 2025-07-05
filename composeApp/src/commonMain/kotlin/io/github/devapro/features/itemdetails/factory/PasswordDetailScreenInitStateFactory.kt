package io.github.devapro.features.itemdetails.factory

import io.github.devapro.core.mvi.InitStateFactory
import io.github.devapro.features.itemdetails.model.PasswordDetailScreenState

class PasswordDetailScreenInitStateFactory : InitStateFactory<PasswordDetailScreenState> {

    override fun createInitState(): PasswordDetailScreenState {
        return PasswordDetailScreenState.Loading
    }
} 