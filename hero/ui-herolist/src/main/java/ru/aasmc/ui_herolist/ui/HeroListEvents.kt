package ru.aasmc.ui_herolist.ui

import ru.aasmc.core.domain.UiComponentState
import ru.aasmc.hero_domain.HeroAttribute
import ru.aasmc.hero_domain.HeroFilter

sealed class HeroListEvents {

    object GetHeroesEvent : HeroListEvents()

    object FilterHeroesEvent : HeroListEvents()

    data class UpdateHeroNameEvent(
        val heroName: String
    ) : HeroListEvents()

    data class UpdateHeroFilter(
        val heroFilter: HeroFilter
    ) : HeroListEvents()

    data class UpdateAttributeFilter(
        val attribute: HeroAttribute
    ): HeroListEvents()

    data class UpdateFilterDialogState(
        val uiComponentState: UiComponentState
    ): HeroListEvents()
}