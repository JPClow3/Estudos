import model.Amigo;
import model.Emprestimo;
import model.Objeto;
import repository.AmigoRepository;
import repository.CsvDataException;
import repository.EmprestimoRepository;
import repository.ObjetoRepository;
import service.LoanService;
import service.ValidationUtils;
import view.MainFrame;
import view.StyleGuide;

import javax.swing.JLabel;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.table.JTableHeader;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class RegressionTests {
    private static int passed;
    private static int failed;

    private RegressionTests() {
    }

    public static void main(String[] args) throws Exception {
        run("existing files survive repository initialization", RegressionTests::existingFilesSurvive);
        run("missing files receive headers only", RegressionTests::missingFilesReceiveHeaders);
        run("wrong header rejects whole file", RegressionTests::wrongHeaderRejected);
        run("wrong column count rejects whole file", RegressionTests::wrongColumnCountRejected);
        run("duplicate and non-positive ids reject whole file", RegressionTests::invalidIdsRejected);
        run("malformed UTF-8 rejects whole file", RegressionTests::malformedUtf8Rejected);
        run("invalid booleans and values reject whole file", RegressionTests::invalidObjectValuesRejected);
        run("invalid dates and statuses reject whole file", RegressionTests::invalidLoanValuesRejected);
        run("failed reads preserve original bytes", RegressionTests::failedReadPreservesBytes);
        run("form validation rejects dangerous values", RegressionTests::formValidation);
        run("orphan loans reject complete dataset", RegressionTests::orphanLoansRejected);
        run("inconsistent active-loan datasets are rejected", RegressionTests::inconsistentActiveLoansRejected);
        run("active loan transitions synchronize availability", RegressionTests::loanTransitions);
        run("referenced records cannot be deleted", RegressionTests::referentialDeletion);
        run("Swing labels, headers, and minimum table height are usable", RegressionTests::swingAccessibility);

        System.out.printf("Regression tests: %d passed, %d failed%n", passed, failed);
        if (failed > 0) {
            throw new AssertionError(failed + " regression test(s) failed");
        }
    }

    private static void existingFilesSurvive() throws Exception {
        Path root = Files.createTempDirectory("existing-csv-");
        Path file = root.resolve("amigos.csv");
        byte[] sentinel = "id;nome;telefone;email\n1;Persisted;64999999999;p@example.com\n"
                .getBytes(StandardCharsets.UTF_8);
        Files.write(file, sentinel);
        new AmigoRepository(file.toString());
        assertArrayEquals(sentinel, Files.readAllBytes(file));
    }

    private static void missingFilesReceiveHeaders() throws Exception {
        Path root = Files.createTempDirectory("missing-csv-");
        Path amigos = root.resolve("amigos.csv");
        Path objetos = root.resolve("objetos.csv");
        Path emprestimos = root.resolve("emprestimos.csv");
        new AmigoRepository(amigos.toString());
        new ObjetoRepository(objetos.toString());
        new EmprestimoRepository(emprestimos.toString());
        assertEquals("id;nome;telefone;email\n", Files.readString(amigos));
        assertEquals("id;nome;categoria;descricao;disponivel;valor\n", Files.readString(objetos));
        assertEquals(
                "id;objetoId;amigoId;dataEmprestimo;dataPrevistaDevolucao;dataDevolucao;status;observacoes\n",
                Files.readString(emprestimos)
        );
    }

    private static void wrongHeaderRejected() throws Exception {
        Path file = writeTemp("amigos.csv", "wrong;header\n1;Name;123;x@y.com\n");
        expectThrows(CsvDataException.class, () -> new AmigoRepository(file.toString()).listar());

        Path whitespace = writeTemp("amigos.csv", " id;nome;telefone;email \n1;Name;123;x@y.com\n");
        expectThrows(CsvDataException.class, () -> new AmigoRepository(whitespace.toString()).listar());
    }

    private static void wrongColumnCountRejected() throws Exception {
        Path file = writeTemp("amigos.csv", "id;nome;telefone;email\n1;Name;123;x@y.com;extra\n");
        expectThrows(CsvDataException.class, () -> new AmigoRepository(file.toString()).listar());
    }

    private static void invalidIdsRejected() throws Exception {
        Path duplicate = writeTemp(
                "amigos.csv",
                "id;nome;telefone;email\n1;One;123;a@x.com\n1;Two;456;b@x.com\n"
        );
        expectThrows(CsvDataException.class, () -> new AmigoRepository(duplicate.toString()).listar());

        Path zero = writeTemp("amigos.csv", "id;nome;telefone;email\n0;Zero;123;a@x.com\n");
        expectThrows(CsvDataException.class, () -> new AmigoRepository(zero.toString()).listar());
    }

    private static void malformedUtf8Rejected() throws Exception {
        Path file = Files.createTempFile("malformed-", ".csv");
        Files.write(file, new byte[]{(byte) 0xC3, (byte) 0x28});
        expectThrows(CsvDataException.class, () -> new AmigoRepository(file.toString()).listar());
    }

    private static void invalidObjectValuesRejected() throws Exception {
        for (String row : List.of(
                "1;A;Cat;Desc;maybe;10",
                "1;A;Cat;Desc;true;NaN",
                "1;A;Cat;Desc;true;Infinity",
                "1;A;Cat;Desc;true;-1"
        )) {
            Path file = writeTemp(
                    "objetos.csv",
                    "id;nome;categoria;descricao;disponivel;valor\n" + row + "\n"
            );
            expectThrows(CsvDataException.class, () -> new ObjetoRepository(file.toString()).listar());
        }
    }

    private static void invalidLoanValuesRejected() throws Exception {
        for (String row : List.of(
                "1;1;1;31/02/2026;01/03/2026;;EMPRESTADO;x",
                "1;1;1;01/03/2026;02/03/2026;;UNKNOWN;x",
                "1;1;1;01/03/2026;02/03/2026;;DEVOLVIDO;x",
                "1;1;1;01/03/2026;02/03/2026;03/03/2026;EMPRESTADO;x",
                "1;1;1;01/03/2026;02/03/2026;;EMPRESTADO;x;extra",
                "1;1;1;01/03/2026"
        )) {
            Path file = writeTemp(
                    "emprestimos.csv",
                    "id;objetoId;amigoId;dataEmprestimo;dataPrevistaDevolucao;dataDevolucao;status;observacoes\n"
                            + row + "\n"
            );
            expectThrows(CsvDataException.class, () -> new EmprestimoRepository(file.toString()).listar());
        }
    }

    private static void failedReadPreservesBytes() throws Exception {
        Path file = writeTemp("amigos.csv", "bad\n1;Name;123;x@y.com\n");
        byte[] before = Files.readAllBytes(file);
        expectThrows(CsvDataException.class, () -> new AmigoRepository(file.toString()).listar());
        assertArrayEquals(before, Files.readAllBytes(file));
    }

    private static void formValidation() {
        expectThrows(IllegalArgumentException.class, () -> ValidationUtils.requireSafeText("Nome", "bad;value", true));
        expectThrows(IllegalArgumentException.class, () -> ValidationUtils.requireSafeText("Nome", "bad\nvalue", true));
        expectThrows(IllegalArgumentException.class, () -> ValidationUtils.validateContact("(__) _____-____", ""));
        expectThrows(IllegalArgumentException.class, () -> ValidationUtils.parseNonNegativeFinite("Valor", "NaN"));
        expectThrows(IllegalArgumentException.class, () -> ValidationUtils.parseNonNegativeFinite("Valor", "-1"));
        expectThrows(
                IllegalArgumentException.class,
                () -> ValidationUtils.validateLoanDates(LocalDate.of(2026, 6, 21), LocalDate.of(2026, 6, 20))
        );
    }

    private static void orphanLoansRejected() throws Exception {
        Fixture fixture = fixture();
        fixture.loanRepository.salvarTodos(List.of(new Emprestimo(
                1, 999, 1, LocalDate.now(), LocalDate.now().plusDays(1), null, "EMPRESTADO", ""
        )));
        expectThrows(CsvDataException.class, fixture.service::listarValidado);
    }

    private static void inconsistentActiveLoansRejected() throws Exception {
        Fixture fixture = fixture();
        fixture.loanRepository.salvarTodos(List.of(new Emprestimo(
                1, 1, 1, LocalDate.now(), LocalDate.now().plusDays(1), null, "EMPRESTADO", ""
        )));
        expectThrows(CsvDataException.class, fixture.service::listarValidado);

        fixture.objectRepository.salvarTodos(List.of(new Objeto(1, "Drill", "Tool", "", false, 100.0)));
        fixture.loanRepository.salvarTodos(List.of(
                new Emprestimo(1, 1, 1, LocalDate.now(), LocalDate.now().plusDays(1), null, "EMPRESTADO", ""),
                new Emprestimo(2, 1, 1, LocalDate.now(), LocalDate.now().plusDays(2), null, "ATRASADO", "")
        ));
        expectThrows(CsvDataException.class, fixture.service::listarValidado);
    }

    private static void loanTransitions() throws Exception {
        Fixture fixture = fixture();
        Emprestimo created = fixture.service.criar(
                1, 1, LocalDate.now(), LocalDate.now().plusDays(7), "EMPRESTADO", "ok"
        );
        assertFalse(fixture.objectRepository.listar().get(0).isDisponivel());
        expectThrows(IllegalArgumentException.class, () -> fixture.service.criar(
                1, 1, LocalDate.now(), LocalDate.now().plusDays(7), "EMPRESTADO", "duplicate"
        ));

        fixture.service.atualizar(
                created.getId(), 1, 1, created.getDataEmprestimo(), created.getDataPrevistaDevolucao(),
                "DEVOLVIDO", "returned"
        );
        assertTrue(fixture.objectRepository.listar().get(0).isDisponivel());

        fixture.service.atualizar(
                created.getId(), 1, 1, created.getDataEmprestimo(), created.getDataPrevistaDevolucao(),
                "EMPRESTADO", "active again"
        );
        assertFalse(fixture.objectRepository.listar().get(0).isDisponivel());

        fixture.service.excluir(created.getId());
        assertTrue(fixture.objectRepository.listar().get(0).isDisponivel());
        assertEquals(0, fixture.loanRepository.listar().size());
    }

    private static void referentialDeletion() throws Exception {
        Fixture fixture = fixture();
        fixture.service.criar(1, 1, LocalDate.now(), LocalDate.now().plusDays(7), "EMPRESTADO", "");
        assertTrue(fixture.service.objetoPossuiHistorico(1));
        assertTrue(fixture.service.amigoPossuiHistorico(1));
    }

    private static void swingAccessibility() throws Exception {
        Fixture fixture = fixture();
        MainFrame[] holder = new MainFrame[1];
        SwingUtilities.invokeAndWait(() -> {
            holder[0] = new MainFrame(
                    fixture.objectRepository,
                    fixture.friendRepository,
                    fixture.loanRepository
            );
            holder[0].setLocation(-10000, -10000);
            holder[0].setSize(800, 600);
            holder[0].setVisible(true);
            holder[0].validate();
        });

        MainFrame frame = holder[0];
        try {
            List<JLabel> labels = descendants(frame, JLabel.class);
            long formLabels = labels.stream().filter(label -> label.getText().endsWith(":")).count();
            long associated = labels.stream()
                    .filter(label -> label.getText().endsWith(":"))
                    .filter(label -> label.getLabelFor() != null)
                    .count();
            assertEquals(formLabels, associated);

            JTabbedPane tabs = descendants(frame, JTabbedPane.class).get(0);
            for (int index = 0; index < tabs.getTabCount(); index++) {
                int selectedIndex = index;
                SwingUtilities.invokeAndWait(() -> {
                    tabs.setSelectedIndex(selectedIndex);
                    frame.validate();
                });
                JTable table = descendants((Container) tabs.getComponentAt(index), JTable.class).get(0);
                JTableHeader header = table.getTableHeader();
                Component rendered = header.getDefaultRenderer().getTableCellRendererComponent(
                        table, "Header", false, false, -1, 0
                );
                assertEquals(StyleGuide.COR_PRIMARIA, rendered.getBackground());
                assertEquals(StyleGuide.BRANCO, rendered.getForeground());
                if (table.getParent().getHeight() < 70) {
                    throw new AssertionError(
                            "Tab " + tabs.getTitleAt(index) + " has table viewport height "
                                    + table.getParent().getHeight()
                    );
                }
            }
        } finally {
            SwingUtilities.invokeAndWait(frame::dispose);
        }
    }

    private static Fixture fixture() throws Exception {
        Path root = Files.createTempDirectory("loan-fixture-");
        ObjetoRepository objects = new ObjetoRepository(root.resolve("objetos.csv").toString());
        AmigoRepository friends = new AmigoRepository(root.resolve("amigos.csv").toString());
        EmprestimoRepository loans = new EmprestimoRepository(root.resolve("emprestimos.csv").toString());
        objects.salvarTodos(List.of(new Objeto(1, "Drill", "Tool", "", true, 100.0)));
        friends.salvarTodos(List.of(new Amigo(1, "Friend", "64999999999", "")));
        return new Fixture(objects, friends, loans, new LoanService(loans, objects, friends));
    }

    private static Path writeTemp(String name, String content) throws Exception {
        Path root = Files.createTempDirectory("csv-case-");
        Path file = root.resolve(name);
        Files.writeString(file, content, StandardCharsets.UTF_8);
        return file;
    }

    private static <T extends Component> List<T> descendants(Container root, Class<T> type) {
        List<T> matches = new ArrayList<>();
        List<Component> queue = new ArrayList<>(Arrays.asList(root.getComponents()));
        while (!queue.isEmpty()) {
            Component component = queue.remove(0);
            if (type.isInstance(component)) {
                matches.add(type.cast(component));
            }
            if (component instanceof Container container) {
                queue.addAll(Arrays.asList(container.getComponents()));
            }
        }
        return matches;
    }

    private static void run(String name, ThrowingRunnable test) {
        try {
            test.run();
            passed++;
            System.out.println("PASS " + name);
        } catch (Throwable throwable) {
            failed++;
            System.err.println("FAIL " + name + ": " + throwable);
            throwable.printStackTrace(System.err);
        }
    }

    private static void expectThrows(Class<? extends Throwable> type, ThrowingRunnable action) {
        try {
            action.run();
        } catch (Throwable throwable) {
            if (type.isInstance(throwable)) {
                return;
            }
            throw new AssertionError("Expected " + type.getSimpleName() + " but got " + throwable, throwable);
        }
        throw new AssertionError("Expected " + type.getSimpleName() + " to be thrown");
    }

    private static void assertTrue(boolean value) {
        if (!value) {
            throw new AssertionError("Expected true");
        }
    }

    private static void assertFalse(boolean value) {
        if (value) {
            throw new AssertionError("Expected false");
        }
    }

    private static void assertEquals(Object expected, Object actual) {
        if (!java.util.Objects.equals(expected, actual)) {
            throw new AssertionError("Expected <" + expected + "> but was <" + actual + ">");
        }
    }

    private static void assertArrayEquals(byte[] expected, byte[] actual) {
        if (!Arrays.equals(expected, actual)) {
            throw new AssertionError("Byte arrays differ");
        }
    }

    private record Fixture(
            ObjetoRepository objectRepository,
            AmigoRepository friendRepository,
            EmprestimoRepository loanRepository,
            LoanService service
    ) {
    }

    @FunctionalInterface
    private interface ThrowingRunnable {
        void run() throws Exception;
    }
}
