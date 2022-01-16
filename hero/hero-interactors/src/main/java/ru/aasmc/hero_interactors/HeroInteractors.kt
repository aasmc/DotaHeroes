package ru.aasmc.hero_interactors

import com.squareup.sqldelight.db.SqlDriver
import ru.aasmc.hero_datasource.cache.HeroCache
import ru.aasmc.hero_datasource.network.HeroService

data class HeroInteractors(
    val getHeroes: GetHeroes,
    val getHeroFromCache: GetHeroFromCache
) {
    companion object Factory {
        fun build(
            sqlDriver: SqlDriver
        ): HeroInteractors {
            val service = HeroService.build()
            val cache = HeroCache.build(
                sqlDriver
            )
            return HeroInteractors(
                getHeroes = GetHeroes(
                    service = service,
                    cache = cache
                ),
                getHeroFromCache = GetHeroFromCache(
                    cache = cache
                )
            )
        }


        val schema: SqlDriver.Schema = HeroCache.schema

        val dbName: String = HeroCache.dbName
    }
}