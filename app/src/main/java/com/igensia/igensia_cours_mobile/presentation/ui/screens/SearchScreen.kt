package com.igensia.igensia_cours_mobile.presentation.ui.screens

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.igensia.igensia_cours_mobile.presentation.ui.theme.Igensia_cours_mobileTheme
import com.igensia.igensia_cours_mobile.presentation.viewmodel.MainViewModel

@Composable
fun SearchScreen(
    modifier: Modifier = Modifier,
    mainViewModel: MainViewModel = MainViewModel()
) {
    val list = mainViewModel.dataList.collectAsStateWithLifecycle().value

    Column(modifier = modifier) {
        println("SearchScreen()")
        Text(text = "Text1", fontSize = 20.sp)
        Spacer(Modifier.size(8.dp))
        Text(text = "Text2", fontSize = 14.sp)
        Spacer(Modifier.size(16.dp))

        for (weather in list) {
            PictureRowItem(text = weather.name, color = Color.Blue)
            PictureRowItem(text = weather.description, color = Color.DarkGray)
        }
    }
}

@Composable
fun PictureRowItem(
    text: String = "Hello from PictureRowItem",
    color: Color = Color.Blue,
    modifier: Modifier = Modifier
) {
    Text(
        text = text,
        color = color,
        modifier = modifier
    )
}

@Preview(showBackground = true, showSystemUi = true)
@Preview(
    showBackground = true,
    showSystemUi = true,
    uiMode = UI_MODE_NIGHT_YES
)
@Composable
fun SearchScreenPreview() {
    Igensia_cours_mobileTheme {
        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
            SearchScreen(modifier = Modifier.padding(innerPadding))
        }
    }
}
