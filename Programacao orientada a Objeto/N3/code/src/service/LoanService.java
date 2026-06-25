package service;

import model.Amigo;
import model.Emprestimo;
import model.Objeto;
import repository.AmigoRepository;
import repository.CsvDataException;
import repository.CsvUtils;
import repository.EmprestimoRepository;
import repository.ObjetoRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class LoanService {
    private static final Set<String> STATUSES = Set.of("EMPRESTADO", "DEVOLVIDO", "ATRASADO");

    private final EmprestimoRepository loanRepository;
    private final ObjetoRepository objectRepository;
    private final AmigoRepository friendRepository;

    public LoanService(
            EmprestimoRepository loanRepository,
            ObjetoRepository objectRepository,
            AmigoRepository friendRepository
    ) {
        this.loanRepository = loanRepository;
        this.objectRepository = objectRepository;
        this.friendRepository = friendRepository;
    }

    public List<Emprestimo> listarValidado() {
        List<Emprestimo> loans = loanRepository.listar();
        List<Objeto> objects = objectRepository.listar();
        List<Amigo> friends = friendRepository.listar();
        Set<Integer> activeObjectIds = new HashSet<>();
        for (int index = 0; index < loans.size(); index++) {
            Emprestimo loan = loans.get(index);
            Objeto object = findObject(objects, loan.getObjetoId());
            if (object == null) {
                throw new CsvDataException(
                        loanRepository.getFilePath(), index + 2,
                        "objetoId " + loan.getObjetoId() + " não existe em objetos.csv"
                );
            }
            if (findFriend(friends, loan.getAmigoId()) == null) {
                throw new CsvDataException(
                        loanRepository.getFilePath(), index + 2,
                        "amigoId " + loan.getAmigoId() + " não existe em amigos.csv"
                );
            }
            ValidationUtils.validateLoanDates(loan.getDataEmprestimo(), loan.getDataPrevistaDevolucao());
            if (isActive(loan.getStatus())) {
                if (!activeObjectIds.add(loan.getObjetoId())) {
                    throw new CsvDataException(
                            loanRepository.getFilePath(), index + 2,
                            "mais de um empréstimo ativo referencia o objetoId " + loan.getObjetoId()
                    );
                }
                if (object.isDisponivel()) {
                    throw new CsvDataException(
                            loanRepository.getFilePath(), index + 2,
                            "objetoId " + loan.getObjetoId() + " está disponível apesar do empréstimo ativo"
                    );
                }
            }
        }
        return loans;
    }

    public Emprestimo criar(
            int objectId,
            int friendId,
            LocalDate loanDate,
            LocalDate dueDate,
            String status,
            String notes
    ) {
        List<Emprestimo> loans = new ArrayList<>(listarValidado());
        List<Objeto> objects = new ArrayList<>(objectRepository.listar());
        validateRequest(null, objectId, friendId, loanDate, dueDate, status, notes, loans, objects);

        LocalDate returnedAt = "DEVOLVIDO".equals(status) ? LocalDate.now() : null;
        Emprestimo loan = new Emprestimo(
                loanRepository.proximoId(), objectId, friendId, loanDate, dueDate, returnedAt, status,
                ValidationUtils.requireSafeText("Observações", notes, false)
        );
        loans.add(loan);
        if (isActive(status)) {
            setAvailability(objects, objectId, false);
        }
        saveBoth(loans, objects);
        return loan;
    }

    public void atualizar(
            int loanId,
            int objectId,
            int friendId,
            LocalDate loanDate,
            LocalDate dueDate,
            String status,
            String notes
    ) {
        List<Emprestimo> loans = new ArrayList<>(listarValidado());
        List<Objeto> objects = new ArrayList<>(objectRepository.listar());
        Emprestimo previous = loans.stream()
                .filter(loan -> loan.getId() == loanId)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Empréstimo não encontrado."));

        validateRequest(loanId, objectId, friendId, loanDate, dueDate, status, notes, loans, objects);
        boolean wasActive = isActive(previous.getStatus());
        boolean willBeActive = isActive(status);

        if (wasActive && (previous.getObjetoId() != objectId || !willBeActive)) {
            setAvailability(objects, previous.getObjetoId(), true);
        }
        if (willBeActive && (!wasActive || previous.getObjetoId() != objectId)) {
            setAvailability(objects, objectId, false);
        }

        LocalDate returnedAt = "DEVOLVIDO".equals(status)
                ? previous.getDataDevolucao() == null ? LocalDate.now() : previous.getDataDevolucao()
                : null;
        Emprestimo replacement = new Emprestimo(
                loanId, objectId, friendId, loanDate, dueDate, returnedAt, status,
                ValidationUtils.requireSafeText("Observações", notes, false)
        );
        for (int index = 0; index < loans.size(); index++) {
            if (loans.get(index).getId() == loanId) {
                loans.set(index, replacement);
                break;
            }
        }
        saveBoth(loans, objects);
    }

    public void excluir(int loanId) {
        List<Emprestimo> loans = new ArrayList<>(listarValidado());
        List<Objeto> objects = new ArrayList<>(objectRepository.listar());
        Emprestimo removed = loans.stream()
                .filter(loan -> loan.getId() == loanId)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Empréstimo não encontrado."));
        loans.removeIf(loan -> loan.getId() == loanId);
        if (isActive(removed.getStatus())) {
            setAvailability(objects, removed.getObjetoId(), true);
        }
        saveBoth(loans, objects);
    }

    public boolean objetoPossuiHistorico(int objectId) {
        return listarValidado().stream().anyMatch(loan -> loan.getObjetoId() == objectId);
    }

    public boolean amigoPossuiHistorico(int friendId) {
        return listarValidado().stream().anyMatch(loan -> loan.getAmigoId() == friendId);
    }

    public boolean objetoPossuiEmprestimoAtivo(int objectId) {
        return listarValidado().stream()
                .anyMatch(loan -> loan.getObjetoId() == objectId && isActive(loan.getStatus()));
    }

    private void validateRequest(
            Integer editingLoanId,
            int objectId,
            int friendId,
            LocalDate loanDate,
            LocalDate dueDate,
            String status,
            String notes,
            List<Emprestimo> loans,
            List<Objeto> objects
    ) {
        ValidationUtils.validateLoanDates(loanDate, dueDate);
        ValidationUtils.requireSafeText("Observações", notes, false);
        if (!STATUSES.contains(status)) {
            throw new IllegalArgumentException("Status inválido.");
        }
        Objeto object = findObject(objects, objectId);
        if (object == null) {
            throw new IllegalArgumentException("O objeto selecionado não existe.");
        }
        if (findFriend(friendRepository.listar(), friendId) == null) {
            throw new IllegalArgumentException("O amigo selecionado não existe.");
        }
        if (isActive(status)) {
            boolean activeDuplicate = loans.stream()
                    .anyMatch(loan -> (editingLoanId == null || loan.getId() != editingLoanId)
                            && loan.getObjetoId() == objectId
                            && isActive(loan.getStatus()));
            if (activeDuplicate) {
                throw new IllegalArgumentException("Este objeto já possui um empréstimo ativo.");
            }

            Emprestimo editing = editingLoanId == null ? null : loans.stream()
                    .filter(loan -> loan.getId() == editingLoanId)
                    .findFirst()
                    .orElse(null);
            boolean alreadyReservedByEditingLoan = editing != null
                    && isActive(editing.getStatus())
                    && editing.getObjetoId() == objectId;
            if (!object.isDisponivel() && !alreadyReservedByEditingLoan) {
                throw new IllegalArgumentException("O objeto selecionado não está disponível.");
            }
        }
    }

    private void saveBoth(List<Emprestimo> loans, List<Objeto> objects) {
        Map<java.nio.file.Path, String> contents = new LinkedHashMap<>();
        contents.put(objectRepository.getFilePath(), objectRepository.fileContent(objects));
        contents.put(loanRepository.getFilePath(), loanRepository.fileContent(loans));
        CsvUtils.writeTransaction(contents);
    }

    private static boolean isActive(String status) {
        return !"DEVOLVIDO".equals(status);
    }

    private static Objeto findObject(List<Objeto> objects, int id) {
        return objects.stream().filter(object -> object.getId() == id).findFirst().orElse(null);
    }

    private static Amigo findFriend(List<Amigo> friends, int id) {
        return friends.stream().filter(friend -> friend.getId() == id).findFirst().orElse(null);
    }

    private static void setAvailability(List<Objeto> objects, int id, boolean available) {
        Objeto object = findObject(objects, id);
        if (object == null) {
            throw new IllegalArgumentException("Objeto relacionado não encontrado.");
        }
        object.setDisponivel(available);
    }
}
