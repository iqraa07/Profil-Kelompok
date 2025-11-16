package com.example.test

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController

class FirstFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Semua UI dibuat pakai Compose, tidak pakai XML lagi
        return ComposeView(requireContext()).apply {
            setContent {
                FirstLandingScreen(
                    onStartClick = {
                        findNavController().navigate(
                            R.id.action_FirstFragment_to_SecondFragment
                        )
                    }
                )
            }
        }
    }
}

/**
 * Halaman pembuka (landing page) aplikasi.
 * Menjelaskan konteks tugas, susunan kelompok, dan fitur utama
 * sebelum user masuk ke halaman ProfileApp (Scaffold).
 */
@Composable
fun FirstLandingScreen(
    onStartClick: () -> Unit
) {
    MaterialTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .widthIn(max = 600.dp)
                        .verticalScroll(rememberScrollState()),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    // Title
                    Text(
                        text = "Profil Kelompok – Praktikum Mobile App",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )
                    Text(
                        text = "Sesi 6 · Scaffold & Jetpack Compose",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.primary,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(top = 4.dp, bottom = 12.dp)
                    )

                    // Card highlight aplikasi
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = CardDefaults.shape,
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(
                                text = "Ringkasan Aplikasi",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.SemiBold
                            )
                            Text(
                                text = "Aplikasi ini menampilkan profil ketua dan anggota kelompok, " +
                                        "daftar kemampuan (skills), serta informasi kontak dengan tampilan " +
                                        "yang disusun menggunakan Material 3 Scaffold.",
                                style = MaterialTheme.typography.bodyMedium
                            )
                            Text(
                                text = "Project ini juga mendemonstrasikan integrasi antara " +
                                        "Navigation Component (Fragment) dan Jetpack Compose, sehingga " +
                                        "cocok dijadikan template awal untuk aplikasi profil atau portfolio.",
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Card susunan kelompok
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = CardDefaults.shape,
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Text(
                                text = "Susunan Kelompok",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.SemiBold
                            )
                            Text(
                                text = "Ketua : Muhammad Iqra Nur Fajar",
                                style = MaterialTheme.typography.bodyMedium
                            )
                            Text(
                                text = "Anggota:",
                                style = MaterialTheme.typography.bodyMedium
                            )
                            Text(
                                text = "• Zalfa Naqiyah\n" +
                                        "• Zakwan Abbas\n" +
                                        "• Akmal Rifael\n" +
                                        "• Muh Akbar",
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Card fitur utama
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = CardDefaults.shape,
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Text(
                                text = "Fitur yang Dapat Dieksplor",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.SemiBold
                            )
                            Text(
                                text = "• About   – Profil ketua & anggota dengan deskripsi peran.\n" +
                                        "• Skills  – Daftar kemampuan tim lengkap dengan level penguasaan.\n" +
                                        "• Contact – Contact person (ketua) + portfolio online (LinkedIn, GitHub, Instagram).\n" +
                                        "• Theme   – Tombol FAB di tab About untuk mengganti Light/Dark theme.",
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    // Tombol masuk ke halaman profil
                    Button(
                        onClick = onStartClick,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(
                            imageVector = Icons.Default.PlayArrow,
                            contentDescription = null
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = "Buka Profil Kelompok")
                    }
                }
            }
        }
    }
}
