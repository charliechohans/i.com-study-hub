package com.example.data

import android.content.Context
import com.example.R
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.snapshots
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await

class StudyRepository(private val db: FirebaseFirestore) {

    constructor(context: Context) : this(
        FirebaseFirestore.getInstance(
            context.applicationContext.getString(R.string.firestore_database_id)
        )
    )

    private val auth = Firebase.auth

    private fun requireUserId(): String {
        return auth.currentUser?.uid
            ?: throw IllegalStateException("User must be signed in with Google before accessing Firestore.")
    }

    // --- STUDY NOTES ---

    fun observeNotes(userId: String): Flow<List<StudyNote>> {
        val path = "users/$userId/notes"
        return db.collection("users").document(userId).collection("notes")
            .snapshots()
            .map { snapshot ->
                snapshot.toObjects(StudyNote::class.java, DocumentSnapshot.ServerTimestampBehavior.ESTIMATE)
            }
            .catch { error ->
                if (error is Exception) handleFirestoreError(error, OperationType.LIST, path)
                throw error
            }
    }

    suspend fun getNoteById(userId: String, noteId: String): Result<StudyNote?> {
        val path = "users/$userId/notes/$noteId"
        return try {
            val doc = db.collection("users").document(userId).collection("notes").document(noteId).get().await()
            Result.success(doc.toObject(StudyNote::class.java, DocumentSnapshot.ServerTimestampBehavior.ESTIMATE))
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            handleFirestoreError(e, OperationType.GET, path)
            Result.failure(e)
        }
    }

    suspend fun createNote(
        title: String,
        content: String,
        subjectCode: String,
        part: String,
        isImportant: Boolean
    ): Result<String> {
        val uid = requireUserId()
        val notesRef = db.collection("users").document(uid).collection("notes")
        val payload = mapOf(
            "userId" to uid,
            "title" to title.trim().take(150),
            "content" to content.trim().take(5000),
            "subjectCode" to subjectCode,
            "part" to part,
            "isImportant" to isImportant,
            "createdAt" to FieldValue.serverTimestamp(),
            "updatedAt" to FieldValue.serverTimestamp()
        )
        return try {
            val docRef = notesRef.add(payload).await()
            Result.success(docRef.id)
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            handleFirestoreError(e, OperationType.CREATE, notesRef.path)
            Result.failure(e)
        }
    }

    suspend fun updateNote(note: StudyNote): Result<Unit> {
        val uid = requireUserId()
        require(note.id.isNotBlank()) { "Note ID must not be blank" }
        requireNotNull(note.createdAt) { "Note createdAt must not be null" }
        val docRef = db.collection("users").document(uid).collection("notes").document(note.id)
        return try {
            docRef.set(note.copy(updatedAt = null)).await()
            Result.success(Unit)
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            handleFirestoreError(e, OperationType.UPDATE, docRef.path)
            Result.failure(e)
        }
    }

    suspend fun deleteNote(noteId: String): Result<Unit> {
        val uid = requireUserId()
        val docRef = db.collection("users").document(uid).collection("notes").document(noteId)
        return try {
            docRef.delete().await()
            Result.success(Unit)
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            handleFirestoreError(e, OperationType.DELETE, docRef.path)
            Result.failure(e)
        }
    }

    // --- STUDY TASKS ---

    fun observeTasks(userId: String): Flow<List<StudyTask>> {
        val path = "users/$userId/tasks"
        return db.collection("users").document(userId).collection("tasks")
            .snapshots()
            .map { snapshot ->
                snapshot.toObjects(StudyTask::class.java, DocumentSnapshot.ServerTimestampBehavior.ESTIMATE)
            }
            .catch { error ->
                if (error is Exception) handleFirestoreError(error, OperationType.LIST, path)
                throw error
            }
    }

    suspend fun createTask(
        title: String,
        subjectCode: String,
        part: String,
        priority: String,
        targetDate: String
    ): Result<String> {
        val uid = requireUserId()
        val tasksRef = db.collection("users").document(uid).collection("tasks")
        val payload = mapOf(
            "userId" to uid,
            "title" to title.trim().take(200),
            "subjectCode" to subjectCode,
            "part" to part,
            "priority" to priority,
            "completed" to false,
            "targetDate" to targetDate.trim().take(60),
            "createdAt" to FieldValue.serverTimestamp(),
            "updatedAt" to FieldValue.serverTimestamp()
        )
        return try {
            val docRef = tasksRef.add(payload).await()
            Result.success(docRef.id)
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            handleFirestoreError(e, OperationType.CREATE, tasksRef.path)
            Result.failure(e)
        }
    }

    suspend fun toggleTaskCompleted(task: StudyTask): Result<Unit> {
        val uid = requireUserId()
        require(task.id.isNotBlank()) { "Task ID must not be blank" }
        requireNotNull(task.createdAt) { "Task createdAt must not be null" }
        val docRef = db.collection("users").document(uid).collection("tasks").document(task.id)
        return try {
            docRef.set(task.copy(completed = !task.completed, updatedAt = null)).await()
            Result.success(Unit)
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            handleFirestoreError(e, OperationType.UPDATE, docRef.path)
            Result.failure(e)
        }
    }

    suspend fun deleteTask(taskId: String): Result<Unit> {
        val uid = requireUserId()
        val docRef = db.collection("users").document(uid).collection("tasks").document(taskId)
        return try {
            docRef.delete().await()
            Result.success(Unit)
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            handleFirestoreError(e, OperationType.DELETE, docRef.path)
            Result.failure(e)
        }
    }

    // --- QUIZ ATTEMPTS ---

    fun observeQuizAttempts(userId: String): Flow<List<QuizAttempt>> {
        val path = "users/$userId/quiz_attempts"
        return db.collection("users").document(userId).collection("quiz_attempts")
            .snapshots()
            .map { snapshot ->
                snapshot.toObjects(QuizAttempt::class.java, DocumentSnapshot.ServerTimestampBehavior.ESTIMATE)
            }
            .catch { error ->
                if (error is Exception) handleFirestoreError(error, OperationType.LIST, path)
                throw error
            }
    }

    suspend fun recordQuizAttempt(
        subjectCode: String,
        subjectTitle: String,
        part: String,
        score: Int,
        totalQuestions: Int
    ): Result<String> {
        val uid = requireUserId()
        val attemptsRef = db.collection("users").document(uid).collection("quiz_attempts")
        val payload = mapOf(
            "userId" to uid,
            "subjectCode" to subjectCode,
            "subjectTitle" to subjectTitle.trim().take(100),
            "part" to part,
            "score" to score.toLong(),
            "totalQuestions" to totalQuestions.toLong(),
            "createdAt" to FieldValue.serverTimestamp()
        )
        return try {
            val docRef = attemptsRef.add(payload).await()
            Result.success(docRef.id)
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            handleFirestoreError(e, OperationType.CREATE, attemptsRef.path)
            Result.failure(e)
        }
    }

    // --- CUSTOM FLASHCARDS ---

    fun observeFlashcards(userId: String): Flow<List<CustomFlashcard>> {
        val path = "users/$userId/flashcards"
        return db.collection("users").document(userId).collection("flashcards")
            .snapshots()
            .map { snapshot ->
                snapshot.toObjects(CustomFlashcard::class.java, DocumentSnapshot.ServerTimestampBehavior.ESTIMATE)
            }
            .catch { error ->
                if (error is Exception) handleFirestoreError(error, OperationType.LIST, path)
                throw error
            }
    }

    suspend fun createFlashcard(
        question: String,
        answer: String,
        subjectCode: String,
        part: String
    ): Result<String> {
        val uid = requireUserId()
        val cardsRef = db.collection("users").document(uid).collection("flashcards")
        val payload = mapOf(
            "userId" to uid,
            "question" to question.trim().take(300),
            "answer" to answer.trim().take(1000),
            "subjectCode" to subjectCode,
            "part" to part,
            "mastered" to false,
            "createdAt" to FieldValue.serverTimestamp(),
            "updatedAt" to FieldValue.serverTimestamp()
        )
        return try {
            val docRef = cardsRef.add(payload).await()
            Result.success(docRef.id)
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            handleFirestoreError(e, OperationType.CREATE, cardsRef.path)
            Result.failure(e)
        }
    }

    suspend fun toggleFlashcardMastered(card: CustomFlashcard): Result<Unit> {
        val uid = requireUserId()
        require(card.id.isNotBlank()) { "Card ID must not be blank" }
        requireNotNull(card.createdAt) { "Card createdAt must not be null" }
        val docRef = db.collection("users").document(uid).collection("flashcards").document(card.id)
        return try {
            docRef.set(card.copy(mastered = !card.mastered, updatedAt = null)).await()
            Result.success(Unit)
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            handleFirestoreError(e, OperationType.UPDATE, docRef.path)
            Result.failure(e)
        }
    }

    suspend fun deleteFlashcard(cardId: String): Result<Unit> {
        val uid = requireUserId()
        val docRef = db.collection("users").document(uid).collection("flashcards").document(cardId)
        return try {
            docRef.delete().await()
            Result.success(Unit)
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            handleFirestoreError(e, OperationType.DELETE, docRef.path)
            Result.failure(e)
        }
    }
}
