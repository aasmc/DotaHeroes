package ru.aasmc.ui_herolist.ui

sealed class HeroListEvents {

    object GetHeroesEvent : HeroListEvents()

    object FilterHeroesEvent : HeroListEvents()

    data class UpdateHeroNameEvent(
        val heroName: String
    ) : HeroListEvents()
}