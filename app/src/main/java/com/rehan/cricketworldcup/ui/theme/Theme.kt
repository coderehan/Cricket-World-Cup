package com.rehan.cricketworldcup.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val LightColorScheme = lightColorScheme(

    primary = GreenPrimary,

    secondary = GreenAccent,

    tertiary = GreenDark,

    background = Background,

    surface = Surface,

    onPrimary = OnPrimary,

    onBackground = OnBackground
)

private val DarkColorScheme = darkColorScheme(

    primary = GreenLight,

    secondary = GreenAccent,

    tertiary = GreenPrimary
)

@Composable
fun CricketWorldCupTheme(
    darkTheme: Boolean = false,
    content: @Composable () -> Unit
) {

    MaterialTheme(

        colorScheme = if (darkTheme)
            DarkColorScheme
        else
            LightColorScheme,

        typography = Typography,

        content = content
    )
}