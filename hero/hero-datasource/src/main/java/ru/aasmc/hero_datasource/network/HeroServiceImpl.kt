package ru.aasmc.hero_datasource.network

import io.ktor.client.*
import io.ktor.client.request.*
import ru.aasmc.hero_domain.Hero

class HeroServiceImpl(
    private val httpClient: HttpClient
) : HeroService {

    override suspend fun getHeroStats(): List<Hero> {
        return httpClient.get<List<HeroDto>> {
            url(EndPoints.HERO_STATS)
        }.map { it.toHero() }
    }
}