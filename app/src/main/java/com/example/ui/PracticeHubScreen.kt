package com.example.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Flip
import androidx.compose.material.icons.filled.Quiz
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Style
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.data.CustomFlashcard
import com.example.data.IComCurriculumData
import com.example.data.QuizAttempt

@Composable
fun PracticeHubScreen(
    selectedPart: String,
    customFlashcardsState: UiState<List<CustomFlashcard>>,
    quizAttemptsState: UiState<List<QuizAttempt>>,
    onSaveQuizAttempt: (String, Int, Int) -> Unit,
    onAddCustomFlashcard: (String, String, String, String) -> Unit,
    onToggleFlashcardMastered: (CustomFlashcard) -> Unit,
    onDeleteCustomFlashcard: (String) -> Unit
) {
    var activeTab by remember { mutableIntStateOf(0) }
    val subjectsForPart = remember(selectedPart) {
        IComCurriculumData.getSubjectsByPart(selectedPart)
    }
    var selectedSubjectCode by remember(selectedPart) {
        mutableStateOf(subjectsForPart.firstOrNull()?.code ?: "ACC_1")
    }
    var showAddFlashcardDialog by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .testTag("practice_hub_screen")
    ) {
        TabRow(
            selectedTabIndex = activeTab,
            containerColor = MaterialTheme.colorScheme.surface
        ) {
            Tab(
                selected = activeTab == 0,
                onClick = { activeTab = 0 },
                text = { Text("MCQ Quiz (کثیر الانتخابی)") },
                icon = { Icon(Icons.Default.Quiz, contentDescription = "MCQ Quiz Tab") },
                modifier = Modifier.testTag("tab_mcq_quiz")
            )
            Tab(
                selected = activeTab == 1,
                onClick = { activeTab = 1 },
                text = { Text("Flashcards (فلیش کارڈز)") },
                icon = { Icon(Icons.Default.Style, contentDescription = "Flashcards Tab") },
                modifier = Modifier.testTag("tab_flashcards")
            )
        }

        // Subject selector row
        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 10.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(subjectsForPart) { subject ->
                FilterChip(
                    selected = selectedSubjectCode == subject.code,
                    onClick = { selectedSubjectCode = subject.code },
                    label = { Text(subject.title) },
                    modifier = Modifier.testTag("practice_subject_chip_${subject.code}")
                )
            }
        }

        val currentSubject = IComCurriculumData.getSubjectByCode(selectedSubjectCode)
            ?: subjectsForPart.first()

        if (activeTab == 0) {
            McqPracticeSection(
                subject = currentSubject,
                quizAttemptsState = quizAttemptsState,
                onSaveQuizAttempt = onSaveQuizAttempt
            )
        } else {
            FlashcardPracticeSection(
                subject = currentSubject,
                customFlashcardsState = customFlashcardsState,
                onOpenAddDialog = { showAddFlashcardDialog = true },
                onToggleMastered = onToggleFlashcardMastered,
                onDeleteCard = onDeleteCustomFlashcard
            )
        }
    }

    if (showAddFlashcardDialog) {
        AddCustomFlashcardDialog(
            subjectCode = selectedSubjectCode,
            part = selectedPart,
            onDismiss = { showAddFlashcardDialog = false },
            onSave = { q, a ->
                onAddCustomFlashcard(q, a, selectedSubjectCode, selectedPart)
                showAddFlashcardDialog = false
            }
        )
    }
}

@Composable
private fun McqPracticeSection(
    subject: com.example.data.IComSubject,
    quizAttemptsState: UiState<List<QuizAttempt>>,
    onSaveQuizAttempt: (String, Int, Int) -> Unit
) {
    val questions = subject.builtInMcqs
    val selectedAnswers = remember(subject.code) { mutableStateMapOf<String, Int>() }
    var submitted by remember(subject.code) { mutableStateOf(false) }

    val score = questions.count { q -> selectedAnswers[q.id] == q.correctIndex }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.EmojiEvents,
                        contentDescription = "Board Exam MCQs",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(36.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "${subject.title} — Board MCQs",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                        Text(
                            text = "20% of I.Com Board Paper consists of Objective MCQs. Test your preparation!",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f)
                        )
                    }
                }
            }
        }

        items(questions) { q ->
            val userChoice = selectedAnswers[q.id]
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = q.question,
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    q.options.forEachIndexed { idx, optText ->
                        val isSelected = userChoice == idx
                        val isCorrectOption = idx == q.correctIndex
                        val rowBg = when {
                            !submitted && isSelected -> MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f)
                            submitted && isCorrectOption -> Color(0xFF059669).copy(alpha = 0.18f)
                            submitted && isSelected && !isCorrectOption -> Color(0xFFDC2626).copy(alpha = 0.18f)
                            else -> Color.Transparent
                        }

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(10.dp))
                                .background(rowBg)
                                .clickable(enabled = !submitted) {
                                    selectedAnswers[q.id] = idx
                                }
                                .padding(vertical = 4.dp, horizontal = 8.dp)
                        ) {
                            RadioButton(
                                selected = isSelected,
                                onClick = {
                                    if (!submitted) selectedAnswers[q.id] = idx
                                }
                            )
                            Text(
                                text = optText,
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }

                    AnimatedVisibility(visible = submitted) {
                        Surface(
                            color = MaterialTheme.colorScheme.surfaceVariant,
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 8.dp)
                        ) {
                            Text(
                                text = "Explanation: ${q.explanation}",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.padding(12.dp)
                            )
                        }
                    }
                }
            }
        }

        item {
            if (!submitted) {
                Button(
                    onClick = {
                        submitted = true
                        onSaveQuizAttempt(subject.code, score, questions.size)
                    },
                    enabled = selectedAnswers.size == questions.size,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                        .testTag("submit_quiz_button"),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Icon(Icons.Default.CheckCircle, contentDescription = "Submit Quiz")
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Submit Quiz & Save Score (${selectedAnswers.size}/${questions.size} answered)")
                }
            } else {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Your Score: $score / ${questions.size}",
                            style = MaterialTheme.typography.headlineMedium,
                            color = MaterialTheme.colorScheme.onSecondaryContainer
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedButton(
                            onClick = {
                                selectedAnswers.clear()
                                submitted = false
                            }
                        ) {
                            Icon(Icons.Default.Refresh, contentDescription = "Retry Quiz")
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Retake Quiz")
                        }
                    }
                }
            }
        }

        // Recent Cloud Quiz Attempts
        item {
            if (quizAttemptsState is UiState.Success && quizAttemptsState.data.isNotEmpty()) {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = "Recent Cloud-Saved Quiz Scores",
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                    quizAttemptsState.data.take(5).forEach { attempt ->
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = MaterialTheme.colorScheme.surface,
                            tonalElevation = 2.dp,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier.padding(12.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = attempt.subjectTitle,
                                    style = MaterialTheme.typography.bodyLarge
                                )
                                Surface(
                                    shape = RoundedCornerShape(8.dp),
                                    color = MaterialTheme.colorScheme.secondaryContainer
                                ) {
                                    Text(
                                        text = "${attempt.score} / ${attempt.totalQuestions}",
                                        style = MaterialTheme.typography.labelLarge,
                                        color = MaterialTheme.colorScheme.onSecondaryContainer,
                                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun FlashcardPracticeSection(
    subject: com.example.data.IComSubject,
    customFlashcardsState: UiState<List<CustomFlashcard>>,
    onOpenAddDialog: () -> Unit,
    onToggleMastered: (CustomFlashcard) -> Unit,
    onDeleteCard: (String) -> Unit
) {
    val flippedCardIds = remember { mutableStateMapOf<String, Boolean>() }
    val userCards = (customFlashcardsState as? UiState.Success)?.data
        ?.filter { it.subjectCode == subject.code }
        ?: emptyList()

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Tap any card to flip Answer",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(
                        text = "${subject.builtInFlashcards.size} Board Cards + ${userCards.size} Custom Cards",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Button(
                    onClick = onOpenAddDialog,
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.testTag("add_flashcard_button")
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Add Flashcard")
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("New Card")
                }
            }
        }

        // User's Custom Cloud Flashcards
        if (userCards.isNotEmpty()) {
            item {
                Text(
                    text = "Your Cloud Flashcards",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.secondary
                )
            }
            items(userCards, key = { it.id }) { card ->
                val isFlipped = flippedCardIds[card.id] == true
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { flippedCardIds[card.id] = !isFlipped },
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (isFlipped) MaterialTheme.colorScheme.secondaryContainer else MaterialTheme.colorScheme.surface
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
                ) {
                    Column(modifier = Modifier.padding(18.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = if (card.mastered) Color(0xFF059669).copy(alpha = 0.2f) else MaterialTheme.colorScheme.primaryContainer
                            ) {
                                Text(
                                    text = if (card.mastered) "Mastered ✓" else "Custom Card",
                                    style = MaterialTheme.typography.labelMedium,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                )
                            }
                            Row {
                                TextButton(onClick = { onToggleMastered(card) }) {
                                    Text(if (card.mastered) "Unmark" else "Mark Mastered")
                                }
                                IconButton(onClick = { onDeleteCard(card.id) }) {
                                    Icon(
                                        Icons.Default.Delete,
                                        contentDescription = "Delete Flashcard",
                                        tint = MaterialTheme.colorScheme.error
                                    )
                                }
                            }
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = if (isFlipped) card.answer else card.question,
                            style = if (isFlipped) MaterialTheme.typography.bodyLarge else MaterialTheme.typography.titleLarge,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                Icons.Default.Flip,
                                contentDescription = "Flip card",
                                modifier = Modifier.size(16.dp),
                                tint = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = if (isFlipped) "Showing Answer (Tap to see Question)" else "Showing Question (Tap to reveal Answer)",
                                style = MaterialTheme.typography.labelMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }
        }

        // Built-in Syllabus Flashcards
        item {
            Text(
                text = "Essential Board Exam Flashcards",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.primary
            )
        }

        items(subject.builtInFlashcards, key = { it.id }) { card ->
            val isFlipped = flippedCardIds[card.id] == true
            val rotationY by animateFloatAsState(
                targetValue = if (isFlipped) 180f else 0f,
                animationSpec = tween(durationMillis = 480, easing = FastOutSlowInEasing),
                label = "flip_card_${card.id}"
            )
            val showBackFace = rotationY > 90f

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .graphicsLayer {
                        this.rotationY = rotationY
                        cameraDistance = 14f * density
                    }
                    .clickable { flippedCardIds[card.id] = !isFlipped }
                    .testTag("builtin_flashcard_${card.id}"),
                shape = RoundedCornerShape(20.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .graphicsLayer {
                            if (showBackFace) {
                                this.rotationY = 180f
                            }
                        }
                        .background(
                            if (showBackFace) {
                                Brush.horizontalGradient(
                                    listOf(Color(0xFF065F46), Color(0xFF047857))
                                )
                            } else {
                                Brush.horizontalGradient(
                                    listOf(Color(0xFF0A2540), Color(0xFF1E3A5F))
                                )
                            }
                        )
                        .padding(20.dp)
                ) {
                    Column {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = Color.White.copy(alpha = 0.16f)
                            ) {
                                Text(
                                    text = card.tag,
                                    style = MaterialTheme.typography.labelMedium,
                                    color = Color(0xFFFBBF24),
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                                )
                            }
                            Text(
                                text = if (showBackFace) "3D FLIP • ANSWER" else "3D FLIP • QUESTION",
                                style = MaterialTheme.typography.labelMedium,
                                color = Color.White.copy(alpha = 0.7f)
                            )
                        }
                        Spacer(modifier = Modifier.height(14.dp))
                        Text(
                            text = if (showBackFace) card.answer else card.question,
                            style = MaterialTheme.typography.titleLarge,
                            color = Color.White
                        )
                        Spacer(modifier = Modifier.height(14.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                Icons.Default.Flip,
                                contentDescription = "Tap to flip in 3D",
                                tint = Color.White.copy(alpha = 0.75f),
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Tap card for 3D flip",
                                style = MaterialTheme.typography.bodyMedium,
                                color = Color.White.copy(alpha = 0.75f)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun AddCustomFlashcardDialog(
    subjectCode: String,
    part: String,
    onDismiss: () -> Unit,
    onSave: (String, String) -> Unit
) {
    var question by remember { mutableStateOf("") }
    var answer by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Create Custom Flashcard ($subjectCode)") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedTextField(
                    value = question,
                    onValueChange = { question = it },
                    label = { Text("Front: Question / Term / Formula") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("flashcard_question_input")
                )
                OutlinedTextField(
                    value = answer,
                    onValueChange = { answer = it },
                    label = { Text("Back: Definition / Accounting Rule") },
                    minLines = 3,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("flashcard_answer_input")
                )
            }
        },
        confirmButton = {
            Button(
                onClick = { onSave(question, answer) },
                enabled = question.isNotBlank() && answer.isNotBlank(),
                modifier = Modifier.testTag("save_flashcard_button")
            ) {
                Text("Save to Cloud")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}
