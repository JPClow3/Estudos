package service;

import model.Cliente;
import repository.ClienteRepository;

import java.util.List;
import java.util.Optional;

public class ClienteService {
    private final ClienteRepository repository;

    public ClienteService(ClienteRepository repository) {
        this.repository = repository;
    }

    public List<Cliente> listar() {
        return repository.listar();
    }

    public void criar(String nome, String email) {
        validar(nome, email);
        List<Cliente> clientes = repository.listar();
        clientes.add(new Cliente(repository.proximoId(), nome.trim(), email.trim()));
        repository.salvarTodos(clientes);
    }

    public void atualizar(int id, String nome, String email) {
        validar(nome, email);
        List<Cliente> clientes = repository.listar();
        Optional<Cliente> found = clientes.stream().filter(c -> c.getId() == id).findFirst();
        if (found.isEmpty()) {
            throw new ValidationException("Cliente nao encontrado.");
        }
        Cliente cliente = found.get();
        cliente.setNome(nome.trim());
        cliente.setEmail(email.trim());
        repository.salvarTodos(clientes);
    }

    public void remover(int id) {
        List<Cliente> clientes = repository.listar();
        boolean removed = clientes.removeIf(c -> c.getId() == id);
        if (!removed) {
            throw new ValidationException("Cliente nao encontrado.");
        }
        repository.salvarTodos(clientes);
    }

    private void validar(String nome, String email) {
        if (nome == null || nome.trim().isEmpty()) {
            throw new ValidationException("Nome do cliente e obrigatorio.");
        }
        if (email == null || email.trim().isEmpty() || !email.contains("@")) {
            throw new ValidationException("Email invalido.");
        }
    }
}

