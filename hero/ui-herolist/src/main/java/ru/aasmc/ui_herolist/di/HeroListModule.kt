package ru.aasmc.ui_herolist.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.aasmc.hero_interactors.GetHeroes
import ru.aasmc.hero_interactors.HeroInteractors
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object HeroListModule {

    @Provides
    @Singleton
    fun provideGetHeroes(interactors: HeroInteractors): GetHeroes {
        return interactors.getHeroes
    }

}