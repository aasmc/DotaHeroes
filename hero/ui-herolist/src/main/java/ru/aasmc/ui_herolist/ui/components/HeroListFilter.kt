package ru.aasmc.ui_herolist.ui.components

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import ru.aasmc.core.domain.FilterOrder
import ru.aasmc.hero_domain.HeroFilter
import ru.aasmc.ui_herolist.ui.test.TAG_HERO_FILTER_DIALOG
import ru.aasmc.ui_herolist.ui.test.TAG_HERO_FILTER_DIALOG_DONE
import ru.aasmc.ui_herolist.ui.test.TAG_HERO_FILTER_HERO_CHECKBOX

@ExperimentalAnimationApi
@Composable
fun HeroListFilter(
    heroFilter: HeroFilter,
    onUpdateHeroFilter: (HeroFilter) -> Unit,
    onCloseDialog: () -> Unit
) {
    AlertDialog(
        modifier = Modifier
            .padding(16.dp)
            .testTag(TAG_HERO_FILTER_DIALOG),
        onDismissRequest = {
            onCloseDialog()
        },
        title = {
            Text(
                text = "Filter",
                style = MaterialTheme.typography.h2
            )
        },
        text = {
            LazyColumn {
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        // todo replace with spacer if it works correctly
                        EmptyRow()

                        // Hero Filter
                        HeroListFilterSelector(
                            filterOnHero = {
                                onUpdateHeroFilter(HeroFilter.HeroOrder())
                            },
                            isEnabled = heroFilter is HeroFilter.HeroOrder,
                            order = if(heroFilter is HeroFilter.HeroOrder) heroFilter.order else null,
                            orderDesc = {
                                onUpdateHeroFilter(
                                    HeroFilter.HeroOrder(
                                        order = FilterOrder.Descending
                                    )
                                )
                            },
                            orderAsc = {
                                onUpdateHeroFilter(
                                    HeroFilter.HeroOrder(
                                        order = FilterOrder.Ascending
                                    )
                                )
                            }
                        )
                    }
                }
            }
        },
        buttons = {
            Column(
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .align(Alignment.End)
                        .testTag(TAG_HERO_FILTER_DIALOG_DONE)
                        .clickable {
                            onCloseDialog()
                        }
                ) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = "Done",
                        modifier = Modifier
                            .padding(10.dp),
                        tint = Color(0xFF009A34)
                    )
                }
            }
        }
    )
}

@ExperimentalAnimationApi
@Composable
fun HeroListFilterSelector(
    filterOnHero: () -> Unit,
    isEnabled: Boolean,
    order: FilterOrder? = null,
    orderDesc: () -> Unit,
    orderAsc: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 12.dp)
                .testTag(TAG_HERO_FILTER_HERO_CHECKBOX)
                .clickable(
                    interactionSource = MutableInteractionSource(),
                    indication = null, // disable the highlight
                    enabled = true,
                    onClick = {
                        filterOnHero()
                    }
                )
        ) {
            Checkbox(
                modifier = Modifier
                    .padding(end = 8.dp)
                    .align(Alignment.CenterVertically),
                checked = isEnabled,
                onCheckedChange = {
                    filterOnHero()
                },
                colors = CheckboxDefaults.colors(MaterialTheme.colors.primary)
            )
            Text(
                text = HeroFilter.HeroOrder().uiValue,
                style = MaterialTheme.typography.h3
            )
        }
        OrderSelector(
            descString = "z -> a",
            ascString = "a -> z",
            isEnabled = isEnabled,
            isDescSelected = isEnabled && order is FilterOrder.Descending,
            isAscSelected = isEnabled && order is FilterOrder.Ascending,
            onUpdateHeroFilterDesc = {
                orderDesc()
            },
            onUpdateHeroFilterAsc = {
                orderAsc()
            }
        )
    }

}


























