package com.notesmanager.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.notesmanager.NativeOperator
import com.notesmanager.R
import com.notesmanager.MainScreenViewModel

@Composable
fun DashboardScreen(vm: MainScreenViewModel) {

    /*

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        TitleText(text = stringResource(id = R.string.dashboard_title_welcome_user))
    }


*/

    MainLayout(modifier = Modifier, viewModel = vm)


}