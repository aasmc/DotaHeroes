package ru.aasmc.ui_herolist.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Checkbox
import androidx.compose.material.CheckboxDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import ru.aasmc.ui_herolist.ui.test.TAG_HERO_FILTER_ASC
import ru.aasmc.ui_herolist.ui.test.TAG_HERO_FILTER_DESC

@ExperimentalAnimationApi
@Composable
fun OrderSelector(
    descString: String,
    ascString: String,
    isEnabled: Boolean,
    isDescSelected: Boolean,
    isAscSelected: Boolean,
    onUpdateHeroFilterDesc: () -> Unit,
    onUpdateHeroFilterAsc: () -> Unit
) {

    // Descending order
    OrderSelectorIndependent(
        label = descString,
        isEnabled = isEnabled,
        isFilterSelected = isDescSelected,
        onUpdateFilter = onUpdateHeroFilterDesc,
        modifier = Modifier.testTag(TAG_HERO_FILTER_DESC)
    )

    // Ascending order
    OrderSelectorIndependent(
        label = ascString,
        isEnabled = isEnabled,
        isFilterSelected = isAscSelected,
        onUpdateFilter = onUpdateHeroFilterAsc,
        modifier = Modifier.testTag(TAG_HERO_FILTER_ASC)
    )

}

@ExperimentalAnimationApi
@Composable
private fun OrderSelectorIndependent(
    label: String,
    isEnabled: Boolean,
    isFilterSelected: Boolean,
    onUpdateFilter: () -> Unit,
    modifier: Modifier = Modifier
) {
    AnimatedVisibility(visible = isEnabled) {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .padding(start = 24.dp, bottom = 8.dp)
                .clickable(
                    interactionSource = MutableInteractionSource(),
                    indication = null, // disable the highlight
                    enabled = isEnabled,
                    onClick = {
                        onUpdateFilter()
                    }
                )
        ) {
            Checkbox(
                modifier = Modifier
                    .padding(end = 8.dp)
                    .align(Alignment.CenterVertically),
                enabled = isEnabled,
                checked = isEnabled && isFilterSelected,
                onCheckedChange = {
                    onUpdateFilter()
                },
                colors = CheckboxDefaults.colors(MaterialTheme.colors.primary)
            )
            Text(
                text = label,
                style = MaterialTheme.typography.body1
            )
        }
    }
}
























