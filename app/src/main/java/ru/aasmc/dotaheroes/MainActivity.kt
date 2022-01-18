package ru.aasmc.dotaheroes

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.*
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.unit.IntOffset
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import coil.ImageLoader
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import dagger.hilt.android.AndroidEntryPoint
import ru.aasmc.dotaheroes.ui.navigation.Screen
import ru.aasmc.dotaheroes.ui.theme.DotaHeroesTheme
import ru.aasmc.ui_heroDetail.ui.HERO_ID_ARGUMENT
import ru.aasmc.ui_heroDetail.ui.HeroDetail
import ru.aasmc.ui_heroDetail.ui.HeroDetailViewModel
import ru.aasmc.ui_herolist.ui.HeroListViewModel
import ru.aasmc.ui_herolist.ui.components.HeroList
import javax.inject.Inject

@ExperimentalAnimationApi
@ExperimentalComposeUiApi
@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var imageLoader: ImageLoader

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            DotaHeroesTheme {

                val navController = rememberAnimatedNavController()

                BoxWithConstraints {
                    AnimatedNavHost(
                        navController = navController,
                        startDestination = Screen.HeroListScreen.route,
                        builder = {
                            addHeroList(navController, imageLoader, constraints.maxWidth / 2)
                            addHeroDetails(imageLoader, constraints.maxWidth / 2)
                        }
                    )
                }
            }
        }
    }
}

@ExperimentalAnimationApi
@ExperimentalComposeUiApi
fun NavGraphBuilder.addHeroList(
    navController: NavController,
    imageLoader: ImageLoader,
    screenWidth:Int
) {
    composable(
        route = Screen.HeroListScreen.route,
        exitTransition = {
            slideOutHorizontally(
                targetOffsetX = { -screenWidth },
                animationSpec = tween(
                    durationMillis = 300,
                    easing = FastOutSlowInEasing
                )

            ) + fadeOut(
                animationSpec = tween(durationMillis = 300)
            )
        },
        popEnterTransition = {
            slideInHorizontally(
                initialOffsetX = { -screenWidth },
                animationSpec = tween(
                    durationMillis = 300,
                    easing = FastOutSlowInEasing
                )
            ) + fadeIn(
                animationSpec = tween(durationMillis = 300)
            )
        }
    ) {

        val viewModel: HeroListViewModel = hiltViewModel()
        HeroList(
            state = viewModel.state.value,
            events = viewModel::onTriggerEvent,
            imageLoader = imageLoader,
            navigateToDetailScreen = { heroId ->
                navController.navigate(
                    "${Screen.HeroDetailScreen.route}/$heroId"
                )
            }
        )
    }
}

@ExperimentalAnimationApi
fun NavGraphBuilder.addHeroDetails(
    imageLoader: ImageLoader,
    screenWidth:Int
) {
    composable(
        route = Screen.HeroDetailScreen.route + "/{$HERO_ID_ARGUMENT}",
        arguments = Screen.HeroDetailScreen.arguments,
        enterTransition = {
            slideInHorizontally(
                initialOffsetX = { screenWidth },
                animationSpec = tween(
                    durationMillis = 300,
                    easing = FastOutSlowInEasing
                )
            ) + fadeIn(
                animationSpec = tween(durationMillis = 300)
            )
        },
        popExitTransition = {
            slideOutHorizontally(
                targetOffsetX = { screenWidth },
                animationSpec = tween(
                    durationMillis = 300,
                    easing = FastOutSlowInEasing
                )

            ) + fadeOut(
                animationSpec = tween(durationMillis = 300)
            )
        }
    ) { _ ->
        val viewModel: HeroDetailViewModel = hiltViewModel()
        HeroDetail(
            state = viewModel.state.value,
            imageLoader = imageLoader
        )
    }
}




















