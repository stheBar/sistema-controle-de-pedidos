package com.csi.sistema_controle_pedidos.controller;

import com.csi.sistema_controle_pedidos.dto.PedidoResponseDTO;
import com.csi.sistema_controle_pedidos.mapper.PedidoMapper;
import com.csi.sistema_controle_pedidos.model.Pedido;
import com.csi.sistema_controle_pedidos.model.PedidoStatus;
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
    @GetMapping("/listar-por-conta")
    @Operation(summary = "Lista todos os pedidos de uma conta")
    public List<PedidoResponseDTO> listarPorConta(@RequestParam Long idConta) {

        return pedidoService.listarPorConta(idConta)
                .stream()
                .map(PedidoMapper::toDto)
                .toList();
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

    @Operation(summary = "Atualizar status de um pedido", description = "Altera o status de um pedido existente (ex: PENDENTE para EM_PREPARO).")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Status do pedido atualizado com sucesso",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Pedido.class))),
            @ApiResponse(responseCode = "404", description = "Pedido não encontrado", content = @Content),
            @ApiResponse(responseCode = "400", description = "Status inválido fornecido", content = @Content)
    })
    @PatchMapping("/{idPedido}/status")
    public PedidoResponseDTO atualizarStatusPedido(
            @PathVariable Long idPedido,
            @RequestParam(name = "status") PedidoStatus novoStatus) {

        Pedido pedido = pedidoService.atualizarStatus(idPedido, novoStatus);
        return PedidoMapper.toDto(pedido);
    }
}
