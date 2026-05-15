package service;

import model.Categoria;
import model.Produto;
import repository.CategoriaRepository;
import repository.ProdutoRepository;

import java.util.List;
import java.util.Optional;

public class ProdutoService {
    private final ProdutoRepository produtoRepository;
    private final CategoriaRepository categoriaRepository;

    public ProdutoService(ProdutoRepository produtoRepository, CategoriaRepository categoriaRepository) {
        this.produtoRepository = produtoRepository;
        this.categoriaRepository = categoriaRepository;
    }

    public List<Produto> listar() {
        return produtoRepository.listar();
    }

    public List<Categoria> listarCategorias() {
        return categoriaRepository.listar();
    }

    public void criar(String nome, double preco, int idCategoria) {
        validar(nome, preco, idCategoria);
        List<Produto> produtos = produtoRepository.listar();
        produtos.add(new Produto(produtoRepository.proximoId(), nome.trim(), preco, idCategoria));
        produtoRepository.salvarTodos(produtos);
    }

    public void atualizar(int id, String nome, double preco, int idCategoria) {
        validar(nome, preco, idCategoria);
        List<Produto> produtos = produtoRepository.listar();
        Optional<Produto> found = produtos.stream().filter(p -> p.getId() == id).findFirst();
        if (found.isEmpty()) {
            throw new ValidationException("Produto nao encontrado.");
        }
        Produto produto = found.get();
        produto.setNome(nome.trim());
        produto.setPreco(preco);
        produto.setIdCategoria(idCategoria);
        produtoRepository.salvarTodos(produtos);
    }

    public void remover(int id) {
        List<Produto> produtos = produtoRepository.listar();
        boolean removed = produtos.removeIf(p -> p.getId() == id);
        if (!removed) {
            throw new ValidationException("Produto nao encontrado.");
        }
        produtoRepository.salvarTodos(produtos);
    }

    private void validar(String nome, double preco, int idCategoria) {
        if (nome == null || nome.trim().isEmpty()) {
            throw new ValidationException("Nome do produto e obrigatorio.");
        }
        if (preco <= 0) {
            throw new ValidationException("Preco deve ser maior que zero.");
        }
        boolean categoriaExiste = categoriaRepository.listar().stream().anyMatch(c -> c.getId() == idCategoria);
        if (!categoriaExiste) {
            throw new ValidationException("Categoria invalida.");
        }
    }
}

