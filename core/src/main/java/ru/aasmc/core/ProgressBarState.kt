package ru.aasmc.core

sealed class ProgressBarState {
    object Loading : ProgressBarState()
    object Idle : ProgressBarState()
}
