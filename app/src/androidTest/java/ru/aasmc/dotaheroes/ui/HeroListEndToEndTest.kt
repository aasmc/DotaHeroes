package ru.aasmc.dotaheroes.ui

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.test.platform.app.InstrumentationRegistry
import coil.ImageLoader
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import dagger.hilt.components.SingletonComponent
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import ru.aasmc.dotaheroes.MainActivity
import ru.aasmc.dotaheroes.coil.FakeImageLoader
import ru.aasmc.dotaheroes.di.HeroInteractorsModule
import ru.aasmc.dotaheroes.ui.navigation.Screen
import ru.aasmc.dotaheroes.ui.theme.DotaHeroesTheme
import ru.aasmc.hero_datasource.cache.HeroCache
import ru.aasmc.hero_datasource.network.HeroService
import ru.aasmc.hero_datasource_test.cache.HeroCacheFake
import ru.aasmc.hero_datasource_test.cache.HeroDatabaseFake
import ru.aasmc.hero_datasource_test.network.HeroServiceFake
import ru.aasmc.hero_datasource_test.network.HeroServiceResponseType
import ru.aasmc.hero_domain.HeroAttribute
import ru.aasmc.hero_interactors.FilterHeroes
import ru.aasmc.hero_interactors.GetHeroFromCache
import ru.aasmc.hero_interactors.GetHeroes
import ru.aasmc.hero_interactors.HeroInteractors
import ru.aasmc.ui_heroDetail.ui.HeroDetail
import ru.aasmc.ui_heroDetail.ui.HeroDetailViewModel
import ru.aasmc.ui_herolist.ui.HeroListViewModel
import ru.aasmc.ui_herolist.ui.components.HeroList
import ru.aasmc.ui_herolist.ui.test.*
import javax.inject.Singleton

@HiltAndroidTest
@UninstallModules(HeroInteractorsModule::class)
@ExperimentalComposeUiApi
@ExperimentalAnimationApi
class HeroListEndToEndTest {

    @Module
    @InstallIn(SingletonComponent::class)
    object TestHeroInteractorsModule {

        @Provides
        @Singleton
        fun provideHeroCache(): HeroCache {
            return HeroCacheFake(HeroDatabaseFake())
        }

        @Provides
        @Singleton
        fun provideHeroService(): HeroService {
            return HeroServiceFake.build(
                HeroServiceResponseType.GoodData
            )
        }

        @Provides
        @Singleton
        fun provideHeroInteractors(
            heroCache: HeroCache,
            heroService: HeroService
        ): HeroInteractors {
            return HeroInteractors(
                getHeroes = GetHeroes(service = heroService, cache = heroCache),
                filterHeroes = FilterHeroes(),
                getHeroFromCache = GetHeroFromCache(heroCache)
            )
        }
    }

    @get:Rule(order = 0)
    var hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    private val context = InstrumentationRegistry.getInstrumentation().targetContext
    private val imageLoader: ImageLoader = FakeImageLoader.build(context)


    @Before
    fun before(){
        // DO NOT USE ANIMATED NAV HOST IN TESTS!
        composeTestRule.setContent {
            DotaHeroesTheme {
                val navController = rememberNavController()
                NavHost(
                    navController = navController,
                    startDestination = Screen.HeroListScreen.route,
                    builder = {
                        composable(
                            route = Screen.HeroListScreen.route,
                        ){
                            val viewModel: HeroListViewModel = hiltViewModel()
                            HeroList(
                                state = viewModel.state.value,
                                events = viewModel::onTriggerEvent,
                                navigateToDetailScreen = { heroId ->
                                    navController.navigate("${Screen.HeroDetailScreen.route}/$heroId")
                                },
                                imageLoader = imageLoader,
                            )
                        }
                        composable(
                            route = Screen.HeroDetailScreen.route + "/{heroId}",
                            arguments = Screen.HeroDetailScreen.arguments,
                        ){
                            val viewModel: HeroDetailViewModel = hiltViewModel()
                            HeroDetail(
                                state = viewModel.state.value,
                                events = viewModel::onTriggerEvent,
                                imageLoader = imageLoader
                            )
                        }
                    }
                )
            }
        }
    }

    @Test
    fun testSearchHeroByName(){
        composeTestRule.onRoot(useUnmergedTree = true).printToLog("TAG") // For learning the ui tree system

        composeTestRule.onNodeWithTag(TAG_HERO_SEARCH_BAR).performTextInput("Anti-Mage")
        composeTestRule.onNodeWithTag(TAG_HERO_NAME, useUnmergedTree = true).assertTextEquals(
            "Anti-Mage",
        )
        composeTestRule.onNodeWithTag(TAG_HERO_SEARCH_BAR).performTextClearance()

        composeTestRule.onNodeWithTag(TAG_HERO_SEARCH_BAR).performTextInput("Storm Spirit")
        composeTestRule.onNodeWithTag(TAG_HERO_NAME, useUnmergedTree = true).assertTextEquals(
            "Storm Spirit",
        )
        composeTestRule.onNodeWithTag(TAG_HERO_SEARCH_BAR).performTextClearance()

        composeTestRule.onNodeWithTag(TAG_HERO_SEARCH_BAR).performTextInput("Mirana")
        composeTestRule.onNodeWithTag(TAG_HERO_NAME, useUnmergedTree = true).assertTextEquals(
            "Mirana",
        )
    }

    @ExperimentalComposeUiApi
    @Test
    fun testFilterHeroAlphabetically(){
        // Show the dialog
        composeTestRule.onNodeWithTag(TAG_HERO_FILTER_BTN).performClick()

        // Confirm the filter dialog is showing
        composeTestRule.onNodeWithTag(TAG_HERO_FILTER_DIALOG).assertIsDisplayed()

        // Filter by "Hero" name
        composeTestRule.onNodeWithTag(TAG_HERO_FILTER_HERO_CHECKBOX).performClick()

        // Order Descending (z-a)
        composeTestRule.onNodeWithTag(TAG_HERO_FILTER_DESC).performClick()

        // Close the dialog
        composeTestRule.onNodeWithTag(TAG_HERO_FILTER_DIALOG_DONE).performClick()

        // Confirm the order is correct
        composeTestRule.onAllNodesWithTag(TAG_HERO_NAME, useUnmergedTree = true).assertAny(hasText("Zeus"))

        // Show the dialog
        composeTestRule.onNodeWithTag(TAG_HERO_FILTER_BTN).performClick()

        // Order Ascending (a-z)
        composeTestRule.onNodeWithTag(TAG_HERO_FILTER_ASC).performClick()

        // Close the dialog
        composeTestRule.onNodeWithTag(TAG_HERO_FILTER_DIALOG_DONE).performClick()

        // Confirm the order is correct
        composeTestRule.onAllNodesWithTag(TAG_HERO_NAME, useUnmergedTree = true).assertAny(hasText("Abaddon"))
    }

    @Test
    fun testFilterHeroByProWins(){
        // Show the dialog
        composeTestRule.onNodeWithTag(TAG_HERO_FILTER_BTN).performClick()

        // Confirm the filter dialog is showing
        composeTestRule.onNodeWithTag(TAG_HERO_FILTER_DIALOG).assertIsDisplayed()

        // Filter by ProWin %
        composeTestRule.onNodeWithTag(TAG_HERO_FILTER_PROWINS_CHECKBOX).performClick()

        // Order Descending (100% - 0%)
        composeTestRule.onNodeWithTag(TAG_HERO_FILTER_DESC).performClick()

        // Close the dialog
        composeTestRule.onNodeWithTag(TAG_HERO_FILTER_DIALOG_DONE).performClick()

        // Confirm the order is correct
        composeTestRule.onAllNodesWithTag(TAG_HERO_NAME, useUnmergedTree = true).assertAny(hasText("Chen"))

        // Show the dialog
        composeTestRule.onNodeWithTag(TAG_HERO_FILTER_BTN).performClick()

        // Order Ascending (0% - 100%)
        composeTestRule.onNodeWithTag(TAG_HERO_FILTER_ASC).performClick()

        // Close the dialog
        composeTestRule.onNodeWithTag(TAG_HERO_FILTER_DIALOG_DONE).performClick()

        // Confirm the order is correct
        composeTestRule.onAllNodesWithTag(TAG_HERO_NAME, useUnmergedTree = true).assertAny(hasText("Dawnbreaker"))
    }

    @Test
    fun testFilterHeroByStrength(){
        // Show the dialog
        composeTestRule.onNodeWithTag(TAG_HERO_FILTER_BTN).performClick()

        // Confirm the filter dialog is showing
        composeTestRule.onNodeWithTag(TAG_HERO_FILTER_DIALOG).assertIsDisplayed()

        composeTestRule.onNodeWithTag(TAG_HERO_FILTER_STENGTH_CHECKBOX).performClick()

        // Close the dialog
        composeTestRule.onNodeWithTag(TAG_HERO_FILTER_DIALOG_DONE).performClick()

        // Confirm that only STRENGTH heros are showing
        composeTestRule.onAllNodesWithTag(TAG_HERO_PRIMARY_ATTRIBUTE, useUnmergedTree = true).assertAll(
            hasText(HeroAttribute.Strength.uiValue)
        )
    }

    @Test
    fun testFilterHeroByAgility(){
        // Show the dialog
        composeTestRule.onNodeWithTag(TAG_HERO_FILTER_BTN).performClick()

        // Confirm the filter dialog is showing
        composeTestRule.onNodeWithTag(TAG_HERO_FILTER_DIALOG).assertIsDisplayed()

        composeTestRule.onNodeWithTag(TAG_HERO_FILTER_AGILITY_CHECKBOX).performClick()

        // Close the dialog
        composeTestRule.onNodeWithTag(TAG_HERO_FILTER_DIALOG_DONE).performClick()

        // Confirm that only STRENGTH heros are showing
        composeTestRule.onAllNodesWithTag(TAG_HERO_PRIMARY_ATTRIBUTE, useUnmergedTree = true).assertAll(
            hasText(HeroAttribute.Agility.uiValue)
        )
    }

    @Test
    fun testFilterHeroByIntelligence(){
        // Show the dialog
        composeTestRule.onNodeWithTag(TAG_HERO_FILTER_BTN).performClick()

        // Confirm the filter dialog is showing
        composeTestRule.onNodeWithTag(TAG_HERO_FILTER_DIALOG).assertIsDisplayed()

        composeTestRule.onNodeWithTag(TAG_HERO_FILTER_INT_CHECKBOX).performClick()

        // Close the dialog
        composeTestRule.onNodeWithTag(TAG_HERO_FILTER_DIALOG_DONE).performClick()

        // Confirm that only STRENGTH heros are showing
        composeTestRule.onAllNodesWithTag(TAG_HERO_PRIMARY_ATTRIBUTE, useUnmergedTree = true).assertAll(
            hasText(
            HeroAttribute.Intelligence.uiValue)
        )
    }

}




















