package ru.aasmc.ui_heroDetail.ui

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.remember
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.test.platform.app.InstrumentationRegistry
import coil.ImageLoader
import org.junit.Rule
import org.junit.Test
import ru.aasmc.hero_datasource_test.network.data.HeroDataValid
import ru.aasmc.hero_datasource_test.network.serializeHeroData
import ru.aasmc.ui_heroDetail.coil.FakeImageLoader
import kotlin.random.Random

@ExperimentalAnimationApi
class HeroDetailTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    private val context = InstrumentationRegistry.getInstrumentation().targetContext
    private val imageLoader: ImageLoader = FakeImageLoader.build(context)
    private val heroData = serializeHeroData(HeroDataValid.data)

    @Test
    fun isHeroDataShown() {
        val hero = heroData.get(Random.nextInt(0, heroData.size - 1))
        composeTestRule.setContent {
            val state = remember {
                HeroDetailState(
                    hero = hero
                )
            }

            HeroDetail(state = state, imageLoader = imageLoader, events = {})
        }

        composeTestRule.onNodeWithText(hero.localizedName).assertIsDisplayed()
        composeTestRule.onNodeWithText(hero.primaryAttribute.uiValue).assertIsDisplayed()
        composeTestRule.onNodeWithText(hero.attackType.uiValue).assertIsDisplayed()

        val proWinPercentage = (hero.proWins.toDouble() / hero.proPick.toDouble() * 100).toInt()
        composeTestRule.onNodeWithText("$proWinPercentage %")

        val turboWins = (hero.turboWins.toDouble() / hero.turboPicks.toDouble() * 100).toInt()
        composeTestRule.onNodeWithText("$turboWins %")
    }
}













