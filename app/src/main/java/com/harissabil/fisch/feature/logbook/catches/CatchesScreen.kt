package com.harissabil.fisch.feature.logbook.catches

import android.content.res.Configuration
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.DeleteForever
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SheetState
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.harissabil.fisch.core.common.component.FishAlertDialog
import com.harissabil.fisch.core.common.theme.FischTheme
import com.harissabil.fisch.core.firebase.firestore.domain.model.Logbook
import com.harissabil.fisch.feature.logbook.catches.component.CatchesList
import com.harissabil.fisch.feature.logbook.catches.component.SortOptionsBottomSheet
import com.harissabil.fisch.feature.logbook.common.component.MoreOptionBottomSheet
import com.harissabil.fisch.feature.logbook.common.mapper.toToDetailState
import com.harissabil.fisch.feature.logbook.common.state.ToDetailState
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CatchesScreen(
    viewModel: CatchesViewModel = hiltViewModel(),
    snackbarHostState: SnackbarHostState,
    listState: LazyListState,
    onNavigateToDetail: (ToDetailState) -> Unit,
) {
    val scope = rememberCoroutineScope()

    val isLoading by viewModel.isLoading.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()
    val logbooks by viewModel.filteredLogbooks.collectAsState(null)

    val moreOptionBottomSheetState = rememberModalBottomSheetState()
    var showMoreOptionBottomSheet by rememberSaveable { mutableStateOf(false) }

    var openAlertDialog by rememberSaveable { mutableStateOf(false) }

    val sortOptionsBottomSheetState = rememberModalBottomSheetState()
    var showSortOptionsBottomSheet by rememberSaveable { mutableStateOf(false) }
    val selectedSortOption by viewModel.sortOption.collectAsState()

    LaunchedEffect(key1 = Unit) {
        viewModel.eventFlow.collectLatest { event ->
            when (event) {
                is CatchesViewModel.UIEvent.ShowSnackbar -> {
                    snackbarHostState.currentSnackbarData?.dismiss()
                    snackbarHostState.showSnackbar(
                        message = event.message
                    )
                }
            }
        }
    }

    CatchesContent(
        isLoading = isLoading,
        query = searchQuery,
        logbooks = logbooks,
        listState = listState,
        onEvent = { event ->
            when (event) {
                CatchesEvent.SortIconClick -> {
                    showSortOptionsBottomSheet = true
                }

                is CatchesEvent.SortCatches -> {
                    scope.launch { sortOptionsBottomSheetState.hide() }.invokeOnCompletion {
                        if (!sortOptionsBottomSheetState.isVisible) {
                            showSortOptionsBottomSheet = false
                        }
                    }
                }

                is CatchesEvent.MoreOption -> {
                    showMoreOptionBottomSheet = true
                }

                CatchesEvent.DeleteLogbook -> openAlertDialog = false

                CatchesEvent.EditLogbook -> {
                    scope.launch { moreOptionBottomSheetState.hide() }.invokeOnCompletion {
                        if (!moreOptionBottomSheetState.isVisible) {
                            showMoreOptionBottomSheet = false
                        }
                    }
                    onNavigateToDetail(viewModel.logbookOnMore.value!!.toToDetailState(isInEditMode = true))
                }

                else -> Unit
            }
            viewModel.onEvent(event)
        },
        onItemClick = { it?.let { logbook -> onNavigateToDetail(logbook.toToDetailState(isInEditMode = false)) } },
        showMoreOptionBottomSheet = showMoreOptionBottomSheet,
        moreOptionBottomSheetState = moreOptionBottomSheetState,
        onMoreOptionBottomSheetDismissRequest = { showMoreOptionBottomSheet = false },
        openAlertDialog = openAlertDialog,
        onDismissRequest = { openAlertDialog = false },
        onDeleteClick = {
            openAlertDialog = true
            scope.launch { moreOptionBottomSheetState.hide() }.invokeOnCompletion {
                if (!moreOptionBottomSheetState.isVisible) {
                    showMoreOptionBottomSheet = false
                }
            }
        },
        showSortOptionsBottomSheet = showSortOptionsBottomSheet,
        sortOptionsBottomSheetState = sortOptionsBottomSheetState,
        onSortOptionsBottomSheetDismissRequest = { showSortOptionsBottomSheet = false },
        selectedSortOption = selectedSortOption
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CatchesContent(
    modifier: Modifier = Modifier,
    isLoading: Boolean,
    query: String,
    logbooks: List<Logbook?>?,
    listState: LazyListState,
    onEvent: (CatchesEvent) -> Unit,
    onItemClick: (logbook: Logbook?) -> Unit,
    showMoreOptionBottomSheet: Boolean,
    moreOptionBottomSheetState: SheetState,
    openAlertDialog: Boolean,
    onDismissRequest: () -> Unit,
    onDeleteClick: () -> Unit,
    onMoreOptionBottomSheetDismissRequest: () -> Unit,
    showSortOptionsBottomSheet: Boolean,
    sortOptionsBottomSheetState: SheetState,
    onSortOptionsBottomSheetDismissRequest: () -> Unit,
    selectedSortOption: SortBy,
) {

    if (openAlertDialog) {
        FishAlertDialog(
            onDismissRequest = onDismissRequest,
            onConfirmation = { onEvent(CatchesEvent.DeleteLogbook) },
            isDestructiveType = true,
            dialogTitle = "Delete catch",
            dialogText = "This action cannot be undone. Are you sure you want to delete this catch?",
            confirmText = "Delete",
            dismissText = "Cancel",
            icon = Icons.Outlined.DeleteForever
        )
    }

    if (showMoreOptionBottomSheet) {
        MoreOptionBottomSheet(
            onDismissRequest = onMoreOptionBottomSheetDismissRequest,
            sheetState = moreOptionBottomSheetState,
            onEditClick = { onEvent(CatchesEvent.EditLogbook) },
            onDeleteClick = onDeleteClick
        )
    }
    if (showSortOptionsBottomSheet) {
        SortOptionsBottomSheet(
            onDismissRequest = onSortOptionsBottomSheetDismissRequest,
            sheetState = sortOptionsBottomSheetState,
            selectedSortOption = selectedSortOption,
            onSortOptionClick = { onEvent(CatchesEvent.SortCatches(it)) }
        )
    }

    CatchesList(
        modifier = Modifier
            .fillMaxWidth()
            .then(modifier),
        listState = listState,
        query = query,
        onQueryChange = { onEvent(CatchesEvent.UpdateSearchQuery(it)) },
        onSort = { onEvent(CatchesEvent.SortIconClick) },
        logbooks = logbooks,
        isLoading = isLoading,
        onItemClick = onItemClick,
        onMoreClick = { onEvent(CatchesEvent.MoreOption(it!!)) },
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun CatchesContentPreview() {
    FischTheme {
        Surface {
            CatchesContent(
                isLoading = false,
                query = "",
                logbooks = null,
                listState = rememberLazyListState(),
                onEvent = {},
                onItemClick = {},
                showMoreOptionBottomSheet = false,
                moreOptionBottomSheetState = rememberModalBottomSheetState(),
                onMoreOptionBottomSheetDismissRequest = { },
                onSortOptionsBottomSheetDismissRequest = { },
                showSortOptionsBottomSheet = false,
                sortOptionsBottomSheetState = rememberModalBottomSheetState(),
                selectedSortOption = SortBy.LATEST,
                openAlertDialog = false,
                onDismissRequest = {},
                onDeleteClick = {}
            )
        }
    }
}