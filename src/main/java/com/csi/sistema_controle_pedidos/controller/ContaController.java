package com.csi.sistema_controle_pedidos.controller;

import com.csi.sistema_controle_pedidos.dto.*;
import com.csi.sistema_controle_pedidos.mapper.ContaMapper;
import com.csi.sistema_controle_pedidos.model.FormaPagamento;

import com.csi.sistema_controle_pedidos.infra.security.AppUserDetails;
import com.csi.sistema_controle_pedidos.service.ContaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.HttpStatus;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/conta")
@Tag(name = "Contas", description = "Operações relacionadas ao gerenciamento de contas")
public class ContaController {

    private final ContaService contaService;

    public ContaController(ContaService contaService) {
        this.contaService = contaService;
    }

    @Operation(summary = "Mostrar detalhes de uma conta", description = "Busca e retorna os detalhes de uma conta específica pelo seu ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Conta encontrada com sucesso",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ContaResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Conta não encontrada",
                    content = @Content)
    })
    @GetMapping("/{id}")
    public ContaResponseDTO mostrarConta(@PathVariable(name = "id") long id) {
        return ContaMapper.toDto(contaService.mostrarConta(id));
    }

    @Operation(summary = "Criar uma nova conta", description = "Cria uma nova conta com base nos dados fornecidos.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Conta criada com sucesso",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ContaResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Dados inválidos fornecidos",
                    content = @Content)
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ContaResponseDTO criarConta(@RequestBody @Valid ContaRequestDTO body) {
        return ContaMapper.toDto(contaService.criarConta(ContaMapper.fromDto(body)));
    }

    @Operation(summary = "Deletar uma conta", description = "Exclui uma conta existente pelo seu ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Conta deletada com sucesso",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Conta não encontrada",
                    content = @Content)
    })
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletarConta(@PathVariable(name = "id") long id) {
        contaService.deletarConta(id);
    }

    @Operation(summary = "Pedir o fechamento da conta", description = "Solicita o fechamento da conta (pedir a conta), retornando o valor total e os itens.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Conta solicitada com sucesso (status PEDIDA)",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ContaDetalhadaDTO.class))),
            @ApiResponse(responseCode = "404", description = "Conta não encontrada",
                    content = @Content)
    })
    @PutMapping("/{id}/pedir")
    public ContaDetalhadaDTO pedirConta(@PathVariable(name = "id") long id) {
        return contaService.pedirConta(id);
    }

    @Operation(summary = "Pagar uma conta", description = "Registra o pagamento de uma conta, alterando seu status para FECHADA.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pagamento registrado com sucesso",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ContaResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Conta não está em status 'PEDIDA' ou forma de pagamento inválida",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Conta não encontrada",
                    content = @Content)
    })
    @PutMapping("/{id}/pagar")
    public ContaResponseDTO pagarConta(@PathVariable(name = "id") long id,
                                       @RequestBody PagamentoRequest body) {
        return ContaMapper.toDto(contaService.pagarConta(id, body.formaPagamento()));
    }

    @Operation(summary = "Adicionar um pedido a uma conta", description = "Cria um novo pedido e o associa a uma conta existente. O usuário (garçom) é pego automaticamente do token.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Pedido criado e adicionado à conta com sucesso",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = PedidoResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Dados do pedido inválidos ou conta já fechada",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Conta não encontrada",
                    content = @Content)
    })
    @PostMapping("/{id}/pedido")
    @ResponseStatus(HttpStatus.CREATED)
    public PedidoResponseDTO criarPedidoNaConta(
            @PathVariable(name = "id") long contaId,
            @RequestBody @Valid CriarPedidoRequest body,
            @AuthenticationPrincipal AppUserDetails userDetails
    ) {
        return contaService.criarPedidoNaConta(contaId, body, userDetails.getDomain());
    }

    public record PagamentoRequest(@NotNull FormaPagamento formaPagamento) {}
}