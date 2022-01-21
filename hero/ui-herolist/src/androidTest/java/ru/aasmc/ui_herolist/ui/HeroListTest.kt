package ru.aasmc.ui_herolist.ui

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.test.platform.app.InstrumentationRegistry
import coil.ImageLoader
import org.junit.Rule
import org.junit.Test
import ru.aasmc.hero_datasource_test.network.data.HeroDataValid
import ru.aasmc.hero_datasource_test.network.serializeHeroData
import ru.aasmc.ui_herolist.coil.FakeImageLoader
import ru.aasmc.ui_herolist.ui.components.HeroList

@ExperimentalAnimationApi
@ExperimentalComposeUiApi
class HeroListTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val context = InstrumentationRegistry.getInstrumentation().targetContext
    private val imageLoader: ImageLoader = FakeImageLoader.build(context)
    private val heroData = serializeHeroData(HeroDataValid.data)

    @Test
    fun areHeroesShown() {
        composeTestRule.setContent {
            val state = remember {
                HeroListState(
                    heroes = heroData,
                    filteredHeroes = heroData
                )
            }

            HeroList(
                state = state,
                events = {},
                imageLoader = imageLoader,
                navigateToDetailScreen = {}
            )
        }

        composeTestRule.onNodeWithText("Anti-Mage").assertIsDisplayed()
        composeTestRule.onNodeWithText("Axe").assertIsDisplayed()
        composeTestRule.onNodeWithText("Bane").assertIsDisplayed()
    }
}



















