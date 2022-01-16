package ru.aasmc.dotaheroes.ui.navigation

import androidx.navigation.NamedNavArgument
import androidx.navigation.NavType
import androidx.navigation.navArgument

const val HERO_ID_ARGUMENT = "heroId"

sealed class Screen(
    val route: String,
    val arguments: List<NamedNavArgument>
) {

    object HeroListScreen: Screen(
        route = "heroList",
        arguments = emptyList()
    )

    object HeroDetailScreen: Screen(
        route = "heroDetail",
        arguments = listOf(
            navArgument(name = HERO_ID_ARGUMENT) {
                type = NavType.IntType
            }
        )
    )

}
