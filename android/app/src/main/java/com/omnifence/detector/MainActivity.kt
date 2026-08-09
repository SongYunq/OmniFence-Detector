package com.omnifence.detector

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.omnifence.detector.ui.OmniFenceApp
import com.omnifence.detector.ui.theme.OmniFenceTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            OmniFenceTheme {
                OmniFenceApp()
            }
        }
    }
}
