package ru.aasmc.ui_heroDetail.ui

import androidx.compose.material.Text
import androidx.compose.runtime.Composable

const val HERO_ID_ARGUMENT = "heroId"

@Composable
fun HeroDetail(
    state: HeroDetailState
) {
    state.hero?.let { hero ->
        Text(text = "Hero name: ${hero.localizedName}")
    } ?: Text(text = "Loading hero")

}