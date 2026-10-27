package com.example.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.CustomFlashcard
import com.example.data.IComCurriculumData
import com.example.data.IComSubject
import com.example.data.QuizAttempt
import com.example.data.StudyNote
import com.example.data.StudyRepository
import com.example.data.StudyTask
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

sealed interface UiState<out T> {
    data object Loading : UiState<Nothing>
    data class Success<T>(val data: T) : UiState<T>
    data class Error(val message: String) : UiState<Nothing>
}

class StudyViewModel(
    private val repository: StudyRepository,
    private val currentUserId: String
) : ViewModel() {

    private val _selectedPart = MutableStateFlow("PART_1")
    val selectedPart: StateFlow<String> = _selectedPart.asStateFlow()

    private val _selectedSubject = MutableStateFlow<IComSubject?>(null)
    val selectedSubject: StateFlow<IComSubject?> = _selectedSubject.asStateFlow()

    private val _statusMessage = MutableStateFlow<String?>(null)
    val statusMessage: StateFlow<String?> = _statusMessage.asStateFlow()

    val notesState: StateFlow<UiState<List<StudyNote>>> = repository.observeNotes(currentUserId)
        .map<List<StudyNote>, UiState<List<StudyNote>>> { list ->
            UiState.Success(list.sortedByDescending { it.createdAt?.seconds ?: 0L })
        }
        .catch { error ->
            Log.w(TAG, "Error observing notes", error)
            emit(UiState.Error(error.message ?: "Failed to load study notes"))
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(STOP_TIMEOUT_MILLIS),
            initialValue = UiState.Loading
        )

    val tasksState: StateFlow<UiState<List<StudyTask>>> = repository.observeTasks(currentUserId)
        .map<List<StudyTask>, UiState<List<StudyTask>>> { list ->
            UiState.Success(list.sortedWith(compareBy<StudyTask> { it.completed }.thenByDescending { it.createdAt?.seconds ?: 0L }))
        }
        .catch { error ->
            Log.w(TAG, "Error observing tasks", error)
            emit(UiState.Error(error.message ?: "Failed to load study planner tasks"))
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(STOP_TIMEOUT_MILLIS),
            initialValue = UiState.Loading
        )

    val quizAttemptsState: StateFlow<UiState<List<QuizAttempt>>> = repository.observeQuizAttempts(currentUserId)
        .map<List<QuizAttempt>, UiState<List<QuizAttempt>>> { list ->
            UiState.Success(list.sortedByDescending { it.createdAt?.seconds ?: 0L })
        }
        .catch { error ->
            Log.w(TAG, "Error observing quiz attempts", error)
            emit(UiState.Error(error.message ?: "Failed to load quiz history"))
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(STOP_TIMEOUT_MILLIS),
            initialValue = UiState.Loading
        )

    val flashcardsState: StateFlow<UiState<List<CustomFlashcard>>> = repository.observeFlashcards(currentUserId)
        .map<List<CustomFlashcard>, UiState<List<CustomFlashcard>>> { list ->
            UiState.Success(list.sortedByDescending { it.createdAt?.seconds ?: 0L })
        }
        .catch { error ->
            Log.w(TAG, "Error observing flashcards", error)
            emit(UiState.Error(error.message ?: "Failed to load flashcards"))
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(STOP_TIMEOUT_MILLIS),
            initialValue = UiState.Loading
        )

    fun selectPart(part: String) {
        _selectedPart.value = part
    }

    fun openSubjectDetail(subject: IComSubject?) {
        _selectedSubject.value = subject
    }

    fun clearStatusMessage() {
        _statusMessage.value = null
    }

    fun addStudyNote(
        title: String,
        content: String,
        subjectCode: String,
        part: String,
        isImportant: Boolean
    ) {
        if (title.isBlank() || content.isBlank()) return
        viewModelScope.launch {
            val result = repository.createNote(title, content, subjectCode, part, isImportant)
            result.onSuccess {
                _statusMessage.value = "Study note saved to cloud!"
            }.onFailure { err ->
                _statusMessage.value = err.localizedMessage ?: "Could not save note"
            }
        }
    }

    fun toggleNoteImportant(note: StudyNote) {
        viewModelScope.launch {
            repository.updateNote(note.copy(isImportant = !note.isImportant))
        }
    }

    fun deleteStudyNote(noteId: String) {
        viewModelScope.launch {
            repository.deleteNote(noteId)
        }
    }

    fun addStudyTask(
        title: String,
        subjectCode: String,
        part: String,
        priority: String,
        targetDate: String
    ) {
        if (title.isBlank()) return
        viewModelScope.launch {
            val result = repository.createTask(title, subjectCode, part, priority, targetDate)
            result.onSuccess {
                _statusMessage.value = "Revision task added!"
            }.onFailure { err ->
                _statusMessage.value = err.localizedMessage ?: "Could not add task"
            }
        }
    }

    fun toggleTask(task: StudyTask) {
        viewModelScope.launch {
            repository.toggleTaskCompleted(task)
        }
    }

    fun deleteTask(taskId: String) {
        viewModelScope.launch {
            repository.deleteTask(taskId)
        }
    }

    fun saveQuizAttempt(subjectCode: String, score: Int, totalQuestions: Int) {
        val subject = IComCurriculumData.getSubjectByCode(subjectCode)
        val subjectTitle = subject?.title ?: subjectCode
        val part = subject?.part ?: _selectedPart.value
        viewModelScope.launch {
            repository.recordQuizAttempt(
                subjectCode = subjectCode,
                subjectTitle = subjectTitle,
                part = part,
                score = score,
                totalQuestions = totalQuestions
            ).onSuccess {
                _statusMessage.value = "Quiz score ($score/$totalQuestions) saved!"
            }
        }
    }

    fun addCustomFlashcard(
        question: String,
        answer: String,
        subjectCode: String,
        part: String
    ) {
        if (question.isBlank() || answer.isBlank()) return
        viewModelScope.launch {
            repository.createFlashcard(question, answer, subjectCode, part).onSuccess {
                _statusMessage.value = "Custom flashcard added!"
            }
        }
    }

    fun toggleFlashcardMastered(card: CustomFlashcard) {
        viewModelScope.launch {
            repository.toggleFlashcardMastered(card)
        }
    }

    fun deleteCustomFlashcard(cardId: String) {
        viewModelScope.launch {
            repository.deleteFlashcard(cardId)
        }
    }

    private companion object {
        const val TAG = "StudyViewModel"
        const val STOP_TIMEOUT_MILLIS = 5000L
    }
}
