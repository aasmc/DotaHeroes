package ru.aasmc.ui_herolist.ui.components

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import ru.aasmc.core.domain.FilterOrder
import ru.aasmc.hero_domain.HeroAttribute
import ru.aasmc.hero_domain.HeroFilter
import ru.aasmc.ui_herolist.ui.test.*
import ru.aasmc.ui_herolist.R

@ExperimentalAnimationApi
@Composable
fun HeroListFilter(
    heroFilter: HeroFilter,
    onUpdateHeroFilter: (HeroFilter) -> Unit,
    attributeFilter: HeroAttribute = HeroAttribute.Unknown,
    onUpdateAttributeFilter: (HeroAttribute) -> Unit,
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
                        Spacer(modifier = Modifier.height(8.dp))
                        Divider(
                            color = MaterialTheme.colors.onBackground.copy(alpha = 0.5f),
                            thickness = 1.dp
                        )
                        Spacer(modifier = Modifier.height(8.dp))

                        // Primary Attribute Filter
                        PrimaryAttrFilterSelector(
                            removeFilterOnPrimaryAttr = {
                                onUpdateAttributeFilter(HeroAttribute.Unknown)
                            },
                            attribute = attributeFilter,
                            onFilterStr = {
                                onUpdateAttributeFilter(
                                    HeroAttribute.Strength
                                )
                            },
                            onFilterAgi = {
                                onUpdateAttributeFilter(
                                    HeroAttribute.Agility
                                )
                            },
                            onFilterInt = {
                                onUpdateAttributeFilter(
                                    HeroAttribute.Intelligence
                                )
                            },
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

/**
 * @param filterOnPrimaryAttr: Set the HeroFilter to 'PrimaryAttribute'
 * @param isEnabled: Is the PrimaryAttribute filter the selected 'HeroFilter'
 * @param attribute: Is the current attribute Strength, Agility or Intelligence?
 * @param orderStr: Set the order to Strength.
 * @param orderAgi: Set the order to Agility.
 * @param orderInt: Set the order to Intelligence.
 */
@ExperimentalAnimationApi
@Composable
fun PrimaryAttrFilterSelector(
    removeFilterOnPrimaryAttr: () -> Unit,
    attribute: HeroAttribute,
    onFilterStr: () -> Unit,
    onFilterAgi: () -> Unit,
    onFilterInt: () -> Unit,
){
    Column(
        modifier = Modifier
            .fillMaxWidth()
    ){
        Row(
            modifier = Modifier
                .padding(bottom = 12.dp)
                .fillMaxWidth()
            ,
        ){
            Text(
                text = stringResource(R.string.primary_attribute),
                style = MaterialTheme.typography.h3,
            )
        }

        PrimaryAttrSelector(
            isStr = attribute is HeroAttribute.Strength,
            isAgi = attribute is HeroAttribute.Agility,
            isInt = attribute is HeroAttribute.Intelligence,
            onUpdateHeroFilterStr = {
                onFilterStr()
            },
            onUpdateHeroFilterAgi = {
                onFilterAgi()
            },
            onUpdateHeroFilterInt = {
                onFilterInt()
            },
            onRemoveAttributeFilter = {
                removeFilterOnPrimaryAttr()
            }
        )
    }
}

@ExperimentalAnimationApi
@Composable
fun PrimaryAttrSelector(
    isStr: Boolean = false,
    isAgi: Boolean = false,
    isInt: Boolean = false,
    onUpdateHeroFilterStr: () -> Unit,
    onUpdateHeroFilterAgi: () -> Unit,
    onUpdateHeroFilterInt: () -> Unit,
    onRemoveAttributeFilter: () -> Unit,
){
    // Strength
    PrimaryAttributeSelectorRow(
        checked = isStr,
        label = HeroAttribute.Strength.uiValue,
        modifier = Modifier
            .testTag(TAG_HERO_FILTER_STENGTH_CHECKBOX),
        onFilter = {
            onUpdateHeroFilterStr()
        }
    )
    // Agility
    PrimaryAttributeSelectorRow(
        checked = isAgi,
        label = HeroAttribute.Agility.uiValue,
        modifier = Modifier
            .testTag(TAG_HERO_FILTER_AGILITY_CHECKBOX),
        onFilter = {
            onUpdateHeroFilterAgi()
        }
    )

    // Intelligence
    PrimaryAttributeSelectorRow(
        checked = isInt,
        label = HeroAttribute.Intelligence.uiValue,
        modifier = Modifier
            .testTag(TAG_HERO_FILTER_INT_CHECKBOX),
        onFilter = {
            onUpdateHeroFilterInt()
        }
    )

    // No Filter on Attribute
    PrimaryAttributeSelectorRow(
        checked = !isStr && !isAgi && !isInt,
        modifier = Modifier.testTag(TAG_HERO_FILTER_UNKNOWN_CHECKBOX),
        label = stringResource(R.string.none),
        onFilter = {
            onRemoveAttributeFilter()
        }
    )
}

@Composable
private fun PrimaryAttributeSelectorRow(
    modifier: Modifier = Modifier,
    label: String,
    checked: Boolean,
    onFilter: () -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(start = 24.dp, bottom = 8.dp)
            .clickable(
                interactionSource = MutableInteractionSource(),
                indication = null, // disable the highlight
                onClick = {
                    onFilter()
                },
            )
        ,
    ){
        Checkbox(
            modifier = Modifier
                .padding(end = 8.dp)
                .align(Alignment.CenterVertically)
            ,
            checked = checked,
            onCheckedChange = {
                onFilter()
            },
            colors = CheckboxDefaults.colors(MaterialTheme.colors.primary)
        )
        Text(
            text = label,
            style = MaterialTheme.typography.body1,
        )
    }
}


























