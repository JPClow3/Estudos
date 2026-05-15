package service;

import model.Categoria;
import repository.CategoriaRepository;
import repository.ProdutoRepository;

import java.util.List;
import java.util.Optional;

public class CategoriaService {
    private final CategoriaRepository categoriaRepository;
    private final ProdutoRepository produtoRepository;

    public CategoriaService(CategoriaRepository categoriaRepository, ProdutoRepository produtoRepository) {
        this.categoriaRepository = categoriaRepository;
        this.produtoRepository = produtoRepository;
    }

    public List<Categoria> listar() {
        return categoriaRepository.listar();
    }

    public void criar(String nome) {
        validar(nome);
        List<Categoria> categorias = categoriaRepository.listar();
        categorias.add(new Categoria(categoriaRepository.proximoId(), nome.trim()));
        categoriaRepository.salvarTodos(categorias);
    }

    public void atualizar(int id, String nome) {
        validar(nome);
        List<Categoria> categorias = categoriaRepository.listar();
        Optional<Categoria> found = categorias.stream().filter(c -> c.getId() == id).findFirst();
        if (found.isEmpty()) {
            throw new ValidationException("Categoria nao encontrada.");
        }
        found.get().setNome(nome.trim());
        categoriaRepository.salvarTodos(categorias);
    }

    public void remover(int id) {
        boolean emUso = produtoRepository.listar().stream().anyMatch(p -> p.getIdCategoria() == id);
        if (emUso) {
            throw new ValidationException("Nao e possivel remover categoria com produtos vinculados.");
        }
        List<Categoria> categorias = categoriaRepository.listar();
        boolean removed = categorias.removeIf(c -> c.getId() == id);
        if (!removed) {
            throw new ValidationException("Categoria nao encontrada.");
        }
        categoriaRepository.salvarTodos(categorias);
    }

    private void validar(String nome) {
        if (nome == null || nome.trim().isEmpty()) {
            throw new ValidationException("Nome da categoria e obrigatorio.");
        }
    }
}

