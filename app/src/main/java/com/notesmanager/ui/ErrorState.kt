package com.notesmanager.ui

import androidx.annotation.StringRes
import com.notesmanager.R

data class ErrorState(
    val hasError: Boolean = false,
    @StringRes val errorMessageStringResource: Int = R.string.empty_string
)