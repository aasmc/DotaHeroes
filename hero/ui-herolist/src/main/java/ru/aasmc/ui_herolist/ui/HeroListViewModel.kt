package ru.aasmc.ui_herolist.ui

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import ru.aasmc.core.domain.DataState
import ru.aasmc.core.domain.Queue
import ru.aasmc.core.domain.UIComponent
import ru.aasmc.core.util.Logger
import ru.aasmc.hero_domain.Hero
import ru.aasmc.hero_domain.HeroFilter
import ru.aasmc.hero_interactors.FilterHeroes
import ru.aasmc.hero_interactors.GetHeroes
import ru.aasmc.ui_herolist.di.HERO_LIST_LOGGER
import javax.inject.Inject
import javax.inject.Named

@HiltViewModel
class HeroListViewModel @Inject constructor(
    private val getHeroes: GetHeroes,
    private val filterHeroes: FilterHeroes,
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
            is HeroListEvents.UpdateHeroFilter -> {
                updateHeroFilter(heroListEvents.heroFilter)
            }
            is HeroListEvents.UpdateFilterDialogState -> {
                state.value = state.value.copy(filterDialogState = heroListEvents.uiComponentState)
            }
            is HeroListEvents.UpdateAttributeFilter -> {
                state.value = state.value.copy(primaryAttribute = heroListEvents.attribute)
                filterHeroes()
            }
            is HeroListEvents.OnRemoveHeadFromQueue -> {
                removeErrorMessageFromQueue()
            }
        }
    }

    private fun removeErrorMessageFromQueue() {
        try {
            val queue = state.value.errorQueue
            queue.remove()

            state.value = state.value.copy(errorQueue = Queue(mutableListOf())) // force recompose
            state.value = state.value.copy(errorQueue = queue)
        } catch (e: Exception) {
            logger.log("Nothing to remove from DialogQueue")
        }
    }

    private fun updateHeroFilter(heroFilter: HeroFilter) {
        state.value = state.value.copy(heroFilter = heroFilter)
        filterHeroes()
    }

    private fun updateHeroName(heroName: String) {
        state.value = state.value.copy(heroName = heroName)
    }

    private fun filterHeroes() {
        val filteredList = filterHeroes.execute(
            current = state.value.heroes,
            heroName = state.value.heroName,
            heroFilter = state.value.heroFilter,
            attributeFilter = state.value.primaryAttribute
        )
        state.value = state.value.copy(filteredHeroes = filteredList)
    }

    private fun getHeroes() {
        getHeroes.execute().onEach { dataState ->
            when (dataState) {
                is DataState.Response -> {
                    when (dataState.uiComponent) {
                        is UIComponent.Dialog -> {
                            appendToErrorQueue(dataState.uiComponent)
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

    private fun appendToErrorQueue(uiComponent: UIComponent) {
        val queue = state.value.errorQueue
        queue.add(uiComponent)
        // todo consider removing this workaround to force recomposition
        state.value = state.value.copy(errorQueue = Queue(mutableListOf())) // force recompose
        state.value = state.value.copy(errorQueue = queue)
    }

}


















