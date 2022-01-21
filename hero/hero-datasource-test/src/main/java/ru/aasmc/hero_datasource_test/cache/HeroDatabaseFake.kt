package ru.aasmc.hero_datasource_test.cache

import ru.aasmc.hero_domain.Hero

class HeroDatabaseFake {
    val heroes: MutableList<Hero> = mutableListOf()
}