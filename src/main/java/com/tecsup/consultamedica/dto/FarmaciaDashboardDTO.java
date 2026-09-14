package com.tecsup.consultamedica.dto;

import com.tecsup.consultamedica.model.Lote;
import com.tecsup.consultamedica.model.Medicamento;

import java.util.List;
import java.util.Map;

public class FarmaciaDashboardDTO {

    private long totalMedicamentos;
    private int stockTotalDisponible;
    private int medicamentosStockBajo;
    private int lotesProximosVencer;
    private int recetasPendientes;
    private long dispensacionesDelDia;

    private List<Map<String, Object>> listaStockCritico;
    private List<Lote> listaProximosAVencer;

    public FarmaciaDashboardDTO() {}

    public long getTotalMedicamentos() { return totalMedicamentos; }
    public void setTotalMedicamentos(long totalMedicamentos) { this.totalMedicamentos = totalMedicamentos; }

    public int getStockTotalDisponible() { return stockTotalDisponible; }
    public void setStockTotalDisponible(int stockTotalDisponible) { this.stockTotalDisponible = stockTotalDisponible; }

    public int getMedicamentosStockBajo() { return medicamentosStockBajo; }
    public void setMedicamentosStockBajo(int medicamentosStockBajo) { this.medicamentosStockBajo = medicamentosStockBajo; }

    public int getLotesProximosVencer() { return lotesProximosVencer; }
    public void setLotesProximosVencer(int lotesProximosVencer) { this.lotesProximosVencer = lotesProximosVencer; }

    public int getRecetasPendientes() { return recetasPendientes; }
    public void setRecetasPendientes(int recetasPendientes) { this.recetasPendientes = recetasPendientes; }

    public long getDispensacionesDelDia() { return dispensacionesDelDia; }
    public void setDispensacionesDelDia(long dispensacionesDelDia) { this.dispensacionesDelDia = dispensacionesDelDia; }

    public List<Map<String, Object>> getListaStockCritico() { return listaStockCritico; }
    public void setListaStockCritico(List<Map<String, Object>> listaStockCritico) { this.listaStockCritico = listaStockCritico; }

    public List<Lote> getListaProximosAVencer() { return listaProximosAVencer; }
    public void setListaProximosAVencer(List<Lote> listaProximosAVencer) { this.listaProximosAVencer = listaProximosAVencer; }
}