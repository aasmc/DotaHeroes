package ru.aasmc.core.domain

sealed class UiComponentState {
    object Show : UiComponentState()

    object Hide : UiComponentState()
}
