package ru.aasmc.hero_interactors

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.aasmc.core.DataState
import ru.aasmc.core.ProgressBarState
import ru.aasmc.core.UIComponent
import ru.aasmc.hero_datasource.network.HeroService
import ru.aasmc.hero_domain.Hero

class GetHeroes(
    private val service: HeroService,
    // TODO add caching
) {

    /**
     * TODO get rid of nested try catches! And look that it emits
     * empty list regardless of the network error.
     * Try to use Flow.catch
     */
    fun execute(): Flow<DataState<List<Hero>>> = flow {
        try {
            // 1. show loading
            emit(
                DataState.Loading(progressBarState = ProgressBarState.Loading)
            )

            val heroes: List<Hero> = try {
                service.getHeroStats()
            } catch (e: Exception) {
                e.printStackTrace()
                emit(
                    DataState.Response<List<Hero>>(
                        uiComponent = UIComponent.Dialog(
                            title = "Network error",
                            description = e.message ?: "Unknown error"
                        )
                    )
                )
                emptyList()
            }

            emit(
                DataState.Data(heroes)
            )

        } catch (e: Exception) {
            e.printStackTrace()
            emit(
                DataState.Response<List<Hero>>(
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