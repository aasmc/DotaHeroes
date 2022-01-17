package ru.aasmc.ui_herolist.ui

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import ru.aasmc.core.DataState
import ru.aasmc.core.Logger
import ru.aasmc.core.UIComponent
import ru.aasmc.hero_domain.Hero
import ru.aasmc.hero_interactors.GetHeroes
import ru.aasmc.ui_herolist.di.HERO_LIST_LOGGER
import javax.inject.Inject
import javax.inject.Named

@HiltViewModel
class HeroListViewModel @Inject constructor(
    private val getHeroes: GetHeroes,
    @Named(HERO_LIST_LOGGER) private val logger: Logger,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    val state: MutableState<HeroListState> = mutableStateOf(HeroListState())


    init {
        onTriggerEvent(HeroListEvents.GetHeroesEvent)
    }

    fun onTriggerEvent(heroListEvents: HeroListEvents) {
        when (heroListEvents) {
            is HeroListEvents.GetHeroesEvent -> {
                getHeroes()
            }
            is HeroListEvents.FilterHeroesEvent -> {
                filterHeroes()
            }
            is HeroListEvents.UpdateHeroNameEvent -> {
                updateHeroName(heroListEvents.heroName)
            }
        }
    }

    private fun updateHeroName(heroName: String) {
        state.value = state.value.copy(heroName = heroName)
    }

    private fun filterHeroes() {
        val filteredHeroes: MutableList<Hero> = state.value.heroes.filter {
            it.localizedName.lowercase().contains(state.value.heroName.lowercase())
        }.toMutableList()
        state.value = state.value.copy(filteredHeroes = filteredHeroes)
    }

    private fun getHeroes() {
        getHeroes.execute().onEach { dataState ->
            when (dataState) {
                is DataState.Response -> {
                    when (dataState.uiComponent) {
                        is UIComponent.Dialog -> {
                            logger.log((dataState.uiComponent as UIComponent.Dialog).description)
                        }
                        is UIComponent.None -> {
                            logger.log((dataState.uiComponent as UIComponent.None).message)
                        }
                    }
                }
                is DataState.Data -> {
                    state.value = state.value.copy(heroes = dataState.data ?: emptyList())
                    filterHeroes()
                }
                is DataState.Loading -> {
                    state.value = state.value.copy(progressBarState = dataState.progressBarState)
                }
            }
        }.launchIn(viewModelScope)
    }

}


















