package ru.aasmc.ui_heroDetail.ui

sealed class HeroDetailEvents {
    data class GetHeroFromCacheEvent(
        val id: Int
    ) : HeroDetailEvents()

    object OnRemoveHeadFromQueue: HeroDetailEvents()
}
