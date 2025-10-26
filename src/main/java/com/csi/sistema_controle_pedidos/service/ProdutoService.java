package com.csi.sistema_controle_pedidos.service;

import com.csi.sistema_controle_pedidos.dto.ProdutoDTO;
import com.csi.sistema_controle_pedidos.mapper.ProdutoMapper;
import com.csi.sistema_controle_pedidos.model.Produto;
import com.csi.sistema_controle_pedidos.repository.ProdutoRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class ProdutoService {

    private final ProdutoRepository produtoRepository;

    public ProdutoService(ProdutoRepository produtoRepository) {
        this.produtoRepository = produtoRepository;
    }

    public List<ProdutoDTO> listarProdutos() {
        return produtoRepository.findAll().stream()
                .map(ProdutoMapper::toDto)
                .toList();
    }

    public ProdutoDTO cadastrarProduto(Produto produto) {
        Produto salvo = produtoRepository.save(produto);
        return ProdutoMapper.toDto(salvo);
    }

    public ProdutoDTO buscarProdutoPorId(Long id) {
        Produto produto = produtoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Produto não encontrado"));
        return ProdutoMapper.toDto(produto);
    }

    public ProdutoDTO atualizarProduto(Produto produto) {
        Produto existente = produtoRepository.findById(produto.getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Produto não encontrado"));

        existente.setNome(produto.getNome());
        existente.setDescricao(produto.getDescricao());
        existente.setPreco(produto.getPreco());
        existente.setDisponivel(produto.getDisponivel());
        existente.setProdutoCategoria(produto.getProdutoCategoria());

        Produto atualizado = produtoRepository.save(existente);
        return ProdutoMapper.toDto(atualizado);
    }

    public void deletarProdutoPorId(Long id) {
        if (!produtoRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Produto não encontrado");
        }
        produtoRepository.deleteById(id);
    }
}
