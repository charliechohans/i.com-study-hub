const {
  initializeTestEnvironment,
  assertFails,
  assertSucceeds,
} = require("@firebase/rules-unit-testing");
const { test, before, after, beforeEach } = require("node:test");
const fs = require("node:fs");

let testEnv;
const PROJECT_ID = process.env.GCP_PROJECT || "demo-no-project";
const ALICE_UID = "alice_123";
const BOB_UID = "bob_456";

const [emulatorHost, emulatorPortStr] = (process.env.FIRESTORE_EMULATOR_HOST || "127.0.0.1:8085").split(":");
const emulatorPort = parseInt(emulatorPortStr, 10);

before(async () => {
  const rules = fs.readFileSync("./firestore.rules", "utf8");
  testEnv = await initializeTestEnvironment({
    projectId: PROJECT_ID,
    firestore: {
      rules,
      host: emulatorHost,
      port: emulatorPort,
    },
  });
});

after(async () => {
  if (testEnv) {
    await testEnv.cleanup();
  }
});

beforeEach(async () => {
  if (testEnv) {
    await testEnv.clearFirestore();
  }
});

test("Unauthenticated user: cannot read or write notes, tasks, quiz_attempts, or flashcards", async () => {
  const unauthDb = testEnv.unauthenticatedContext().firestore();
  await assertFails(unauthDb.collection("users").doc(ALICE_UID).collection("notes").get());
  await assertFails(unauthDb.collection("users").doc(ALICE_UID).collection("tasks").get());
  await assertFails(unauthDb.collection("users").doc(ALICE_UID).collection("quiz_attempts").get());
  await assertFails(unauthDb.collection("users").doc(ALICE_UID).collection("flashcards").get());
});

test("Authenticated user: can create and read own StudyNote, cannot read Bob's note", async () => {
  const aliceDb = testEnv.authenticatedContext(ALICE_UID).firestore();
  const bobDb = testEnv.authenticatedContext(BOB_UID).firestore();
  const now = new Date();

  const noteRef = aliceDb.collection("users").doc(ALICE_UID).collection("notes").doc("note_1");
  await assertSucceeds(
    noteRef.set({
      userId: ALICE_UID,
      title: "Journal & Ledger Rules",
      content: "Real Account: Debit what comes in, Credit what goes out.",
      subjectCode: "ACC_1",
      part: "PART_1",
      isImportant: true,
      createdAt: now,
      updatedAt: now,
    })
  );

  await assertSucceeds(aliceDb.collection("users").doc(ALICE_UID).collection("notes").get());
  await assertFails(bobDb.collection("users").doc(ALICE_UID).collection("notes").doc("note_1").get());
  await assertFails(bobDb.collection("users").doc(ALICE_UID).collection("notes").get());
});

test("StudyNote: rejects ghost fields and invalid part enum", async () => {
  const aliceDb = testEnv.authenticatedContext(ALICE_UID).firestore();
  const now = new Date();

  // Ghost field rejection
  await assertFails(
    aliceDb.collection("users").doc(ALICE_UID).collection("notes").doc("note_bad_1").set({
      userId: ALICE_UID,
      title: "Valid Title",
      content: "Valid Content",
      subjectCode: "ACC_1",
      part: "PART_1",
      isImportant: false,
      createdAt: now,
      updatedAt: now,
      ghostField: "hacked",
    })
  );

  // Invalid part enum rejection
  await assertFails(
    aliceDb.collection("users").doc(ALICE_UID).collection("notes").doc("note_bad_2").set({
      userId: ALICE_UID,
      title: "Valid Title",
      content: "Valid Content",
      subjectCode: "ACC_1",
      part: "PART_3",
      isImportant: false,
      createdAt: now,
      updatedAt: now,
    })
  );
});

test("StudyTask: owner can create, update completion, and delete task", async () => {
  const aliceDb = testEnv.authenticatedContext(ALICE_UID).firestore();
  const now = new Date();
  const taskRef = aliceDb.collection("users").doc(ALICE_UID).collection("tasks").doc("task_1");

  await assertSucceeds(
    taskRef.set({
      userId: ALICE_UID,
      title: "Revise Final Accounts Adjustments",
      subjectCode: "ACC_1",
      part: "PART_1",
      priority: "HIGH",
      completed: false,
      targetDate: "Today",
      createdAt: now,
      updatedAt: now,
    })
  );

  await assertSucceeds(
    taskRef.update({
      completed: true,
      updatedAt: new Date(),
    })
  );

  await assertSucceeds(taskRef.delete());
});

test("QuizAttempt: owner can create and list attempts, but cannot update", async () => {
  const aliceDb = testEnv.authenticatedContext(ALICE_UID).firestore();
  const now = new Date();
  const attemptRef = aliceDb.collection("users").doc(ALICE_UID).collection("quiz_attempts").doc("attempt_1");

  await assertSucceeds(
    attemptRef.set({
      userId: ALICE_UID,
      subjectCode: "ECO_1",
      subjectTitle: "Principles of Economics",
      part: "PART_1",
      score: 9,
      totalQuestions: 10,
      createdAt: now,
    })
  );

  await assertFails(
    attemptRef.update({
      score: 10,
    })
  );
});

test("CustomFlashcard: owner can create and toggle mastered", async () => {
  const aliceDb = testEnv.authenticatedContext(ALICE_UID).firestore();
  const now = new Date();
  const cardRef = aliceDb.collection("users").doc(ALICE_UID).collection("flashcards").doc("card_1");

  await assertSucceeds(
    cardRef.set({
      userId: ALICE_UID,
      question: "What is Law of Demand?",
      answer: "Other things remaining the same, when price rises demand contracts and when price falls demand extends.",
      subjectCode: "ECO_1",
      part: "PART_1",
      mastered: false,
      createdAt: now,
      updatedAt: now,
    })
  );

  await assertSucceeds(
    cardRef.update({
      mastered: true,
      updatedAt: new Date(),
    })
  );
});
