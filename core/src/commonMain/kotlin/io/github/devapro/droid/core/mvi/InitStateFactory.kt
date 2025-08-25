package io.github.devapro.droid.core.mvi

interface InitStateFactory<STATE> {

    fun createInitState(): STATE
}