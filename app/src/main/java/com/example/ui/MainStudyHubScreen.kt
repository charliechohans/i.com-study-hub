package com.example.ui

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.AutoGraph
import androidx.compose.material.icons.filled.AutoStories
import androidx.compose.material.icons.filled.BookmarkAdd
import androidx.compose.material.icons.filled.BusinessCenter
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.EditNote
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.Quiz
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.credentials.CredentialManager
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.R
import com.example.data.IComCurriculumData
import com.example.data.IComSubject
import com.example.data.StudyRepository
import com.example.data.SubjectChapter
import com.google.firebase.firestore.FirebaseFirestore

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainStudyHubScreen(
    currentUserId: String,
    userEmail: String?,
    onSignOutComplete: () -> Unit
) {
    val viewModel: StudyViewModel = viewModel(
        key = currentUserId,
        factory = viewModelFactory {
            initializer {
                val app = checkNotNull(this[APPLICATION_KEY]) {
                    "APPLICATION_KEY missing from CreationExtras"
                }
                val databaseId = app.getString(R.string.firestore_database_id)
                val db = FirebaseFirestore.getInstance(databaseId)
                StudyViewModel(StudyRepository(db), currentUserId)
            }
        }
    )

    val selectedPart by viewModel.selectedPart.collectAsStateWithLifecycle()
    val selectedSubject by viewModel.selectedSubject.collectAsStateWithLifecycle()
    val notesState by viewModel.notesState.collectAsStateWithLifecycle()
    val tasksState by viewModel.tasksState.collectAsStateWithLifecycle()
    val quizAttemptsState by viewModel.quizAttemptsState.collectAsStateWithLifecycle()
    val flashcardsState by viewModel.flashcardsState.collectAsStateWithLifecycle()
    val statusMessage by viewModel.statusMessage.collectAsStateWithLifecycle()

    var currentBottomNavIndex by remember { mutableIntStateOf(0) }
    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val credentialManager = remember { CredentialManager.create(context) }

    LaunchedEffect(statusMessage) {
        statusMessage?.let { msg ->
            snackbarHostState.showSnackbar(msg)
            viewModel.clearStatusMessage()
        }
    }

    if (selectedSubject != null) {
        BackHandler {
            viewModel.openSubjectDetail(null)
        }
    } else if (currentBottomNavIndex != 0) {
        BackHandler {
            currentBottomNavIndex = 0
        }
    }

    Scaffold(
        contentWindowInsets = WindowInsets.safeDrawing,
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .shadow(6.dp, CircleShape)
                                .clip(CircleShape)
                                .background(
                                    Brush.linearGradient(
                                        listOf(Color(0xFFF59E0B), Color(0xFFD97706))
                                    )
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.School,
                                contentDescription = "I.Com Study Hub",
                                tint = Color(0xFF0A2540),
                                modifier = Modifier.size(22.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = selectedSubject?.title ?: "I.Com Study Hub 3D",
                                style = MaterialTheme.typography.titleLarge,
                                color = Color.White
                            )
                            Text(
                                text = userEmail ?: "Accounting • Economics • Commerce • Math",
                                style = MaterialTheme.typography.labelMedium,
                                color = Color(0xFFD1FAE5)
                            )
                        }
                    }
                },
                navigationIcon = {
                    if (selectedSubject != null) {
                        IconButton(
                            onClick = { viewModel.openSubjectDetail(null) },
                            modifier = Modifier.testTag("back_from_subject_button")
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Back to Dashboard",
                                tint = Color.White
                            )
                        }
                    }
                },
                actions = {
                    IconButton(
                        onClick = {
                            signOutUser(
                                credentialManager = credentialManager,
                                onSignOutComplete = onSignOutComplete,
                                scope = scope
                            )
                        },
                        modifier = Modifier.testTag("sign_out_button")
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.Logout,
                            contentDescription = "Sign Out",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF0A2540)
                )
            )
        },
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    selected = currentBottomNavIndex == 0,
                    onClick = {
                        viewModel.openSubjectDetail(null)
                        currentBottomNavIndex = 0
                    },
                    icon = { Icon(Icons.Default.AutoStories, contentDescription = "Dashboard") },
                    label = { Text("Dashboard") },
                    modifier = Modifier.testTag("nav_syllabus")
                )
                NavigationBarItem(
                    selected = currentBottomNavIndex == 1,
                    onClick = {
                        viewModel.openSubjectDetail(null)
                        currentBottomNavIndex = 1
                    },
                    icon = { Icon(Icons.Default.Calculate, contentDescription = "Commerce Lab") },
                    label = { Text("Lab & Rules") },
                    modifier = Modifier.testTag("nav_commerce_lab")
                )
                NavigationBarItem(
                    selected = currentBottomNavIndex == 2,
                    onClick = {
                        viewModel.openSubjectDetail(null)
                        currentBottomNavIndex = 2
                    },
                    icon = { Icon(Icons.Default.Quiz, contentDescription = "MCQs & Cards") },
                    label = { Text("MCQs & Cards") },
                    modifier = Modifier.testTag("nav_practice")
                )
                NavigationBarItem(
                    selected = currentBottomNavIndex == 3,
                    onClick = {
                        viewModel.openSubjectDetail(null)
                        currentBottomNavIndex = 3
                    },
                    icon = { Icon(Icons.Default.EditNote, contentDescription = "Notes & Plan") },
                    label = { Text("Notes & Plan") },
                    modifier = Modifier.testTag("nav_notes_planner")
                )
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            AnimatedContent(
                targetState = selectedSubject,
                transitionSpec = {
                    (fadeIn() + slideInVertically { it / 6 }) togetherWith
                        (fadeOut() + slideOutVertically { it / 6 })
                },
                label = "subject_detail_transition"
            ) { activeSubject ->
                if (activeSubject != null) {
                    SubjectDetailContent(
                        subject = activeSubject,
                        onQuickSaveFormulaNote = { chapter ->
                            viewModel.addStudyNote(
                                title = "${activeSubject.title}: Ch.${chapter.number} ${chapter.title}",
                                content = chapter.keyFormulasOrRules.joinToString("\n• ", prefix = "• "),
                                subjectCode = activeSubject.code,
                                part = activeSubject.part,
                                isImportant = true
                            )
                        },
                        onStartSubjectQuiz = {
                            viewModel.openSubjectDetail(null)
                            currentBottomNavIndex = 2
                        }
                    )
                } else {
                    when (currentBottomNavIndex) {
                        0 -> SyllabusDashboardContent(
                            selectedPart = selectedPart,
                            onSelectPart = viewModel::selectPart,
                            notesCount = (notesState as? UiState.Success)?.data?.size ?: 0,
                            pendingTasksCount = (tasksState as? UiState.Success)?.data?.count { !it.completed } ?: 0,
                            quizzesCount = (quizAttemptsState as? UiState.Success)?.data?.size ?: 0,
                            onSelectSubject = viewModel::openSubjectDetail,
                            onNavigateToTab = { tabIdx -> currentBottomNavIndex = tabIdx }
                        )
                        1 -> CommerceToolsScreen()
                        2 -> PracticeHubScreen(
                            selectedPart = selectedPart,
                            customFlashcardsState = flashcardsState,
                            quizAttemptsState = quizAttemptsState,
                            onSaveQuizAttempt = viewModel::saveQuizAttempt,
                            onAddCustomFlashcard = viewModel::addCustomFlashcard,
                            onToggleFlashcardMastered = viewModel::toggleFlashcardMastered,
                            onDeleteCustomFlashcard = viewModel::deleteCustomFlashcard
                        )
                        3 -> NotesAndPlannerScreen(
                            selectedPart = selectedPart,
                            notesState = notesState,
                            tasksState = tasksState,
                            onAddNote = viewModel::addStudyNote,
                            onToggleNoteImportant = viewModel::toggleNoteImportant,
                            onDeleteNote = viewModel::deleteStudyNote,
                            onAddTask = viewModel::addStudyTask,
                            onToggleTask = viewModel::toggleTask,
                            onDeleteTask = viewModel::deleteTask
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun SyllabusDashboardContent(
    selectedPart: String,
    onSelectPart: (String) -> Unit,
    notesCount: Int,
    pendingTasksCount: Int,
    quizzesCount: Int,
    onSelectSubject: (IComSubject) -> Unit,
    onNavigateToTab: (Int) -> Unit
) {
    // Core 4 flagship I.Com subjects explicitly highlighted on the Main Dashboard
    val coreFourSubjects = remember {
        listOfNotNull(
            IComCurriculumData.getSubjectByCode("ACC_1"),
            IComCurriculumData.getSubjectByCode("ECO_1"),
            IComCurriculumData.getSubjectByCode("POC_1"),
            IComCurriculumData.getSubjectByCode("BM_1")
        )
    }

    val part2Subjects = remember {
        IComCurriculumData.getSubjectsByPart("PART_2")
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .testTag("syllabus_dashboard"),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(18.dp)
    ) {
        // 1. 3D Animated Hero Banner
        item {
            Animated3DHeroBanner(
                notesCount = notesCount,
                pendingTasksCount = pendingTasksCount,
                quizzesCount = quizzesCount
            )
        }

        // 2. Quick 3D Pillars Strip for Core 4 Subjects (Accounting, Economics, Commerce, Business Math)
        item {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Core I.Com Subjects (3D Navigation)",
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Text(
                        text = "Tap to Tilt & Open",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.secondary
                    )
                }

                // 2x2 3D Subject Navigation Grid for Accounting, Economics, Principles of Commerce, Business Math
                val pairs = coreFourSubjects.chunked(2)
                pairs.forEachIndexed { rowIdx, rowSubjects ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        rowSubjects.forEachIndexed { colIdx, subject ->
                            val itemIndex = rowIdx * 2 + colIdx
                            Subject3DNavigationCard(
                                subject = subject,
                                index = itemIndex,
                                onClick = { onSelectSubject(subject) },
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }
            }
        }

        // 3. Part 1 / Part 2 Filter & Full Subject Explorer
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                FilterChip(
                    selected = selectedPart == "PART_1",
                    onClick = { onSelectPart("PART_1") },
                    label = {
                        Text(
                            text = "I.Com Part 1 (11th)",
                            modifier = Modifier.padding(vertical = 6.dp)
                        )
                    },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = MaterialTheme.colorScheme.primary,
                        selectedLabelColor = MaterialTheme.colorScheme.onPrimary
                    ),
                    modifier = Modifier
                        .weight(1f)
                        .testTag("part_1_chip")
                )
                FilterChip(
                    selected = selectedPart == "PART_2",
                    onClick = { onSelectPart("PART_2") },
                    label = {
                        Text(
                            text = "I.Com Part 2 (12th)",
                            modifier = Modifier.padding(vertical = 6.dp)
                        )
                    },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = MaterialTheme.colorScheme.primary,
                        selectedLabelColor = MaterialTheme.colorScheme.onPrimary
                    ),
                    modifier = Modifier
                        .weight(1f)
                        .testTag("part_2_chip")
                )
            }
        }

        // If user switches to Part 2, display the 3D Navigation Cards for Part 2 subjects as well
        if (selectedPart == "PART_2") {
            item {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text(
                        text = "I.Com Part 2 — 3D Subject Cards",
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    val part2Pairs = part2Subjects.chunked(2)
                    part2Pairs.forEachIndexed { rowIdx, rowSubjects ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(14.dp)
                        ) {
                            rowSubjects.forEachIndexed { colIdx, subject ->
                                val itemIndex = rowIdx * 2 + colIdx
                                Subject3DNavigationCard(
                                    subject = subject,
                                    index = itemIndex,
                                    onClick = { onSelectSubject(subject) },
                                    modifier = Modifier.weight(1f)
                                )
                            }
                        }
                    }
                }
            }
        }

        // 4. Interactive 3D Quick Launchers for Calculators, Quizzes & Cloud Notes
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(10.dp, RoundedCornerShape(22.dp)),
                shape = RoundedCornerShape(22.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            ) {
                Column(
                    modifier = Modifier.padding(18.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = "Quick Study Actions",
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        QuickAction3DButton(
                            title = "Dr / Cr Lab",
                            subtitle = "Calculators",
                            color = Color(0xFF059669),
                            onClick = { onNavigateToTab(1) },
                            modifier = Modifier.weight(1f)
                        )
                        QuickAction3DButton(
                            title = "Board MCQs",
                            subtitle = "Quizzes & Cards",
                            color = Color(0xFFD97706),
                            onClick = { onNavigateToTab(2) },
                            modifier = Modifier.weight(1f)
                        )
                        QuickAction3DButton(
                            title = "My Notes",
                            subtitle = "Cloud Planner",
                            color = Color(0xFF4F46E5),
                            onClick = { onNavigateToTab(3) },
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun QuickAction3DButton(
    title: String,
    subtitle: String,
    color: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .shadow(6.dp, RoundedCornerShape(16.dp), spotColor = color)
            .clip(RoundedCornerShape(16.dp))
            .background(
                Brush.verticalGradient(
                    listOf(color, color.copy(alpha = 0.80f))
                )
            )
            .border(1.dp, Color.White.copy(alpha = 0.35f), RoundedCornerShape(16.dp))
            .clickable(onClick = onClick)
            .padding(vertical = 12.dp, horizontal = 10.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = Color.White
            )
            Text(
                text = subtitle,
                style = MaterialTheme.typography.labelMedium,
                color = Color.White.copy(alpha = 0.85f)
            )
        }
    }
}

@Composable
private fun SubjectDetailContent(
    subject: IComSubject,
    onQuickSaveFormulaNote: (SubjectChapter) -> Unit,
    onStartSubjectQuiz: () -> Unit
) {
    val theme = remember(subject.code) { getSubject3DTheme(subject.code) }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .testTag("subject_detail_screen"),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(12.dp, RoundedCornerShape(24.dp), spotColor = theme.glowColor),
                shape = RoundedCornerShape(24.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Brush.linearGradient(theme.primaryGradient))
                        .padding(20.dp)
                ) {
                    Column {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = subject.urduSubtitle,
                                style = MaterialTheme.typography.titleMedium,
                                color = theme.glowColor
                            )
                            Text(
                                text = "Total: ${subject.totalMarks} | Pass: ${subject.passingMarks}",
                                style = MaterialTheme.typography.labelMedium,
                                color = Color.White
                            )
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = subject.description,
                            style = MaterialTheme.typography.bodyLarge,
                            color = Color.White.copy(alpha = 0.92f)
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(
                            onClick = onStartSubjectQuiz,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFFF59E0B),
                                contentColor = Color(0xFF0A2540)
                            ),
                            shape = RoundedCornerShape(14.dp)
                        ) {
                            Icon(Icons.Default.Quiz, contentDescription = "Take Quiz")
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Practice ${subject.title} MCQs & Flashcards",
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }

        items(subject.chapters, key = { it.number }) { chapter ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.weight(1f)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(CircleShape)
                                    .background(MaterialTheme.colorScheme.secondaryContainer),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "${chapter.number}",
                                    style = MaterialTheme.typography.titleMedium,
                                    color = MaterialTheme.colorScheme.onSecondaryContainer
                                )
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = chapter.title,
                                style = MaterialTheme.typography.titleLarge,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                        IconButton(
                            onClick = { onQuickSaveFormulaNote(chapter) },
                            modifier = Modifier.testTag("bookmark_chapter_${chapter.number}")
                        ) {
                            Icon(
                                imageVector = Icons.Default.BookmarkAdd,
                                contentDescription = "Save Chapter Formulas to My Cloud Notes",
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = chapter.summary,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(modifier = Modifier.height(12.dp))
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.55f),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier.padding(14.dp),
                            verticalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.Lightbulb,
                                    contentDescription = "Key Formulas & Rules",
                                    tint = MaterialTheme.colorScheme.secondary,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "Key Formulas, Rules & Concepts",
                                    style = MaterialTheme.typography.labelLarge,
                                    color = MaterialTheme.colorScheme.onSecondaryContainer
                                )
                            }
                            chapter.keyFormulasOrRules.forEach { rule ->
                                Text(
                                    text = "• $rule",
                                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium),
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "Board Exam Important Questions (Long / Short):",
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    chapter.importantQuestions.forEachIndexed { idx, q ->
                        Row(
                            modifier = Modifier.padding(vertical = 3.dp),
                            verticalAlignment = Alignment.Top
                        ) {
                            Icon(
                                imageVector = Icons.Default.Star,
                                contentDescription = "Important Question",
                                tint = Color(0xFFD97706),
                                modifier = Modifier
                                    .size(16.dp)
                                    .padding(top = 2.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Q${idx + 1}: $q",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                }
            }
        }
    }
}
