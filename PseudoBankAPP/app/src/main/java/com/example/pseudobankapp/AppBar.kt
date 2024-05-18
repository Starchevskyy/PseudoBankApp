package com.example.pseudobankapp

import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon

import androidx.compose.material.IconButton
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.pseudobankapp.R


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppBarView(
    title: String,
    onBackNavClicked: () -> Unit = {},
){
    val navigationIcon: (@Composable () -> Unit)? =
        //pokud titulek neobsahuje slovo (contains) => zobrazí se sipka zpet
        if (!title.contains("PseudoBank")){
            {
                IconButton(onClick = { onBackNavClicked() }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        tint = Color.White,
                        contentDescription = "Zpet"
                    )

                }
            }
        }else{ null}

    TopAppBar(title = { Text(text = title, color = colorResource(id= R.color.white),
        fontWeight = FontWeight.Bold,
        fontSize = 20.sp,
        modifier= Modifier
            .padding(start = 4.dp)
            .heightIn(max = 24.dp)) },
        elevation = 3.dp,
        backgroundColor = colorResource(id= R.color.blue),
        navigationIcon = navigationIcon,

    )

}