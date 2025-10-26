package com.csi.sistema_controle_pedidos.service;

import com.csi.sistema_controle_pedidos.model.Mesa;
import com.csi.sistema_controle_pedidos.repository.MesaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MesaService {

    private final MesaRepository mesaRepository;

    public MesaService(MesaRepository mesaRepository) {
        this.mesaRepository = mesaRepository;
    }

    public Mesa cadastrarMesa(Mesa mesa) {
        return mesaRepository.save(mesa);
    }

    public List<Mesa> listarMesas() {
        return mesaRepository.findAll();
    }

    public Mesa buscarMesaPorId(Long id) {
        return mesaRepository.findById(id).get();
    }

    public void atualizarMesa(Mesa mesa) {
        Mesa m = mesaRepository.findById(mesa.getId()).get();
        m.setDisponivel(mesa.getDisponivel());

        mesaRepository.save(mesa);
    }

    public void removerMesa(Long id) {
        mesaRepository.deleteById(id);
    }

}
