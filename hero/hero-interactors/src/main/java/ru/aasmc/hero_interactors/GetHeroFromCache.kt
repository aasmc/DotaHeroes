package ru.aasmc.hero_interactors

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.aasmc.core.domain.DataState
import ru.aasmc.core.domain.ProgressBarState
import ru.aasmc.core.domain.UIComponent
import ru.aasmc.hero_datasource.cache.HeroCache
import ru.aasmc.hero_domain.Hero

class GetHeroFromCache(
    private val cache: HeroCache
) {

    fun execute(id: Int): Flow<DataState<Hero>> = flow {
        try {
            // 1. show loading
            emit(
                DataState.Loading(progressBarState = ProgressBarState.Loading)
            )

            val cachedHero = cache.getHero(id)
                ?: throw Exception("Hero with id: $id doesn't exist in the cache!")

            emit(DataState.Data(cachedHero))

        } catch (e: Exception) {
            e.printStackTrace()
            emit(
                DataState.Response(
                    uiComponent = UIComponent.Dialog(
                        title = "Error",
                        description = e.message ?: "Unknown error"
                    )
                )
            )
        } finally {
            emit(DataState.Loading(progressBarState = ProgressBarState.Idle))
        }
    }

}