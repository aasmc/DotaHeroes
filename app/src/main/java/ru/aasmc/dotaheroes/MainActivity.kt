package ru.aasmc.dotaheroes

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import coil.ImageLoader
import dagger.hilt.android.AndroidEntryPoint
import ru.aasmc.dotaheroes.ui.navigation.Screen
import ru.aasmc.dotaheroes.ui.theme.DotaHeroesTheme
import ru.aasmc.ui_heroDetail.ui.HERO_ID_ARGUMENT
import ru.aasmc.ui_heroDetail.ui.HeroDetail
import ru.aasmc.ui_heroDetail.ui.HeroDetailViewModel
import ru.aasmc.ui_herolist.HeroList
import ru.aasmc.ui_herolist.ui.HeroListViewModel
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var imageLoader: ImageLoader

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            DotaHeroesTheme {

                val navController = rememberNavController()
                NavHost(
                    navController = navController,
                    startDestination = Screen.HeroListScreen.route,
                    builder = {
                        addHeroList(navController, imageLoader)
                        addHeroDetails()
                    }
                )
            }
        }
    }
}

fun NavGraphBuilder.addHeroList(
    navController: NavController,
    imageLoader: ImageLoader
) {
    composable(
        route = Screen.HeroListScreen.route
    ) {

        val viewModel: HeroListViewModel = hiltViewModel()
        HeroList(
            state = viewModel.state.value,
            imageLoader = imageLoader,
            navigateToDetailScreen = { heroId ->
                navController.navigate(
                    "${Screen.HeroDetailScreen.route}/$heroId"
                )
            }
        )
    }
}

fun NavGraphBuilder.addHeroDetails() {
    composable(
        route = Screen.HeroDetailScreen.route + "/{$HERO_ID_ARGUMENT}",
        arguments = Screen.HeroDetailScreen.arguments
    ) { _ ->
        val viewModel: HeroDetailViewModel = hiltViewModel()
        HeroDetail(state = viewModel.state.value)
    }
}




















