# EmprestaFacil Hardening Design

## Goal

Make the Swing application safe for real CSV-backed use by preventing startup data loss, rejecting corrupted files as a whole, enforcing loan consistency, and correcting the audited UI and accessibility defects.

## Scope

The change covers:

- startup and initial CSV creation;
- strict CSV parsing and validation;
- object, friend, and loan form validation;
- cross-file loan integrity and object availability;
- deletion constraints;
- table-header rendering and minimum-window layout;
- label associations and keyboard accessibility;
- dependency-free automated regression tests.

It does not add a database, external CSV library, build tool, authentication, or import/export wizard.

## Startup and Persistence

`Main` must never delete an existing CSV. At startup, each repository creates its file only when missing and writes only the expected header. Demo records must not be inserted automatically.

The application continues to use the `semana3/dados` directory relative to its launch directory.

## Strict CSV Contract

Each repository accepts only UTF-8 files matching its exact header and exact column count:

- `amigos.csv`: `id;nome;telefone;email`
- `objetos.csv`: `id;nome;categoria;descricao;disponivel;valor`
- `emprestimos.csv`: `id;objetoId;amigoId;dataEmprestimo;dataPrevistaDevolucao;dataDevolucao;status;observacoes`

The complete file is rejected when any row is invalid. No partial list is returned, no invalid row is silently skipped, and the source file is never rewritten during a failed read.

Strict validation includes:

- exact header after allowing a UTF-8 BOM only at the start;
- exact field count;
- one physical line per record;
- positive, unique integer IDs;
- strict `true` or `false` boolean values;
- finite, non-negative object values;
- dates in strict `dd/MM/yyyy` format;
- loan status limited to `EMPRESTADO`, `DEVOLVIDO`, or `ATRASADO`;
- non-empty required persisted fields;
- no semicolon, carriage return, or newline inside a persisted field.

CSV errors are represented by a dedicated runtime exception carrying the filename, row number when applicable, and a user-readable reason. Swing actions catch this error and show it to the user instead of printing to stderr.

## Form Validation

Forms reject invalid input before any repository write:

- Text fields reject semicolons and line breaks with an explicit message.
- Objects require name and category.
- Object value must be a finite number greater than or equal to zero.
- Friends require a name and at least one real contact method: a completely filled phone number or a non-empty email.
- Placeholder-only masked phone text is treated as blank.
- Loans require an existing object and friend, valid dates, and a due date on or after the loan date.
- A new active loan cannot use an unavailable object or an object already present in another active loan.
- Loan status values come from the fixed supported set.

Validation failures leave the form contents intact and do not modify any CSV.

## Loan and Availability Consistency

Loan operations and object availability must remain synchronized:

- creating an active loan marks its object unavailable;
- changing an active loan to `DEVOLVIDO` marks its object available;
- changing a returned loan back to active is allowed only if the object is available, then marks it unavailable;
- changing the object on an active loan releases the previous object and reserves the new object;
- deleting an active loan releases its object;
- editing or deleting a returned loan does not reserve or release an object unnecessarily.

Before displaying or changing loans, the application validates that every loan references an existing object and friend. An orphan reference rejects the complete loan dataset with a visible error.

Writes involving both loans and objects use a safe two-file update strategy: validate all resulting data first, write temporary files, then replace the target files. If preparation fails, neither target is changed. If replacement fails, restore the original bytes and report the error.

## Referential Deletion Rules

Deleting an object is blocked when any loan references its ID.

Deleting a friend is blocked when any loan references its ID.

The message identifies that the record cannot be deleted because loan history depends on it. Historical returned loans also count as references.

## UI and Accessibility

- Install the system look and feel before constructing Swing components.
- Use an explicit header renderer so table-header foreground and background colors remain readable under Windows look and feel.
- Give tables a usable minimum viewport height. At the supported 800 x 600 minimum window size, visible records must remain present in all three tabs.
- Associate every visible form label with its input through `setLabelFor`.
- Keep existing colors and button semantics.
- Preserve the current three-tab information architecture.

## Testing

Add a dependency-free Java test runner under `code/test` using assertions and explicit failure reporting.

Regression coverage must include:

- existing files survive startup initialization;
- missing files receive only their headers;
- bad header, wrong field count, duplicate ID, malformed UTF-8, invalid date, invalid boolean, non-finite/negative value, invalid status, and multiline data reject the whole file;
- failed reads preserve file bytes;
- form validators reject forbidden characters, placeholder phone values, invalid values, and reversed dates;
- orphan loans are rejected;
- unavailable and duplicate active loans are rejected;
- create, edit, return, and delete loan transitions update availability;
- referenced objects and friends cannot be deleted;
- all form labels have `labelFor`;
- table headers retain readable colors;
- each table has usable height at 800 x 600;
- a large valid CSV can load without data loss, while the UI reports work instead of silently freezing.

## Acceptance Criteria

The work is complete when:

1. All regression tests pass.
2. `javac -Xlint:all` compiles production and test sources without errors.
3. Startup persistence verification preserves pre-existing sentinel data.
4. Adversarial CSV fixtures are rejected atomically with clear messages.
5. Rendered screenshots at 900 x 700 and 800 x 600 show readable table headers and visible records.
6. The working tree contains no generated class files, temporary CSV fixtures, or screenshots.
