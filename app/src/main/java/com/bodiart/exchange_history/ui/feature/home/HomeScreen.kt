package com.bodiart.exchange_history.ui.feature.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Button
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.bodiart.exchange_history.ui.feature.main.NavigationNode
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.MaterialDialogState
import com.vanpra.composematerialdialogs.datetime.date.datepicker
import com.vanpra.composematerialdialogs.rememberMaterialDialogState
import java.time.LocalDate

@Composable
fun HomeScreen(
    navController: NavController,
    vm: HomeViewModel = hiltViewModel()
) {
    val state by vm.viewStateFlow.collectAsState()
    val datePickerDialogState = rememberMaterialDialogState()

    LaunchedEffect(key1 = Unit) {
        vm.eventFlow.collect { event ->
            when (event) {
                HomeEvent.ShowDatePickerDialog -> datePickerDialogState.show()
                HomeEvent.HideDatePickerDialog -> datePickerDialogState.hide()
                is HomeEvent.OpenCalculator ->
                    navController.navigate(NavigationNode.Calculator.buildRoute(event.date))
                HomeEvent.OpenHistory -> navController.navigate(NavigationNode.History.route)
            }
        }
    }

    Scaffold { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Column(
                modifier = Modifier.align(Alignment.Center)
            ) {
                Button(onClick = vm::onCalculateClicked) {
                    Text("Calculate currency rate")
                }
                Spacer(modifier = Modifier.size(20.dp))
                Button(onClick = vm::onOpenHistoryClicked) {
                    Text("Watch operation history")
                }
            }

            DatePickerDialog(
                state = datePickerDialogState,
                onDateChange = vm::onDateChanged
            )
        }
    }
}

@Composable
private fun DatePickerDialog(
    state: MaterialDialogState,
    onDateChange: (LocalDate) -> Unit
) {
    MaterialDialog(
        dialogState = state,
        buttons = {
            positiveButton("Ok")
            negativeButton("Cancel")
        }
    ) {
        datepicker { date ->
            onDateChange(date)
        }
    }
}
