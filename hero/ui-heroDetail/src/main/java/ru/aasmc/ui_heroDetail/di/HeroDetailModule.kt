package ru.aasmc.ui_heroDetail.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.aasmc.core.util.Logger
import ru.aasmc.hero_interactors.GetHeroFromCache
import ru.aasmc.hero_interactors.HeroInteractors
import javax.inject.Named
import javax.inject.Singleton

const val HERO_DETAIL_LOGGER = "HeroDetailLogger"

@Module
@InstallIn(SingletonComponent::class)
object HeroDetailModule {

    @Provides
    @Singleton
    fun provideGetHeroFromCache(
        interactors: HeroInteractors
    ): GetHeroFromCache {
        return interactors.getHeroFromCache
    }

    @Provides
    @Singleton
    @Named(HERO_DETAIL_LOGGER)
    fun provideLogger(): Logger {
        return Logger(tag = HERO_DETAIL_LOGGER, isDebug = true)
    }

}