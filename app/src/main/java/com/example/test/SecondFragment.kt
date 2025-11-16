package com.example.test

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment

class SecondFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Fragment ini jadi host Jetpack Compose
        return ComposeView(requireContext()).apply {
            setContent {
                ProfileApp()   // <- function Composable dari Scaffold.kt
            }
        }
    }
}
