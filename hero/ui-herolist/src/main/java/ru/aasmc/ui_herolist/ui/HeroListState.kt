package ru.aasmc.ui_herolist.ui

import ru.aasmc.core.ProgressBarState
import ru.aasmc.hero_domain.Hero

data class HeroListState(
    val progressBarState: ProgressBarState = ProgressBarState.Idle,
    val heroes: List<Hero> = emptyList()
)
