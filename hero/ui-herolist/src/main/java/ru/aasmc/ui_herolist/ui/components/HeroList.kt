package ru.aasmc.ui_herolist.ui.components

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import coil.ImageLoader
import ru.aasmc.core.domain.ProgressBarState
import ru.aasmc.core.domain.UiComponentState
import ru.aasmc.ui_herolist.ui.HeroListEvents
import ru.aasmc.ui_herolist.ui.HeroListState

@ExperimentalAnimationApi
@ExperimentalComposeUiApi
@Composable
fun HeroList(
    state: HeroListState,
    events: (HeroListEvents) -> Unit,
    imageLoader: ImageLoader,
    navigateToDetailScreen: (Int) -> Unit
) {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {

        Column {
            HeroListToolbar(
                heroName = state.heroName,
                onHeroNameChanged = { heroName ->
                    events(HeroListEvents.UpdateHeroNameEvent(heroName = heroName))
                },
                onExecuteSearch = {
                    events(HeroListEvents.FilterHeroesEvent)
                },
                onShowFilterDialog = {
                    events(HeroListEvents.UpdateFilterDialogState(UiComponentState.Show))
                }
            )
            LazyColumn {
                items(state.filteredHeroes) { hero ->
                    HeroListItem(
                        hero = hero,
                        imageLoader = imageLoader,
                        onSelectHero = { heroId ->
                            navigateToDetailScreen(heroId)
                        }
                    )
                }
            }
        }

        if (state.filterDialogState is UiComponentState.Show) {
            HeroListFilter(
                heroFilter = state.heroFilter,
                onUpdateHeroFilter = { heroFilter ->
                    events(HeroListEvents.UpdateHeroFilter(heroFilter = heroFilter))
                },
                onCloseDialog = {
                    events(HeroListEvents.UpdateFilterDialogState(UiComponentState.Hide))
                }
            )
        }

        if (state.progressBarState is ProgressBarState.Loading) {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center)
            )
        }
    }
}
