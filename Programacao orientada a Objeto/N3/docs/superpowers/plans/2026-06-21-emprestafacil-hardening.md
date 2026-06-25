# EmprestaFacil Hardening Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [x]`) syntax for tracking.

**Goal:** Eliminate destructive startup behavior, reject invalid CSVs atomically, enforce loan consistency, and fix the audited Swing usability defects.

**Architecture:** Keep the dependency-free Java/Swing structure. Centralize strict CSV mechanics in `CsvUtils`, field rules in `ValidationUtils`, and cross-repository loan transitions in `LoanService`; panels remain responsible for rendering and user messages. Add a plain Java regression runner under `code/test`.

**Tech Stack:** Java 25, Swing, `java.nio.file`, plain Java assertions/custom test helpers.

---

### Task 1: Add regression harness and strict CSV contract

**Files:**
- Create: `code/test/RegressionTests.java`
- Create: `code/src/repository/CsvDataException.java`
- Modify: `code/src/repository/CsvUtils.java`
- Modify: `code/src/repository/AmigoRepository.java`
- Modify: `code/src/repository/ObjetoRepository.java`
- Modify: `code/src/repository/EmprestimoRepository.java`

- [x] Write tests for exact headers, exact columns, UTF-8 failures, duplicate/positive IDs, strict booleans, finite non-negative values, strict dates/statuses, all-or-nothing reads, and byte preservation.
- [x] Compile and run tests; verify the new tests fail against current behavior.
- [x] Implement strict CSV parsing and repository validation with user-readable `CsvDataException`.
- [x] Run the regression suite and verify the CSV tests pass.

### Task 2: Protect startup data

**Files:**
- Modify: `code/src/Main.java`
- Test: `code/test/RegressionTests.java`

- [x] Add tests proving existing files remain unchanged and missing files receive headers only.
- [x] Run tests and verify startup-initialization tests fail.
- [x] Remove demo deletion/insertion and expose a testable repository initialization method.
- [x] Run tests and verify startup tests pass.

### Task 3: Centralize validation and loan transitions

**Files:**
- Create: `code/src/service/ValidationUtils.java`
- Create: `code/src/service/LoanService.java`
- Modify: `code/src/view/ObjetosPanel.java`
- Modify: `code/src/view/AmigosPanel.java`
- Modify: `code/src/view/EmprestimosPanel.java`
- Modify: `code/src/view/MainFrame.java`
- Test: `code/test/RegressionTests.java`

- [x] Add tests for forbidden characters, placeholder contacts, non-finite/negative values, reversed dates, orphan loans, unavailable/duplicate active loans, availability transitions, rollback, and referential deletion checks.
- [x] Run tests and verify they fail because the service/validation API is absent.
- [x] Implement validators and `LoanService` with rollback-safe two-repository updates.
- [x] Route panel save/edit/delete operations through the service and show CSV/validation errors.
- [x] Run tests and verify business-rule tests pass.

### Task 4: Fix Swing rendering and accessibility

**Files:**
- Create: `code/src/view/ModernTableHeaderRenderer.java`
- Modify: `code/src/view/MainFrame.java`
- Modify: `code/src/view/ObjetosPanel.java`
- Modify: `code/src/view/AmigosPanel.java`
- Modify: `code/src/view/EmprestimosPanel.java`
- Test: `code/test/RegressionTests.java`

- [x] Add component tests for header colors, `labelFor`, and visible table height at 800 x 600.
- [x] Run tests and verify the UI tests fail.
- [x] Install look and feel before component construction, add the header renderer, associate labels, and rebalance minimum-size layouts.
- [x] Run tests and verify UI tests pass.

### Task 5: Full verification

**Files:**
- Verify all modified production and test files.

- [x] Compile production and tests with `javac -Xlint:all`.
- [x] Run the complete regression suite.
- [x] Repeat startup sentinel verification.
- [x] Repeat malformed/adversarial and 250,000-row CSV stress tests.
- [x] Render all tabs at 900 x 700 and 800 x 600 and inspect screenshots.
- [x] Run `git diff --check` and confirm no generated artifacts are tracked.

