package com.bodiart.exchange_history.ui.feature.history

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.bodiart.exchange_history.ui.theme.Typography

@Composable
fun HistoryScreen(vm: HistoryViewModel = hiltViewModel()) {
    val state by vm.viewStateFlow.collectAsState()

    Scaffold { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(state.operations) { item -> // no key provided, for 10 items its ok
                CurrencyOperationScreenItem(item)
            }
        }
    }
}

@Composable
private fun CurrencyOperationScreenItem(item: HistoryState.Operation) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, Color.Black, RoundedCornerShape(15.dp))
            .padding(16.dp)
    ) {
        Text(
            text = item.originalCurrency,
            style = Typography.body1
        )
        Text(
            text = item.originalCurrencyAmount,
            style = Typography.body2
        )
        Spacer(modifier = Modifier.size(16.dp))
        Image(
            painter = painterResource(id = android.R.drawable.arrow_down_float),
            contentDescription = "",
            colorFilter = ColorFilter.tint(Color.Black)
        )
        Spacer(modifier = Modifier.size(16.dp))
        Text(
            text = item.targetCurrency,
            style = Typography.body1
        )
        Text(
            text = item.targetCurrencyAmount,
            style = Typography.body2
        )
    }
}