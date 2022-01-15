package ru.aasmc.hero_interactors

import ru.aasmc.hero_datasource.network.HeroService

data class HeroInteractors(
    val getHeroes: GetHeroes
    // TODO add other hero interactors
) {
    companion object Factory {
        fun build(): HeroInteractors {
            val service = HeroService.build()
            return HeroInteractors(
                getHeroes = GetHeroes(service)
            )
        }
    }
}