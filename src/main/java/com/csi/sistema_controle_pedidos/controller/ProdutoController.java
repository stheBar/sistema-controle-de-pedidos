package com.csi.sistema_controle_pedidos.controller;

import com.csi.sistema_controle_pedidos.dto.ProdutoDTO;
import com.csi.sistema_controle_pedidos.mapper.ProdutoMapper;
import com.csi.sistema_controle_pedidos.model.Produto;
import com.csi.sistema_controle_pedidos.service.ProdutoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/produto")
@Tag(name = "Produtos", description = "Operações para gerenciamento do cardápio de produtos")
public class ProdutoController {

    private final ProdutoService produtoService;

    public ProdutoController(ProdutoService produtoService) {
        this.produtoService = produtoService;
    }

    @Operation(summary = "Listar todos os produtos", description = "Retorna uma lista de todos os produtos cadastrados no cardápio.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de produtos retornada com sucesso",
                    content = @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = ProdutoDTO.class))))
    })
    @GetMapping
    public List<ProdutoDTO> listar() {
        return produtoService.listarProdutos();
    }

    @Operation(summary = "Cadastrar um novo produto", description = "Cria um novo produto no cardápio.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Produto cadastrado com sucesso",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ProdutoDTO.class))),
            @ApiResponse(responseCode = "400", description = "Dados inválidos fornecidos", content = @Content)
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProdutoDTO cadastrarProduto(@RequestBody @Valid ProdutoDTO dto) {
        Produto novoProduto = ProdutoMapper.fromDto(dto);
        return produtoService.cadastrarProduto(novoProduto);
    }

    @Operation(summary = "Buscar produto por ID", description = "Retorna os dados de um produto específico.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Produto encontrado com sucesso",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ProdutoDTO.class))),
            @ApiResponse(responseCode = "404", description = "Produto não encontrado", content = @Content)
    })
    @GetMapping("/{id}")
    public ProdutoDTO buscarProdutoPorId(@PathVariable Long id) {
        return produtoService.buscarProdutoPorId(id);
    }

    @Operation(summary = "Atualizar um produto", description = "Atualiza os dados de um produto existente (requer o ID no corpo).")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Produto atualizado com sucesso",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ProdutoDTO.class))),
            @ApiResponse(responseCode = "404", description = "Produto não encontrado", content = @Content),
            @ApiResponse(responseCode = "400", description = "Dados inválidos fornecidos", content = @Content)
    })
    @PutMapping
    public ProdutoDTO atualizarProduto(@RequestBody @Valid ProdutoDTO dto) {
        Produto produto = ProdutoMapper.fromDto(dto);
        return produtoService.atualizarProduto(produto);
    }

    @Operation(summary = "Excluir um produto", description = "Exclui um produto do cardápio pelo seu ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Produto excluído com sucesso", content = @Content),
            @ApiResponse(responseCode = "404", description = "Produto não encontrado", content = @Content)
    })
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void excluirProduto(@PathVariable Long id) {
        produtoService.deletarProdutoPorId(id);
    }
}
