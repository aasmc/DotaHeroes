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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
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
import ru.aasmc.ui_herolist.ui.test.TAG_HERO_FILTER_PROWINS_CHECKBOX

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
                        // HeroFilter
                        ListFilterSelector(
                            filter = {
                                onUpdateHeroFilter(HeroFilter.HeroOrder())
                            },
                            isEnabled = heroFilter is HeroFilter.HeroOrder,
                            label = HeroFilter.HeroOrder().uiValue,
                            modifier = Modifier
                                .testTag(TAG_HERO_FILTER_HERO_CHECKBOX)
                        ) {
                            val enabled: Boolean =
                                heroFilter is HeroFilter.HeroOrder

                            val order =
                                if(heroFilter is HeroFilter.HeroOrder) heroFilter.order else null

                            OrderSelector(
                                descString = "z -> a",
                                ascString = "a -> z",
                                isEnabled = enabled,
                                isDescSelected = enabled &&  order is FilterOrder.Descending,
                                isAscSelected = enabled && order is FilterOrder.Ascending,
                                onUpdateHeroFilterDesc = {
                                    onUpdateHeroFilter(
                                        HeroFilter.HeroOrder(
                                            order = FilterOrder.Descending
                                        )
                                    )
                                },
                                onUpdateHeroFilterAsc = {
                                    onUpdateHeroFilter(
                                        HeroFilter.HeroOrder(
                                            order = FilterOrder.Ascending
                                        )
                                    )
                                }
                            )
                        }

                        // Pro Win Filter
                        ListFilterSelector(
                            filter = {
                                onUpdateHeroFilter(HeroFilter.ProWins())
                            },
                            isEnabled = heroFilter is HeroFilter.ProWins,
                            label = HeroFilter.ProWins().uiValue,
                            modifier = Modifier
                                .testTag(TAG_HERO_FILTER_PROWINS_CHECKBOX)
                        ) {
                            val enabled: Boolean =
                                heroFilter is HeroFilter.ProWins

                            val order =
                                if(heroFilter is HeroFilter.ProWins) heroFilter.order else null

                            OrderSelector(
                                descString = "100% - 0%",
                                ascString = "0% - 100%",
                                isEnabled = enabled,
                                isDescSelected = enabled &&  order is FilterOrder.Descending,
                                isAscSelected = enabled && order is FilterOrder.Ascending,
                                onUpdateHeroFilterDesc = {
                                    onUpdateHeroFilter(
                                        HeroFilter.ProWins(
                                            order = FilterOrder.Descending
                                        )
                                    )
                                },
                                onUpdateHeroFilterAsc = {
                                    onUpdateHeroFilter(
                                        HeroFilter.ProWins(
                                            order = FilterOrder.Ascending
                                        )
                                    )
                                }
                            )
                        }
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
fun ListFilterSelector(
    modifier: Modifier = Modifier,
    label: String,
    filter: () -> Unit,
    isEnabled: Boolean,
    orderSelector: @Composable () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .padding(bottom = 12.dp)
                .clickable(
                    interactionSource = MutableInteractionSource(),
                    indication = null, // disable the highlight
                    enabled = true,
                    onClick = {
                        filter()
                    }
                )
        ) {
            Checkbox(
                modifier = Modifier
                    .padding(end = 8.dp)
                    .align(Alignment.CenterVertically),
                checked = isEnabled,
                onCheckedChange = {
                    filter()
                },
                colors = CheckboxDefaults.colors(MaterialTheme.colors.primary)
            )
            Text(
                text = label,
                style = MaterialTheme.typography.h3
            )
        }
        orderSelector()
    }
}



























