package com.harissabil.fisch.feature.logbook.catches.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.RadioButton
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import com.harissabil.fisch.core.common.theme.spacing
import com.harissabil.fisch.feature.logbook.catches.SortBy

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SortOptionsBottomSheet(
    modifier: Modifier = Modifier,
    onDismissRequest: () -> Unit,
    sheetState: SheetState,
    selectedSortOption: SortBy,
    onSortOptionClick: (SortBy) -> Unit,
) {
    val sortOptions: List<SortBy> = SortBy.entries

    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        sheetState = sheetState,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = MaterialTheme.spacing.medium + MaterialTheme.spacing.small)
                .then(modifier),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            sortOptions.forEach { sortOption ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onSortOptionClick(sortOption) }
                        .padding(
                            horizontal = MaterialTheme.spacing.medium,
                        ),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = selectedSortOption == sortOption,
                        onClick = { onSortOptionClick(sortOption) }
                    )
                    Spacer(modifier = Modifier.width(MaterialTheme.spacing.small))
                    Text(
                        text = sortOption.value,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }
    }
}