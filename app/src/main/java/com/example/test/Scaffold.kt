package com.example.test

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

/* =========================
 * Data Kelompok
 * ========================= */

data class TeamMember(
    val name: String,
    val role: String,
    val bio: String,
    val responsibilities: String,
    val email: String? = null,
    val phone: String? = null,
    val whatsapp: String? = null,
    val linkedin: String? = null,
    val instagram: String? = null,
    val github: String? = null
)

private val teamMembers = listOf(
    TeamMember(
        name = "Muhammad Iqra Nur Fajar",
        role = "Ketua",
        bio = "Mahasiswa Sistem dan Teknologi Informasi yang tertarik pada pengembangan aplikasi Android, " +
                "automation, dan pemanfaatan data untuk pengambilan keputusan. Aktif mengerjakan side-project " +
                "serta eksplorasi teknologi baru untuk meningkatkan kemampuan teknis dan problem solving.",
        responsibilities = "Menginisiasi ide aplikasi, menyusun struktur project, mengintegrasikan Navigation + Fragment + Compose, " +
                "mengimplementasikan fitur utama (Scaffold, bottom navigation, theme toggle, bottom sheet info), " +
                "serta mengoordinasikan pembagian tugas anggota dan review hasil akhir.",
        email = "iqranurfajar0804@gmail.com",
        phone = "081342331223",
        // link WA bisa langsung dipakai untuk redirect
        whatsapp = "https://wa.me/6281342331223",
        linkedin = "https://www.linkedin.com/in/muhammad-iqra-nur-fajar-669829299",
        instagram = "https://www.instagram.com/qraaaaa_",
        github = "https://github.com/iqraa07"
    ),
    TeamMember(
        name = "Zalfa Naqiyah",
        role = "Anggota",
        bio = "Memiliki ketertarikan pada UI/UX dan desain antarmuka yang bersih, rapi, dan mudah dipahami pengguna.",
        responsibilities = "Mendesain struktur tampilan halaman About dan Contact, mengatur hierarki teks, " +
                "serta membantu pemilihan warna dan komponen Material 3 agar tampilan aplikasi konsisten."
    ),
    TeamMember(
        name = "Zakwan Abbas",
        role = "Anggota",
        bio = "Berfokus pada logika aplikasi dan alur navigasi agar pengalaman pengguna terasa mulus.",
        responsibilities = "Membantu integrasi Navigation Component, mengonfigurasi alur FirstFragment–SecondFragment, " +
                "dan memastikan perpindahan halaman berjalan stabil tanpa error."
    ),
    TeamMember(
        name = "Akmal Rifael",
        role = "Anggota",
        bio = "Tertarik pada kualitas kode dan dokumentasi teknis sederhana yang mudah dibaca.",
        responsibilities = "Mendukung penulisan kode yang terstruktur, melakukan refactor pada bagian yang berulang, " +
                "serta menyusun dokumentasi teknis singkat untuk menjelaskan fitur-fitur utama aplikasi."
    ),
    TeamMember(
        name = "Muh Akbar",
        role = "Anggota",
        bio = "Fokus pada pengujian fungsional aplikasi dan pengecekan tampilan di berbagai perangkat.",
        responsibilities = "Melakukan uji coba di emulator dan perangkat fisik, memastikan tampilan responsif, " +
                "serta membantu menyiapkan skenario demo/presentasi aplikasi."
    )
)

private const val PROGRAM_STUDI = "Sistem dan Teknologi Informasi"
private const val KELAS_INFO = "Praktikum Mobile App — Sesi 6"
private const val KAMPUS = "Nobel Indonesia Institute"

data class Skill(
    val name: String,
    val icon: ImageVector,
    val level: Float,         // 0f..1f
    val category: String
)

private val skillsData = listOf(
    Skill("Kotlin & Compose", Icons.Default.Code, 0.9f, "Mobile Development"),
    Skill("Android Studio", Icons.Default.PhoneAndroid, 0.8f, "Development Tools"),
    Skill("UI/UX Basic", Icons.Default.DesignServices, 0.75f, "Design & Layout"),
    Skill("Problem Solving", Icons.Default.Lightbulb, 0.85f, "Critical Thinking"),
    Skill("Version Control (Git)", Icons.Default.Storage, 0.7f, "Collaboration")
)

/* =========================
 * Bottom Navigation Model
 * ========================= */

sealed class ProfileScreen(
    val index: Int,
    val title: String,
    val label: String,
    val icon: ImageVector
) {
    data object About : ProfileScreen(
        0,
        "About — Profil Kelompok",
        "About",
        Icons.Default.Person
    )

    data object Skills : ProfileScreen(
        1,
        "Skills — Kemampuan Tim",
        "Skills",
        Icons.Default.Star
    )

    data object Contact : ProfileScreen(
        2,
        "Contact — Hubungi Kami",
        "Contact",
        Icons.Default.Phone
    )

    companion object {
        val all = listOf(About, Skills, Contact)
        fun fromIndex(index: Int): ProfileScreen = all.firstOrNull { it.index == index } ?: About
    }
}

/* =========================
 * Root App dengan Scaffold
 * ========================= */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileApp() {
    var selectedScreenIndex by rememberSaveable { mutableStateOf(0) }
    var isDarkTheme by rememberSaveable { mutableStateOf(false) }
    var selectedMemberIndex by rememberSaveable { mutableStateOf(0) }
    var showInfoSheet by rememberSaveable { mutableStateOf(false) }

    val currentScreen = ProfileScreen.fromIndex(selectedScreenIndex)

    val colorScheme = if (isDarkTheme) darkColorScheme() else lightColorScheme()
    val snackbarHostState = remember { SnackbarHostState() }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val scope = rememberCoroutineScope()

    val addRotation by animateFloatAsState(
        targetValue = if (showInfoSheet) 45f else 0f,
        label = "AddFabRotation"
    )

    MaterialTheme(colorScheme = colorScheme) {

        // Bottom sheet info ringkas (muncul saat tombol + ditekan)
        if (showInfoSheet) {
            InfoBottomSheet(
                sheetState = sheetState,
                ketua = teamMembers.first(),
                members = teamMembers.drop(1),
                onDismissRequest = { showInfoSheet = false }
            )
        }

        Scaffold(
            topBar = {
                CenterAlignedTopAppBar(
                    title = {
                        Text(
                            text = currentScreen.title,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.SemiBold
                        )
                    },
                    actions = {
                        IconButton(
                            onClick = {
                                scope.launch {
                                    snackbarHostState.showSnackbar(
                                        "Profil Kelompok STI — ${teamMembers.size} anggota • Fitur: Scaffold, Bottom Nav, Theme Toggle, Info Sheet"
                                    )
                                }
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Info,
                                contentDescription = "Info Aplikasi"
                            )
                        }
                    }
                )
            },
            bottomBar = {
                NavigationBar {
                    ProfileScreen.all.forEach { screen ->
                        NavigationBarItem(
                            selected = screen.index == selectedScreenIndex,
                            onClick = { selectedScreenIndex = screen.index },
                            label = { Text(screen.label) },
                            icon = { Icon(screen.icon, contentDescription = screen.label) }
                        )
                    }
                }
            },
            floatingActionButton = {
                // Dua FAB: (+) untuk info, lampu untuk dark/light theme
                Column(
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    horizontalAlignment = Alignment.End
                ) {
                    SmallFloatingActionButton(
                        onClick = { showInfoSheet = true }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Lihat Ringkasan Info",
                            modifier = Modifier.graphicsLayer {
                                rotationZ = addRotation
                            }
                        )
                    }
                    if (currentScreen is ProfileScreen.About) {
                        FloatingActionButton(
                            onClick = { isDarkTheme = !isDarkTheme }
                        ) {
                            Icon(
                                imageVector = if (isDarkTheme)
                                    Icons.Default.LightMode
                                else
                                    Icons.Default.DarkMode,
                                contentDescription = "Toggle Theme"
                            )
                        }
                    }
                }
            },
            snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
        ) { paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                contentAlignment = Alignment.TopCenter
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .widthIn(max = 600.dp)
                ) {
                    when (currentScreen) {
                        is ProfileScreen.About -> AboutScreen(
                            teamMembers = teamMembers,
                            selectedIndex = selectedMemberIndex,
                            onMemberSelected = { selectedMemberIndex = it }
                        )

                        is ProfileScreen.Skills -> SkillsScreen(skills = skillsData)

                        is ProfileScreen.Contact -> ContactScreen(
                            leader = teamMembers.first(),
                            onSendMessage = {
                                scope.launch {
                                    snackbarHostState.showSnackbar("Pesan berhasil dikirim ✔")
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}

/* =========================
 * Bottom Sheet Info (+)
 * ========================= */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InfoBottomSheet(
    sheetState: SheetState,
    ketua: TeamMember,
    members: List<TeamMember>,
    onDismissRequest: () -> Unit
) {
    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        sheetState = sheetState
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "Ringkasan Informasi",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = "Gunakan tab di bawah (About, Skills, Contact) untuk melihat detail profil, " +
                        "kemampuan tim, dan contact person. Tombol (+) ini menyediakan ringkasan cepat.",
                style = MaterialTheme.typography.bodyMedium
            )

            // Biodata ketua ringkas
            Card(
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = "Biodata Ketua",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(text = ketua.name, style = MaterialTheme.typography.bodyMedium)
                    Text(text = ketua.role, style = MaterialTheme.typography.bodySmall)
                    ketua.email?.let {
                        Text(text = "Email: $it", style = MaterialTheme.typography.bodySmall)
                    }
                    ketua.phone?.let {
                        Text(text = "Phone: $it", style = MaterialTheme.typography.bodySmall)
                    }
                }
            }

            // Ringkas anggota
            Card(
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = "Anggota Kelompok",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                    members.forEach { member ->
                        Text(
                            text = "• ${member.name} — ${member.role}",
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
            }

            // Ringkas fitur aplikasi
            Card(
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = "Fitur Aplikasi",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        text = "• About   – Profil ketua & anggota (bio dan tanggung jawab).\n" +
                                "• Skills  – Daftar skill tim dengan indikator level.\n" +
                                "• Contact – Contact person (ketua) + link portfolio online.\n" +
                                "• Theme   – Toggle Light / Dark mode melalui tombol FAB.\n" +
                                "• Info    – Ringkasan cepat melalui tombol (+) di pojok kanan bawah.",
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

/* =========================
 * About Screen
 * ========================= */

@Composable
fun AboutScreen(
    teamMembers: List<TeamMember>,
    selectedIndex: Int,
    onMemberSelected: (Int) -> Unit
) {
    val ketua = teamMembers.firstOrNull() ?: return
    val selectedMember = teamMembers.getOrNull(selectedIndex) ?: ketua
    val uriHandler = LocalUriHandler.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_launcher_foreground),
            contentDescription = "Foto Profil Ketua",
            modifier = Modifier
                .size(120.dp)
                .clip(CircleShape)
                .border(
                    width = 2.dp,
                    color = MaterialTheme.colorScheme.primary,
                    shape = CircleShape
                ),
            contentScale = ContentScale.Crop
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = ketua.name,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )
        Text(
            text = "Ketua Kelompok — $PROGRAM_STUDI",
            style = MaterialTheme.typography.bodyMedium
        )
        Text(
            text = KELAS_INFO,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.primary
        )
        Text(
            text = KAMPUS,
            style = MaterialTheme.typography.bodySmall
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Card deskripsi kelompok + fitur aplikasi
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "Tentang Kelompok & Aplikasi",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = "Kelompok ini mengerjakan tugas praktikum \"Scaffold & Jetpack Compose\" " +
                            "pada mata kuliah Pengembangan Aplikasi Mobile di $KAMPUS.",
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = "Aplikasi yang dikembangkan menampilkan profil ketua dan anggota, " +
                            "daftar skill utama, serta informasi kontak yang dibangun dengan Material 3. " +
                            "Struktur utama menggunakan Scaffold dengan TopAppBar, Bottom Navigation, " +
                            "Floating Action Button untuk toggle tema, dan konten yang responsif.",
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = "Project ini juga mendemonstrasikan integrasi antara Navigation Component (Fragment) " +
                            "dengan Jetpack Compose, sehingga cocok dijadikan template awal untuk aplikasi profil atau portfolio sederhana.",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Pilihan anggota (FilterChip horizontal)
        Text(
            text = "Pilih Profil Anggota",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.fillMaxWidth()
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            teamMembers.forEachIndexed { index, member ->
                FilterChip(
                    selected = index == selectedIndex,
                    onClick = { onMemberSelected(index) },
                    label = {
                        Text(
                            text = if (member == ketua)
                                "Ketua - ${member.name}"
                            else
                                member.name
                        )
                    }
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Detail anggota yang dipilih (pakai animasi ukuran)
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .animateContentSize(
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioMediumBouncy,
                        stiffness = Spring.StiffnessLow
                    )
                ),
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Text(
                    text = selectedMember.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Peran: ${selectedMember.role}",
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = "Profil Singkat",
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = selectedMember.bio,
                    style = MaterialTheme.typography.bodySmall
                )
                Text(
                    text = "Tanggung Jawab Utama",
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = selectedMember.responsibilities,
                    style = MaterialTheme.typography.bodySmall
                )

                // Khusus ketua: tampilkan social links & kontak online
                if (selectedMember == ketua &&
                    (ketua.linkedin != null || ketua.instagram != null || ketua.github != null)
                ) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Online Presence (Ketua)",
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.SemiBold
                    )
                    Column(
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        ketua.linkedin?.let { url ->
                            Text(
                                text = "LinkedIn : $url",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.clickable { uriHandler.openUri(url) }
                            )
                        }
                        ketua.instagram?.let { url ->
                            Text(
                                text = "Instagram: $url",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.clickable { uriHandler.openUri(url) }
                            )
                        }
                        ketua.github?.let { url ->
                            Text(
                                text = "GitHub   : $url",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.clickable { uriHandler.openUri(url) }
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}

/* =========================
 * Skills Screen
 * ========================= */

@Composable
fun SkillsScreen(
    skills: List<Skill>
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        item {
            Text(
                text = "Skills Kelompok",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Start
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Daftar kemampuan teknis dan non-teknis yang mendukung proses " +
                        "perancangan, pengembangan, dan presentasi aplikasi profil ini.",
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(modifier = Modifier.height(8.dp))
        }

        items(skills) { skill ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Icon(
                        imageVector = skill.icon,
                        contentDescription = skill.name,
                        modifier = Modifier.size(32.dp)
                    )
                    Column(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(
                            text = skill.name,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.SemiBold
                        )
                        Text(
                            text = skill.category,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.primary
                        )
                        LinearProgressIndicator(
                            progress = skill.level,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(6.dp)
                        )
                        Text(
                            text = "Penguasaan: ${(skill.level * 100).toInt()}%",
                            style = MaterialTheme.typography.bodySmall
                        )
                        Text(
                            text = "Skill ini berperan dalam menyusun UI, menulis kode yang terstruktur, " +
                                    "serta menjaga kerja sama tim selama pengerjaan tugas.",
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
            }
        }
    }
}

/* =========================
 * Contact Screen
 * ========================= */

@Composable
fun ContactScreen(
    leader: TeamMember,
    onSendMessage: () -> Unit
) {
    val uriHandler = LocalUriHandler.current

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = "Contact",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )

        // Card kontak utama (ketua)
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "Contact Person (Ketua)",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = leader.name,
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = "Program Studi: $PROGRAM_STUDI",
                    style = MaterialTheme.typography.bodySmall
                )
                leader.email?.let {
                    Text(
                        text = "Email : $it",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
                leader.phone?.let {
                    Text(
                        text = "Phone : $it",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
                Text(
                    text = "Silakan hubungi contact person di atas untuk diskusi terkait project, " +
                            "kolaborasi, atau masukan pengembangan aplikasi lebih lanjut.",
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }

        Text(
            text = "Online Portfolio & Kontak Cepat",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold
        )

        // Social & contact chips
        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            leader.linkedin?.let { url ->
                AssistChip(
                    onClick = { uriHandler.openUri(url) },
                    label = { Text("LinkedIn") },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Work,
                            contentDescription = "LinkedIn"
                        )
                    }
                )
            }
            leader.github?.let { url ->
                AssistChip(
                    onClick = { uriHandler.openUri(url) },
                    label = { Text("GitHub") },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Code,
                            contentDescription = "GitHub"
                        )
                    }
                )
            }
            leader.instagram?.let { url ->
                AssistChip(
                    onClick = { uriHandler.openUri(url) },
                    label = { Text("Instagram") },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.CameraAlt,
                            contentDescription = "Instagram"
                        )
                    }
                )
            }
        }

        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            leader.email?.let { mail ->
                AssistChip(
                    onClick = { uriHandler.openUri("mailto:$mail") },
                    label = { Text("Email") },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Email,
                            contentDescription = "Kirim Email"
                        )
                    }
                )
            }
            leader.phone?.let { phone ->
                AssistChip(
                    onClick = { uriHandler.openUri("tel:$phone") },
                    label = { Text("Telepon") },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Call,
                            contentDescription = "Panggil"
                        )
                    }
                )
            }
            leader.whatsapp?.let { url ->
                AssistChip(
                    onClick = { uriHandler.openUri(url) },
                    label = { Text("WhatsApp") },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Chat,
                            contentDescription = "WhatsApp"
                        )
                    }
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Button(onClick = onSendMessage) {
            Text("Send Message (Simulasi)")
        }

        Text(
            text = "Tombol di atas mensimulasikan pengiriman pesan ke tim. " +
                    "Pada pengembangan berikutnya, fitur ini dapat dihubungkan ke email, WhatsApp, " +
                    "atau backend API untuk menyimpan pesan pengguna.",
            style = MaterialTheme.typography.bodySmall
        )
    }
}
