package ru.aasmc.ui_herolist

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import coil.ImageLoader
import ru.aasmc.core.ProgressBarState
import ru.aasmc.ui_herolist.ui.HeroListState
import ru.aasmc.ui_herolist.ui.ui.HeroListItem

@Composable
fun HeroList(
    state: HeroListState,
    imageLoader: ImageLoader,
    navigateToDetailScreen: (Int) -> Unit
) {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        LazyColumn {
            items(state.heroes) { hero ->
                HeroListItem(
                    hero = hero,
                    imageLoader = imageLoader,
                    onSelectHero = { heroId ->
                        navigateToDetailScreen(heroId)
                    }
                )
            }
        }
        if (state.progressBarState is ProgressBarState.Loading) {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center)
            )
        }
    }
}
