package ru.aasmc.core

/**
 * Wrapper class for possible states data can be in.
 */
sealed class DataState<T> {

    /**
     * Represents a response that may contain some data or not.
     */
    data class Response<T>(
        val uiComponent: UIComponent
    ) : DataState<T>()

    /**
     * Wrapper for state that contains data.
     */
    data class Data<T>(
        val data: T? = null
    ) : DataState<T>()

    /**
     * Represents the state of loading data. It can be either Idle or Loading.
     */
    data class Loading<T>(
        val progressBarState: ProgressBarState = ProgressBarState.Idle
    ) : DataState<T>()
}
