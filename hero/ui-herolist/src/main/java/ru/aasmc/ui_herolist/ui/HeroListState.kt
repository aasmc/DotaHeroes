package ru.aasmc.ui_herolist.ui

import ru.aasmc.core.domain.ProgressBarState
import ru.aasmc.core.domain.Queue
import ru.aasmc.core.domain.UIComponent
import ru.aasmc.core.domain.UiComponentState
import ru.aasmc.hero_domain.Hero
import ru.aasmc.hero_domain.HeroAttribute
import ru.aasmc.hero_domain.HeroFilter

data class HeroListState(
    val progressBarState: ProgressBarState = ProgressBarState.Idle,
    val heroes: List<Hero> = emptyList(),
    val filteredHeroes: List<Hero> = emptyList(),
    val heroName: String = "",
    val heroFilter: HeroFilter = HeroFilter.HeroOrder(),
    val primaryAttribute: HeroAttribute = HeroAttribute.Unknown,
    val filterDialogState: UiComponentState = UiComponentState.Hide,
    val errorQueue: Queue<UIComponent> = Queue(mutableListOf())
)
