package com.example.pseudobankapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.pseudobankapp.HomeView
import com.example.pseudobankapp.Navigation
import com.example.pseudobankapp.ui.theme.PseudoBankAPPTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PseudoBankAPPTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {

                    //HomeView()
                    Navigation()

                }
            }
        }
    }
}
//------------------------------------------------------------------
/*  nejdříve je potřeba nastavit zavislosti
buidl.gradle.kts(:app) => dependencies

*/