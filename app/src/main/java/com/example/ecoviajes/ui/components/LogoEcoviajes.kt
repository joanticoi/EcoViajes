package com.example.ecoviajes.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.ecoviajes.R

@Composable
fun LogoEcoviajes(modifier: Modifier = Modifier) {
    Image(
        painter = painterResource(id = R.drawable.logo2), // usa tu logo
        contentDescription = "Logo Ecoviajes",
        modifier = modifier.size(120.dp)
    )
}
