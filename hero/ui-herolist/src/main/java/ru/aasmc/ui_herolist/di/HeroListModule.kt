package ru.aasmc.ui_herolist.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.aasmc.core.util.Logger
import ru.aasmc.hero_interactors.FilterHeroes
import ru.aasmc.hero_interactors.GetHeroes
import ru.aasmc.hero_interactors.HeroInteractors
import javax.inject.Named
import javax.inject.Singleton

const val HERO_LIST_LOGGER = "HeroListLogger"

@Module
@InstallIn(SingletonComponent::class)
object HeroListModule {

    @Provides
    @Singleton
    fun provideGetHeroes(interactors: HeroInteractors): GetHeroes {
        return interactors.getHeroes
    }

    @Provides
    @Singleton
    @Named(HERO_LIST_LOGGER)
    fun provideLogger(): Logger {
        return Logger(tag = HERO_LIST_LOGGER, isDebug = true)
    }

    @Provides
    @Singleton
    fun provideFilterHeroes(
        interactors: HeroInteractors
    ): FilterHeroes {
        return interactors.filterHeroes
    }

}



















