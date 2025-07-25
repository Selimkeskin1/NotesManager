package com.notesmanager
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.MaterialTheme
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.Surface
import androidx.compose.ui.Modifier
import kotlin.getValue
import androidx.activity.ComponentActivity
import com.notesmanager.ui.MainLayout
import com.notesmanager.ui.theme.MainTheme



//class MainActivity : AppCompatActivity() {
class MainActivity : ComponentActivity() {

    private val vop = VirtualOperator()
    private val viewModel: ViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
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