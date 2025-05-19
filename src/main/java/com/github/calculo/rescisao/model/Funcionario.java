package com.github.calculo.rescisao.model;


import com.github.calculo.rescisao.enums.TipoRescisao;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Entity
@Table(name = "funcionario")
@Getter
public class Funcionario implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nome;
    private BigDecimal salarioMensal;
    private LocalDate dataAdmissao;
    private LocalDate dataDemissao;

    @Enumerated(EnumType.STRING)
    private TipoRescisao tipoRescisao;

    private boolean temFeriasVencidas;

    @OneToOne(mappedBy = "funcionario", cascade = CascadeType.ALL)
    private RescisaoFuncionario rescisaoFuncionario;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TipoRescisao getTipoRescisao() {
        return tipoRescisao;
    }

    public void setTipoRescisao(TipoRescisao tipoRescisao) {
        this.tipoRescisao = tipoRescisao;
    }

    public boolean isTemFeriasVencidas() {
        return temFeriasVencidas;
    }

    public void setTemFeriasVencidas(boolean temFeriasVencidas) {
        this.temFeriasVencidas = temFeriasVencidas;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public BigDecimal getSalarioMensal() {
        return salarioMensal;
    }

    public void setSalarioMensal(BigDecimal salarioMensal) {
        this.salarioMensal = salarioMensal;
    }

    public LocalDate getDataAdmissao() {
        return dataAdmissao;
    }

    public void setDataAdmissao(LocalDate dataAdmissao) {
        this.dataAdmissao = dataAdmissao;
    }

    public LocalDate getDataDemissao() {
        return dataDemissao;
    }

    public void setDataDemissao(LocalDate dataDemissao) {
        this.dataDemissao = dataDemissao;
    }
}
