package com.rehan.cricketworldcup

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.core.view.WindowCompat
import com.rehan.cricketworldcup.presentation.navigation.CricketNavGraph
import com.rehan.cricketworldcup.ui.theme.CricketWorldCupTheme
import com.rehan.cricketworldcup.ui.theme.GreenDark
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        WindowCompat.setDecorFitsSystemWindows(window, true)

        WindowCompat.getInsetsController(
            window,
            window.decorView
        ).isAppearanceLightStatusBars = false

        window.statusBarColor = GreenDark.toArgb()

        setContent {

            CricketWorldCupTheme {

                Surface(
                    modifier = Modifier.fillMaxSize()
                ) {

                    CricketNavGraph()
                }
            }
        }
    }
}