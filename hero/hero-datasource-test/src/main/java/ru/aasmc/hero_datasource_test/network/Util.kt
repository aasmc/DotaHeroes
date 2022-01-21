package ru.aasmc.hero_datasource_test.network

import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import ru.aasmc.hero_datasource.network.HeroDto
import ru.aasmc.hero_datasource.network.toHero
import ru.aasmc.hero_domain.Hero

private val json = Json {
    ignoreUnknownKeys = true
}

fun serializeHeroData(jsonData: String): List<Hero> {
    val heroes: List<Hero> = json.decodeFromString<List<HeroDto>>(
        jsonData
    ).map { it.toHero() }
    return heroes
}