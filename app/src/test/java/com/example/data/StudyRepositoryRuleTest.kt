package com.example.data

import com.example.base.FirestoreEmulatorTestBase
import com.google.firebase.firestore.FirebaseFirestoreException
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withTimeout
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class StudyRepositoryRuleTest : FirestoreEmulatorTestBase() {

    @Test
    fun createAndObserveNote_authenticatedOwner_succeeds() = runBlocking {
        val aliceUid = signInTestUser(ALICE_EMAIL)
        val repository = StudyRepository(firestore)

        val createResult = withTimeout(DEFAULT_TIMEOUT_MS) {
            repository.createNote(
                title = "Accounting Equation Summary",
                content = "Assets = Liabilities + Owner's Equity",
                subjectCode = "ACC_1",
                part = "PART_1",
                isImportant = true
            )
        }
        assertTrue(createResult.isSuccess)
        val noteId = createResult.getOrThrow()

        val notes = withTimeout(FLOW_TIMEOUT_MS) {
            repository.observeNotes(aliceUid).first { list -> list.any { it.id == noteId } }
        }
        assertTrue(notes.any { it.id == noteId && it.title == "Accounting Equation Summary" })
    }

    @Test
    fun getNoteById_crossUserAccess_failsWithPermissionDenied() = runBlocking {
        val aliceUid = signInTestUser(ALICE_EMAIL)
        val repository = StudyRepository(firestore)

        val noteId = withTimeout(DEFAULT_TIMEOUT_MS) {
            repository.createNote(
                title = "Alice's Private Note",
                content = "Secret revision notes",
                subjectCode = "ECO_1",
                part = "PART_1",
                isImportant = false
            ).getOrThrow()
        }

        signInTestUser(BOB_EMAIL)
        val crossUserResult = withTimeout(DEFAULT_TIMEOUT_MS) {
            repository.getNoteById(aliceUid, noteId)
        }
        assertTrue(crossUserResult.isFailure)
        val firestoreEx = crossUserResult.exceptionOrNull()?.unwrapFirestoreException()
        assertNotNull(firestoreEx)
        assertEquals(FirebaseFirestoreException.Code.PERMISSION_DENIED, firestoreEx?.code)
    }

    @Test
    fun observeNotes_unauthenticatedUser_failsWithPermissionDenied() = runBlocking {
        auth.signOut()
        val repository = StudyRepository(firestore)

        var caughtException: Throwable? = null
        try {
            withTimeout(FLOW_TIMEOUT_MS) {
                repository.observeNotes("some_user_uid").first()
            }
        } catch (e: Throwable) {
            caughtException = e
        }
        assertNotNull(caughtException)
        val firestoreException = caughtException?.unwrapFirestoreException()
        assertNotNull("Expected nested FirebaseFirestoreException, got $caughtException", firestoreException)
        assertEquals(FirebaseFirestoreException.Code.PERMISSION_DENIED, firestoreException?.code)
    }

    @Test
    fun createTask_andRecordQuizAttempt_succeedsForOwner() = runBlocking {
        val aliceUid = signInTestUser(ALICE_EMAIL)
        val repository = StudyRepository(firestore)

        val taskResult = withTimeout(DEFAULT_TIMEOUT_MS) {
            repository.createTask(
                title = "Practice Bank Reconciliation Statement",
                subjectCode = "ACC_1",
                part = "PART_1",
                priority = "HIGH",
                targetDate = "Today"
            )
        }
        assertTrue(taskResult.isSuccess)
        val taskId = taskResult.getOrThrow()

        val tasks = withTimeout(FLOW_TIMEOUT_MS) {
            repository.observeTasks(aliceUid).first { list -> list.any { it.id == taskId } }
        }
        val createdTask = tasks.first { it.id == taskId }
        val toggleResult = withTimeout(DEFAULT_TIMEOUT_MS) {
            repository.toggleTaskCompleted(createdTask)
        }
        assertTrue(toggleResult.isSuccess)

        val quizResult = withTimeout(DEFAULT_TIMEOUT_MS) {
            repository.recordQuizAttempt(
                subjectCode = "ACC_1",
                subjectTitle = "Principles of Accounting",
                part = "PART_1",
                score = 8,
                totalQuestions = 10
            )
        }
        assertTrue(quizResult.isSuccess)
    }

    private companion object {
        const val ALICE_EMAIL = "alice_icom@test.com"
        const val BOB_EMAIL = "bob_icom@test.com"
        const val DEFAULT_TIMEOUT_MS = 5000L
        const val FLOW_TIMEOUT_MS = 3000L
    }
}
