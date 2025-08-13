package com.notesmanager.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
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