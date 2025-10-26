package com.csi.sistema_controle_pedidos.controller;

import com.csi.sistema_controle_pedidos.model.Pedido;
import com.csi.sistema_controle_pedidos.service.PedidoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/pedido")
@Tag(name = "Pedidos", description = "Operações relacionadas a pedidos individuais")
public class PedidoController {

    private final PedidoService pedidoService;

    public PedidoController(PedidoService pedidoService) {
        this.pedidoService = pedidoService;
    }

    @Operation(summary = "Listar pedidos de uma mesa", description = "Retorna uma lista de todos os pedidos associados à conta aberta de uma mesa específica.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pedidos encontrados com sucesso",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Pedido.class))),
            @ApiResponse(responseCode = "404", description = "Mesa não encontrada ou sem conta aberta", content = @Content)
    })
    @GetMapping
    public List<Pedido> listarPedidosDeUmaMesa(@RequestParam(name = "idMesa") long idMesa) {
        return pedidoService.listarPedidosDeUmaMesa(idMesa);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Criar um novo pedido", description = "Cria um novo pedido (geralmente associado a uma conta).")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Pedido criado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos fornecidos (ex: conta fechada, produto indisponível)", content = @Content),
            @ApiResponse(responseCode = "404", description = "Conta, Usuário ou Produto não encontrado", content = @Content)
    })
    @PostMapping
    public void criarPedido(@RequestBody Pedido pedido) {
        pedidoService.criarPedido(pedido);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Deletar um pedido", description = "Exclui um pedido existente (requer o objeto Pedido com ID no corpo).")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Pedido deletado com sucesso", content = @Content),
            @ApiResponse(responseCode = "404", description = "Pedido não encontrado", content = @Content)
    })
    @DeleteMapping
    public void deletarPedido(@RequestBody Pedido pedido) {
        pedidoService.deletarPedido(pedido);
    }
}
