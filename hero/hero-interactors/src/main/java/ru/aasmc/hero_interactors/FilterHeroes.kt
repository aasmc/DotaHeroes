package ru.aasmc.hero_interactors

import ru.aasmc.core.domain.FilterOrder
import ru.aasmc.hero_domain.Hero
import ru.aasmc.hero_domain.HeroAttribute
import ru.aasmc.hero_domain.HeroFilter
import kotlin.math.round

class FilterHeroes {

    fun execute(
        current: List<Hero>,
        heroName: String,
        heroFilter: HeroFilter,
        attributeFilter: HeroAttribute
    ): List<Hero> {
        var filteredHeroes: MutableList<Hero> = current.filter {
            it.localizedName.lowercase().contains(heroName.lowercase())
        }.toMutableList()

        when (heroFilter) {
            is HeroFilter.HeroOrder -> {
                sortHeroesBy(
                    filteredHeroes, heroFilter.order
                ) { hero ->
                    hero.localizedName
                }
            }
            is HeroFilter.ProWins -> {
                sortHeroesBy(
                    filteredHeroes, heroFilter.order
                ) { hero ->
                    getWinRate(hero.proWins.toDouble(), hero.proPick.toDouble())
                }
            }
        }

        when (attributeFilter) {
            HeroAttribute.Agility -> {
                filteredHeroes = filteredHeroes.filter {
                    it.primaryAttribute is HeroAttribute.Agility
                }.toMutableList()
            }
            HeroAttribute.Intelligence -> {
                filteredHeroes = filteredHeroes.filter {
                    it.primaryAttribute is HeroAttribute.Intelligence
                }.toMutableList()
            }
            HeroAttribute.Strength -> {
                filteredHeroes = filteredHeroes.filter {
                    it.primaryAttribute is HeroAttribute.Strength
                }.toMutableList()
            }
            HeroAttribute.Unknown -> {
                // do not filter
            }
        }
        return filteredHeroes
    }

    private fun <T : Comparable<T>> sortHeroesBy(
        heroesList: MutableList<Hero>,
        filterOrder: FilterOrder,
        filter: (Hero) -> T
    ) {
        when (filterOrder) {
            FilterOrder.Descending -> {
                heroesList.sortByDescending {
                    filter(it)
                }
            }
            FilterOrder.Ascending -> {
                heroesList.sortBy {
                    filter(it)
                }
            }
        }
    }

    private fun getWinRate(proWins: Double, proPick: Double): Int {
        return if (proPick <= 0) {
            0
        } else {
            val winRate: Int = round(proWins / proPick * 100).toInt()
            winRate
        }
    }

}














