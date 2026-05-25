package com.nicolasperez.restorantespringboot.services;

import com.nicolasperez.restorantespringboot.dto.IngredienteDetalleDTO;
import com.nicolasperez.restorantespringboot.entities.Despensa;
import com.nicolasperez.restorantespringboot.entities.Ingrediente;
import com.nicolasperez.restorantespringboot.repositories.DespensaRepository;
import com.nicolasperez.restorantespringboot.repositories.IngredienteRepository;

import jakarta.transaction.Transactional;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class DespensaService {

    private final DespensaRepository despensaRepository;
    private final IngredienteRepository ingredienteRepository;

    public DespensaService(
            DespensaRepository despensaRepository,
            IngredienteRepository ingredienteRepository
    ) {
        this.despensaRepository = despensaRepository;
        this.ingredienteRepository = ingredienteRepository;
    }

    // ============================================================
    // DESPENSA
    // ============================================================

    public Despensa obtenerDespensaPorGerente(
            Integer gerenteId
    ) {
        return despensaRepository
                .buscarPorGerente(gerenteId)
                .orElse(null);
    }

    public List<IngredienteDetalleDTO>
    obtenerIngredientesDespensa(Integer despensaId) {

        return despensaRepository
                .obtenerIngredientesInfo(despensaId);
    }

    public List<Ingrediente> obtenerIngredientesConStockBajo(
            Integer despensaId,
            Integer umbral
    ) {

        return despensaRepository
                .obtenerIngredientesConStockBajo(
                        despensaId,
                        umbral
                );
    }

    public boolean agregarIngredienteADespensa(
            Integer despensaId,
            Integer ingredienteId
    ) {

        Optional<Despensa> despensaOpt =
                despensaRepository.findById(despensaId);

        Optional<Ingrediente> ingredienteOpt =
                ingredienteRepository.findById(ingredienteId);

        if (despensaOpt.isEmpty()
                || ingredienteOpt.isEmpty()) {
            return false;
        }

        Despensa despensa = despensaOpt.get();

        despensa.getIngredientes()
                .add(ingredienteOpt.get());

        despensaRepository.save(despensa);

        return true;
    }

    public boolean removerIngredienteDeDespensa(
            Integer despensaId,
            Integer ingredienteId
    ) {

        Optional<Despensa> despensaOpt =
                despensaRepository.findById(despensaId);

        Optional<Ingrediente> ingredienteOpt =
                ingredienteRepository.findById(ingredienteId);

        if (despensaOpt.isEmpty()
                || ingredienteOpt.isEmpty()) {
            return false;
        }

        Despensa despensa = despensaOpt.get();

        despensa.getIngredientes()
                .remove(ingredienteOpt.get());

        despensaRepository.save(despensa);

        return true;
    }

    // ============================================================
    // ESTADISTICAS
    // ============================================================

    public DespensaEstadisticas obtenerEstadisticasDespensa(
            Integer despensaId
    ) {

        List<IngredienteDetalleDTO>
                ingredientes =
                obtenerIngredientesDespensa(despensaId);

        int totalIngredientes = ingredientes.size();

        int conStock = 0;
        int stockBajo = 0;
        int sinStock = 0;

        for (IngredienteDetalleDTO dto
                : ingredientes) {

            if (dto.getCantidadStock() == 0) {

                sinStock++;

            } else if (dto.getCantidadStock() < 10) {

                stockBajo++;
                conStock++;

            } else {

                conStock++;
            }
        }

        return new DespensaEstadisticas(
                totalIngredientes,
                conStock,
                stockBajo,
                sinStock
        );
    }

    public Despensa obtenerDespensa(Integer id) {
        return despensaRepository.findById(id)
                .orElse(null);
    }

    // ============================================================
    // DTO ESTADISTICAS
    // ============================================================

    public static class DespensaEstadisticas {

        private final int totalIngredientes;
        private final int conStock;
        private final int stockBajo;
        private final int sinStock;

        public DespensaEstadisticas(
                int totalIngredientes,
                int conStock,
                int stockBajo,
                int sinStock
        ) {
            this.totalIngredientes = totalIngredientes;
            this.conStock = conStock;
            this.stockBajo = stockBajo;
            this.sinStock = sinStock;
        }

        public int getTotalIngredientes() {
            return totalIngredientes;
        }

        public int getConStock() {
            return conStock;
        }

        public int getStockBajo() {
            return stockBajo;
        }

        public int getSinStock() {
            return sinStock;
        }
    }
}