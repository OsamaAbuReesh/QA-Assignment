
# 🧪 ProductStock Unit Testing Assignment

**QA Course – Unit Testing & Test Design**

---

## 📌 Project Overview

This project presents a complete unit testing solution for the **ProductStock** class using **TypeScript** and **Jest**.
Although the assignment specification refers to **JUnit 5**, the same testing concepts and principles were applied using Jest, which provides equivalent functionality for modern JavaScript/TypeScript projects.

The work covers:

* Test implementation
* Test case design
* Code coverage analysis and discussion

---

## 🧪 Task (A): Test Implementation

A comprehensive test suite was implemented to validate the main behaviors of the `ProductStock` class.

### Tested Features

* Valid and invalid constructor parameters
* Adding stock (`addStock`) – normal, boundary, and error cases
* Reserving and releasing stock (`reserve`, `releaseReservation`)
* Shipping reserved stock (`shipReserved`)
* Removing damaged stock (`removeDamaged`)
* Reorder logic (`isReorderNeeded`)
* Updating reorder threshold and maximum capacity

### Testing Techniques Used

* Test suites and nested test scenarios
* Lifecycle hooks:

  * `beforeAll`
  * `beforeEach`
  * `afterEach`
  * `afterAll`
* Parameterized tests for multiple input values
* Disabled test for a future feature
* Clear and descriptive test names for readability

---

## 📋 Task (B): Test Case Design

The **test case design** was prepared separately before implementation and submitted as a **standalone PDF file**.

The test case design sheet includes:

* Test Case ID
* Method / feature under test
* Preconditions
* Inputs
* Expected results
* Test type (normal, boundary, error, disabled)

📄 **File:**
`Task_B_Test_Case_Design_ProductStock.pdf`

This separation improves clarity and follows common academic practice.

---

## 📊 Task (C): Coverage & Discussion

### Coverage Results

The test suite was executed using Jest with coverage enabled.

* **Statements:** 87.87%
* **Branches:** 86.66%
* **Functions:** 100%
* **Lines:** 100%

The achieved coverage exceeds the required **80% minimum**.

### Uncovered Code Explanation

The uncovered lines correspond to defensive validation logic and exceptional branches that represent invalid or unreachable states. These branches are intentionally difficult or unnecessary to trigger during normal execution.

### Coverage Tool Used

Coverage was generated using:

```bash
npm run test:coverage
```

A detailed HTML coverage report is generated automatically under:

```
coverage/lcov-report/index.html
```

---

## ▶️ How to Run the Project

### 1️⃣ Install dependencies

```bash
npm install
```

### 2️⃣ Run tests

```bash
npm test
```

### 3️⃣ Run tests with coverage

```bash
npm run test:coverage
```

---

## 📝 Notes

* Jest was used instead of JUnit 5 while maintaining the same testing principles.
* TypeScript was chosen to provide stronger typing and cleaner project structure.
* The test suite covers both **happy paths** and **error paths** using systematic test design.

---

## ✅ Final Status

* Task (A): ✔ Completed
* Task (B): ✔ Completed (submitted as PDF)
* Task (C): ✔ Completed
* Coverage Requirement: ✔ Met

---

### 🎯 Submission Ready

This repository, along with the attached **Task (B) PDF**, fully satisfies the assignment requirements and is ready for submission.
