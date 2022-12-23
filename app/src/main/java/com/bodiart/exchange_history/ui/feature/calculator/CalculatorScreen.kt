package com.bodiart.exchange_history.ui.feature.calculator

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.MaterialDialogState
import com.vanpra.composematerialdialogs.listItemsSingleChoice
import com.vanpra.composematerialdialogs.rememberMaterialDialogState

@Composable
fun CalculatorScreen(
    navController: NavController,
    vm: CalculatorViewModel = hiltViewModel()
) {
    val state by vm.viewStateFlow.collectAsState()
    val originalCurrencyPickerDialogState = rememberMaterialDialogState()
    val targetCurrencyPickerDialogState = rememberMaterialDialogState()

    var originalCurrencyPickerDialogList by remember {
        mutableStateOf(listOf<String>())
    }
    var targetCurrencyPickerDialogList by remember {
        mutableStateOf(listOf<String>())
    }

    LaunchedEffect(key1 = Unit) { // move to separate func
        vm.eventFlow.collect { event ->
            when (event) {
                CalculatorEvent.Back -> navController.popBackStack()
                is CalculatorEvent.ShowOriginalCurrencyPickerDialog -> {
                    originalCurrencyPickerDialogList = event.currencies
                    originalCurrencyPickerDialogState.show()
                }
                is CalculatorEvent.ShowTargetCurrencyPickerDialog -> {
                    targetCurrencyPickerDialogList = event.currencies
                    targetCurrencyPickerDialogState.show()
                }
                CalculatorEvent.HideOriginalCurrencyPickerDialog ->
                    originalCurrencyPickerDialogState.hide()
                CalculatorEvent.HideTargetCurrencyPickerDialog ->
                    targetCurrencyPickerDialogState.hide()
            }
        }
    }

    Scaffold(
        topBar = {
            TopBar(title = "Calculator", onBackClicked = vm::onBackClicked)
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {

            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                Row(modifier = Modifier.fillMaxWidth()) {
                    CurrencyColumn(
                        currency = state.originalCurrency,
                        amount = state.originalCurrencyAmount,
                        isAmountEditable = true,
                        onCurrencyClicked = vm::onOriginalCurrencyClicked,
                        onAmountChanged = vm::onOriginalAmountChanged,
                        modifier = Modifier
                            .fillMaxWidth(0.5f)
                            .padding(16.dp),
                    )

                    CurrencyColumn(
                        currency = state.targetCurrency,
                        amount = state.targetCurrencyAmount,
                        isAmountEditable = false,
                        onCurrencyClicked = vm::onTargetCurrencyClicked,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                    )
                }

                Spacer(modifier = Modifier.size(20.dp))

                Button(
                    onClick = vm::onCalculateClicked,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                ) {
                    Text("Calculate")
                }
            }

            CurrencyPickerDialog(
                state = originalCurrencyPickerDialogState,
                currencies = originalCurrencyPickerDialogList,
                onCurrencySelected = vm::onOriginalCurrencySelected
            )

            CurrencyPickerDialog(
                state = targetCurrencyPickerDialogState,
                currencies = targetCurrencyPickerDialogList,
                onCurrencySelected = vm::onTargetCurrencySelected
            )

            if (state.isLoadingVisible) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .clickable {} // block clicks
                ) {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
            }
        }
    }
}

@Composable
private fun TopBar(
    title: String,
    onBackClicked: () -> Unit
) {
    TopAppBar(
        title = {
            Text(title)
        },
        navigationIcon = {
            IconButton(onClick = onBackClicked) {
                Icon(painterResource(id = android.R.drawable.ic_menu_close_clear_cancel), "")
            }
        },
        backgroundColor = Color.White,
        contentColor = Color.Black,
        elevation = 12.dp
    )
}

@Composable
private fun CurrencyPickerDialog(
    state: MaterialDialogState,
    currencies: List<String>,
    onCurrencySelected: (String) -> Unit
) {
    MaterialDialog(
        dialogState = state,
        buttons = {
            positiveButton("Ok")
            negativeButton("Cancel")
        }
    ) {
        listItemsSingleChoice(list = currencies) { selectedInd ->
            onCurrencySelected(currencies[selectedInd])
        }
    }
}

@Composable
private fun CurrencyColumn(
    currency: String?,
    amount: String,
    isAmountEditable: Boolean,
    onCurrencyClicked: () -> Unit,
    onAmountChanged: (String) -> Unit = {},
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
    ) {
        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = onCurrencyClicked
        ) {
            Text(currency ?: "Select currency")
        }

        Spacer(modifier = Modifier.size(16.dp))

        TextField(
            modifier = Modifier.fillMaxWidth(),
            value = amount,
            onValueChange = onAmountChanged,
            placeholder = {
                if (isAmountEditable) {
                    Text("Insert amount")
                }
            },
            readOnly = !isAmountEditable,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )
    }
}