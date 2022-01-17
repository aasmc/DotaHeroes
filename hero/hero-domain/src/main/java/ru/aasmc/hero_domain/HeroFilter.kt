package ru.aasmc.hero_domain

import ru.aasmc.core.domain.FilterOrder

private const val HERO_ORDER = "Hero"
private const val PRO_WINS_ORDER = "Pro win-rate"

sealed class HeroFilter(
    val uiValue: String
) {

    data class HeroOrder(
        val order: FilterOrder = FilterOrder.Descending
    ): HeroFilter(HERO_ORDER)

    data class ProWins(
        val order: FilterOrder = FilterOrder.Descending
    ): HeroFilter(PRO_WINS_ORDER)

}
