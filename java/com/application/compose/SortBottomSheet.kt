package com.application.compose

import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview


@Composable
fun Check(name: String){
   Text(text = name)
}

@Preview()
@Composable
fun CheckPreview(){
   MaterialTheme{
      Check(name = "naveen")
   }
}