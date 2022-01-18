package ru.aasmc.ui_heroDetail.ui

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import ru.aasmc.core.domain.DataState
import ru.aasmc.core.domain.ProgressBarState
import ru.aasmc.hero_interactors.GetHeroFromCache
import javax.inject.Inject

@HiltViewModel
class HeroDetailViewModel @Inject constructor(
    private val getHeroFromCache: GetHeroFromCache,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    val state: MutableState<HeroDetailState> = mutableStateOf(HeroDetailState())

    init {
        // all of the arguments that are passed to the destination
        // through the back stack entry are automatically saved in the
        // savedStateHandle, and we cann access them by their names.
        savedStateHandle.get<Int>(HERO_ID_ARGUMENT)?.let { heroId ->
            onTriggerEvent(HeroDetailEvents.GetHeroFromCacheEvent(heroId))
        }
    }

    fun onTriggerEvent(events: HeroDetailEvents) {
        when (events) {
            is HeroDetailEvents.GetHeroFromCacheEvent -> {
                getHeroFromCache(events.id)
            }
        }
    }

    private fun getHeroFromCache(id: Int) {
        getHeroFromCache.execute(id)
            .onEach { dataState ->
                when (dataState) {
                    is DataState.Data -> {
                        state.value = state.value.copy(hero = dataState.data)
                    }
                    is DataState.Loading -> {
                        state.value = state.value.copy(progressBarState = dataState.progressBarState)
                    }
                    is DataState.Response -> {
                        // TODO Handle errors
                    }
                }
            }.launchIn(viewModelScope)
    }

}

















