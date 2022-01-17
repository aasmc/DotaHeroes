package ru.aasmc.ui_heroDetail.ui

import ru.aasmc.core.domain.ProgressBarState
import ru.aasmc.hero_domain.Hero

data class HeroDetailState(
    private val progressBarState: ProgressBarState = ProgressBarState.Idle,
    val hero: Hero? = null
)
