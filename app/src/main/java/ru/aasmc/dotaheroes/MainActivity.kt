package ru.aasmc.dotaheroes

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.hilt.navigation.compose.hiltViewModel
import coil.ImageLoader
import com.squareup.sqldelight.android.AndroidSqliteDriver
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import ru.aasmc.core.DataState
import ru.aasmc.core.Logger
import ru.aasmc.core.ProgressBarState
import ru.aasmc.core.UIComponent
import ru.aasmc.dotaheroes.ui.theme.DotaHeroesTheme
import ru.aasmc.hero_interactors.HeroInteractors
import ru.aasmc.ui_herolist.HeroList
import ru.aasmc.ui_herolist.ui.HeroListState
import ru.aasmc.ui_herolist.ui.HeroListViewModel
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var imageLoader: ImageLoader

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            DotaHeroesTheme {
                val viewModel: HeroListViewModel = hiltViewModel()
                HeroList(state = viewModel.state.value, imageLoader = imageLoader)
            }
        }
    }
}




















