package ru.aasmc.dotaheroes

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import coil.ImageLoader
import com.squareup.sqldelight.android.AndroidSqliteDriver
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import ru.aasmc.core.DataState
import ru.aasmc.core.Logger
import ru.aasmc.core.ProgressBarState
import ru.aasmc.core.UIComponent
import ru.aasmc.dotaheroes.ui.navigation.HERO_ID_ARGUMENT
import ru.aasmc.dotaheroes.ui.navigation.Screen
import ru.aasmc.dotaheroes.ui.theme.DotaHeroesTheme
import ru.aasmc.hero_interactors.HeroInteractors
import ru.aasmc.ui_heroDetail.ui.HeroDetail
import ru.aasmc.ui_herolist.HeroList
import ru.aasmc.ui_herolist.ui.HeroListState
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
    ) { backStackEntry ->
        HeroDetail(heroId = backStackEntry.arguments?.getInt(HERO_ID_ARGUMENT))
    }
}




















