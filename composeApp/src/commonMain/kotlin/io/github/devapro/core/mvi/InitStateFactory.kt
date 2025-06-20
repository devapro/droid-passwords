package io.github.devapro.core.mvi

interface InitStateFactory<STATE> {

    fun createInitState(): STATE
}