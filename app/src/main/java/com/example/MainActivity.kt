package com.example

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.AppMode
import com.example.ui.MainViewModel
import com.example.ui.screens.AdminPanelScreen
import com.example.ui.screens.UserStoreScreen
import com.example.ui.theme.BgDeep
import com.example.ui.theme.MyApplicationTheme

class MainActivity : ComponentActivity() {
    private val mainViewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            val storeConfig by mainViewModel.storeConfig.collectAsState()
            val currentTheme = storeConfig?.themeName ?: "Blue"

            MyApplicationTheme(themeName = currentTheme) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = BgDeep
                ) {
                    LaptopsApp(viewModel = mainViewModel)
                }
            }
        }
    }
}

@Composable
fun LaptopsApp(viewModel: MainViewModel) {
    val appMode by viewModel.appMode.collectAsState()

    // Handle toast messages
    LaunchedEffect(Unit) {
        viewModel.toastMessage.collect { message ->
            Toast.makeText(viewModel.getApplication(), message, Toast.LENGTH_SHORT).show()
        }
    }

    Crossfade(targetState = appMode, label = "AppModeCrossfade") { mode ->
        when (mode) {
            AppMode.USER -> UserStoreScreen(viewModel = viewModel)
            AppMode.ADMIN -> AdminPanelScreen(viewModel = viewModel)
        }
    }
}
