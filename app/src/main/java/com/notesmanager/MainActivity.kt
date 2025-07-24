package com.notesmanager

import android.content.pm.ActivityInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.ui.Modifier
import com.notesmanager.databinding.ActivityMainBinding
import kotlin.getValue
import androidx.activity.ComponentActivity

import com.notesmanager.ui.theme.MainTheme
import com.notesmanager.ui.*


//class MainActivity : AppCompatActivity() {
    class MainActivity : ComponentActivity() {

    private val vop = VirtualOperator()
    private val viewModel: ViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        lifecycle.addObserver(vop)
        viewModel.operator = vop
        setContent {
            MainTheme {
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colors.background) {
                    // pass the ViewModel down the composables' hierarchy
                    MainLayout(Modifier, viewModel)
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        lifecycle.removeObserver(vop)
    }
    override fun onResume() {
        super.onResume()
        viewModel.applyParameters()
    }



}