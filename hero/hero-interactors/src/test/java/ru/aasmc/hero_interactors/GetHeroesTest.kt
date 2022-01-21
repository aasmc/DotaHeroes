package ru.aasmc.hero_interactors

import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import org.junit.Test
import ru.aasmc.core.domain.DataState
import ru.aasmc.core.domain.ProgressBarState
import ru.aasmc.core.domain.UIComponent
import ru.aasmc.hero_datasource.cache.HeroCache
import ru.aasmc.hero_datasource.network.HeroService
import ru.aasmc.hero_datasource_test.cache.HeroCacheFake
import ru.aasmc.hero_datasource_test.cache.HeroDatabaseFake
import ru.aasmc.hero_datasource_test.network.HeroServiceFake
import ru.aasmc.hero_datasource_test.network.HeroServiceResponseType
import ru.aasmc.hero_datasource_test.network.data.HeroDataValid
import ru.aasmc.hero_datasource_test.network.data.HeroDataValid.NUM_HEROES
import ru.aasmc.hero_datasource_test.network.serializeHeroData

class GetHeroesTest {

    // SUT
    private lateinit var getHeroes: GetHeroes

    // dependencies
    private lateinit var heroCache: HeroCache
    private lateinit var heroService: HeroService

    @Test
    fun getHeroes_success() = runBlocking {
        val heroDb = HeroDatabaseFake()
        heroCache = HeroCacheFake(heroDb)
        heroService = HeroServiceFake.Factory.build(
            type = HeroServiceResponseType.GoodData
        )
        getHeroes = GetHeroes(
            service = heroService,
            cache = heroCache
        )

        // confirm the cache is empty
        var cachedHeroes = heroCache.selectAll()
        assert(cachedHeroes.isEmpty())

        // execute emissions
        val emissions = getHeroes.execute().toList()

        // first emission should be loading
        assert(emissions[0] == DataState.Loading<List<HeroService>>(ProgressBarState.Loading))

        // confirm second emission is data
        assert(emissions[1] is DataState.Data)
        assert((emissions[1] as DataState.Data).data?.size ?: 0 == NUM_HEROES)

        // confirm the cache is not empty
        cachedHeroes = heroCache.selectAll()
        assert(cachedHeroes.size == NUM_HEROES)

        // confirm Loading is hidden
        assert(emissions[2] == DataState.Loading<List<HeroService>>(ProgressBarState.Idle))
    }

    @Test
    fun getHeroes_malformedData_successFromCache() = runBlocking {
        val heroDb = HeroDatabaseFake()
        heroCache = HeroCacheFake(heroDb)
        heroService = HeroServiceFake.Factory.build(
            type = HeroServiceResponseType.MalformedData
        )
        getHeroes = GetHeroes(
            service = heroService,
            cache = heroCache
        )
        // confirm the cache is empty
        var cachedHeroes = heroCache.selectAll()
        assert(cachedHeroes.isEmpty())

        // add some data to the cache
        val heroData = serializeHeroData(HeroDataValid.data)
        heroCache.insert(heroData)

        cachedHeroes = heroCache.selectAll()
        assert(cachedHeroes.isNotEmpty())

        // execute use case
        val emissions = getHeroes.execute().toList()

        // first emission should be loading
        assert(emissions[0] == DataState.Loading<List<HeroService>>(ProgressBarState.Loading))

        // confirm the second emission is an error response
        assert(emissions[1] is DataState.Response)
        assert(
            ((emissions[1] as DataState.Response).uiComponent
                    as UIComponent.Dialog).title == "Network error"
        )
        assert(
            ((emissions[1] as DataState.Response).uiComponent
                    as UIComponent.Dialog).description.contains(
                "Unexpected JSON token at offset"
            )
        )

        // confirm the 3rd emission is data from the cache
        assert(emissions[2] is DataState.Data)
        assert((emissions[2] as DataState.Data).data?.size ?: 0 == NUM_HEROES)

        // confirm Loading is hidden
        assert(emissions[3] == DataState.Loading<List<HeroService>>(ProgressBarState.Idle))
    }

    @Test
    fun getHeroes_emptyList() = runBlocking {
        val heroDb = HeroDatabaseFake()
        heroCache = HeroCacheFake(heroDb)
        heroService = HeroServiceFake.Factory.build(
            type = HeroServiceResponseType.EmptyList
        )
        getHeroes = GetHeroes(
            service = heroService,
            cache = heroCache
        )

        // confirm the cache is empty
        var cachedHeroes = heroCache.selectAll()
        assert(cachedHeroes.isEmpty())

        // execute emissions
        val emissions = getHeroes.execute().toList()

        // first emission should be loading
        assert(emissions[0] == DataState.Loading<List<HeroService>>(ProgressBarState.Loading))

        // confirm second emission is data
        assert(emissions[1] is DataState.Data)
        assert((emissions[1] as DataState.Data).data?.size == 0)

        // confirm the cache is not empty
        cachedHeroes = heroCache.selectAll()
        assert(cachedHeroes.isEmpty())

        // confirm Loading is hidden
        assert(emissions[2] == DataState.Loading<List<HeroService>>(ProgressBarState.Idle))
    }
}






















