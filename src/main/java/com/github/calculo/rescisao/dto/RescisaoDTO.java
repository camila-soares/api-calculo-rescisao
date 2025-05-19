package com.github.calculo.rescisao.dto;


import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class RescisaoDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private BigDecimal saldoSalario;
    private BigDecimal feriasVencidas;
    private BigDecimal feriasProporcionais;
    private BigDecimal decimoTerceiro;
    private BigDecimal avisoPrevio;
    private BigDecimal multaFgts;
    private BigDecimal total;

    public BigDecimal getAvisoPrevio() {
        return avisoPrevio;
    }

    public void setAvisoPrevio(BigDecimal avisoPrevio) {
        this.avisoPrevio = avisoPrevio;
    }

    public BigDecimal getSaldoSalario() {
        return saldoSalario;
    }

    public void setSaldoSalario(BigDecimal saldoSalario) {
        this.saldoSalario = saldoSalario;
    }

    public BigDecimal getFeriasVencidas() {
        return feriasVencidas;
    }

    public void setFeriasVencidas(BigDecimal feriasVencidas) {
        this.feriasVencidas = feriasVencidas;
    }

    public BigDecimal getFeriasProporcionais() {
        return feriasProporcionais;
    }

    public void setFeriasProporcionais(BigDecimal feriasProporcionais) {
        this.feriasProporcionais = feriasProporcionais;
    }

    public BigDecimal getDecimoTerceiro() {
        return decimoTerceiro;
    }

    public void setDecimoTerceiro(BigDecimal decimoTerceiro) {
        this.decimoTerceiro = decimoTerceiro;
    }

    public BigDecimal getMultaFgts() {
        return multaFgts;
    }

    public void setMultaFgts(BigDecimal multaFgts) {
        this.multaFgts = multaFgts;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }
}
