package ru.aasmc.ui_herolist.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import coil.ImageLoader
import ru.aasmc.core.ProgressBarState
import ru.aasmc.ui_herolist.ui.HeroListState

@ExperimentalComposeUiApi
@Composable
fun HeroList(
    state: HeroListState,
    imageLoader: ImageLoader,
    navigateToDetailScreen: (Int) -> Unit
) {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {

        Column {
            val name = remember {
                mutableStateOf("")
            }
            HeroListToolbar(
                heroName = name.value,
                onHeroNameChanged = { heroName ->
                    name.value = heroName
                },
                onExecuteSearch = {

                },
                onShowFilterDialog = {}
            )
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
        }

        if (state.progressBarState is ProgressBarState.Loading) {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center)
            )
        }
    }
}
