package com.csi.sistema_controle_pedidos.controller;

import com.csi.sistema_controle_pedidos.model.Mesa;
import com.csi.sistema_controle_pedidos.service.MesaService;
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
@RequestMapping("/mesa")
@Tag(name = "Mesas", description = "Path relacionado a operações de mesas")
public class MesaController {

    private final MesaService mesaService;

    public MesaController(MesaService mesaService) {
        this.mesaService = mesaService;
    }

    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Cadastrar uma nova mesa", description = "Cria uma nova mesa no sistema.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Mesa cadastrada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos fornecidos", content = @Content)
    })
    @PostMapping
    public void cadastrarMesa(@RequestBody Mesa mesa) {
        mesaService.cadastrarMesa(mesa);
    }

    @Operation(summary = "Atualizar uma mesa", description = "Atualiza os dados de uma mesa existente.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Mesa atualizada com sucesso"),
            @ApiResponse(responseCode = "404", description = "Mesa não encontrada", content = @Content),
            @ApiResponse(responseCode = "400", description = "Dados inválidos fornecidos", content = @Content)
    })
    @PutMapping
    public void atualizarMesa(@RequestBody Mesa mesa) {
        mesaService.atualizarMesa(mesa);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Excluir uma mesa", description = "Exclui uma mesa existente (requer o objeto Mesa com ID no corpo).")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Mesa excluída com sucesso", content = @Content),
            @ApiResponse(responseCode = "404", description = "Mesa não encontrada", content = @Content)
    })
    @DeleteMapping
    public void excluirMesa(@RequestBody Mesa mesa) {
        mesaService.removerMesa(mesa.getId());
    }

    @Operation(summary = "Buscar mesa por ID", description = "Retorna os dados de uma mesa específica.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Mesa encontrada com sucesso",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Mesa.class))),
            @ApiResponse(responseCode = "404", description = "Mesa não encontrada", content = @Content)
    })
    @GetMapping("/{id}")
    public Mesa buscarMesaPorId(@PathVariable(name = "id") Long id) {
        return mesaService.buscarMesaPorId(id);
    }

    @Operation(summary = "Listar todas as mesas", description = "Retorna uma lista de todas as mesas cadastradas.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de mesas retornada com sucesso",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Mesa.class)))
    })
    @GetMapping
    public List<Mesa> listarMesas() {
        return mesaService.listarMesas();
    }
}
