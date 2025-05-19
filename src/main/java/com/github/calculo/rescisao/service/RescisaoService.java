package com.github.calculo.rescisao.service;


import com.github.calculo.rescisao.dto.RescisaoDTO;
import com.github.calculo.rescisao.enums.TipoRescisao;
import com.github.calculo.rescisao.model.Funcionario;
import com.github.calculo.rescisao.repository.FuncionarioRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Service
public class RescisaoService {

    private final FuncionarioRepository funcionarioRepository;

    public RescisaoService(FuncionarioRepository funcionarioRepository) {
        this.funcionarioRepository = funcionarioRepository;
    }

    public RescisaoDTO calcularRescisao(Funcionario funcionario) {
        funcionarioRepository.save(funcionario);
        LocalDate admissao = funcionario.getDataAdmissao();
        LocalDate demissao = funcionario.getDataDemissao();
        BigDecimal salario = funcionario.getSalarioMensal();
        TipoRescisao tipo = funcionario.getTipoRescisao();

        BigDecimal saldoSalario = calcularSaldoSalario(salario, demissao);
        BigDecimal decimoTerceiro = BigDecimal.ZERO;
        BigDecimal feriasProporcionais = BigDecimal.ZERO;
        BigDecimal umTercoFeriasProporcionais = BigDecimal.ZERO;
        BigDecimal feriasVencidas = BigDecimal.ZERO;
        BigDecimal avisoPrevio = BigDecimal.ZERO;
        BigDecimal multaFgts = BigDecimal.ZERO;

        // Comum a todos: férias vencidas
        if (funcionario.isTemFeriasVencidas()) {
            feriasVencidas = salario.add(salario.divide(BigDecimal.valueOf(3), 2, RoundingMode.HALF_UP));
        }

        switch (tipo) {
            case SEM_JUSTA_CAUSA:
                decimoTerceiro = calcular13Proporcional(salario, demissao);
                feriasProporcionais = calcularFeriasProporcionais(salario, admissao, demissao);
                umTercoFeriasProporcionais = feriasProporcionais.divide(BigDecimal.valueOf(3), 2, RoundingMode.HALF_UP);
                avisoPrevio = salario;
                multaFgts = calcularMultaFgts(salario, admissao, demissao);
                break;

            case JUSTA_CAUSA:
                // apenas saldo salário e férias vencidas
                break;

            case PEDIDO_DEMISSAO:
                decimoTerceiro = calcular13Proporcional(salario, demissao);
                feriasProporcionais = calcularFeriasProporcionais(salario, admissao, demissao);
                umTercoFeriasProporcionais = feriasProporcionais.divide(BigDecimal.valueOf(3), 2, RoundingMode.HALF_UP);
                avisoPrevio = salario.negate(); // desconto do aviso prévio não trabalhado
                break;
        }

        BigDecimal total = saldoSalario
                .add(decimoTerceiro)
                .add(feriasProporcionais).add(umTercoFeriasProporcionais)
                .add(feriasVencidas)
                .add(avisoPrevio)
                .add(multaFgts);

        RescisaoDTO dto = new RescisaoDTO();
        dto.setSaldoSalario(saldoSalario);
        dto.setFeriasProporcionais(feriasProporcionais.add(umTercoFeriasProporcionais));
        dto.setFeriasVencidas(feriasVencidas);
        dto.setDecimoTerceiro(decimoTerceiro);
        dto.setAvisoPrevio(avisoPrevio);
        dto.setMultaFgts(multaFgts);
        dto.setTotal(total);

        return dto;
    }

    private BigDecimal calcularSaldoSalario(BigDecimal salario, LocalDate demissao) {
        int diasTrabalhados = demissao.getDayOfMonth();
        int diasNoMes = demissao.lengthOfMonth();
        return salario.multiply(BigDecimal.valueOf(diasTrabalhados))
                .divide(BigDecimal.valueOf(diasNoMes), 2, RoundingMode.HALF_UP);
    }

    private BigDecimal calcular13Proporcional(BigDecimal salario, LocalDate demissao) {
        int meses = demissao.getMonthValue();
        return salario.multiply(BigDecimal.valueOf(meses))
                .divide(BigDecimal.valueOf(12), 2, RoundingMode.HALF_UP);
    }

    private BigDecimal calcularFeriasProporcionais(BigDecimal salario, LocalDate admissao, LocalDate demissao) {
        long meses = ChronoUnit.MONTHS.between(admissao, demissao) % 12;
        return salario.multiply(BigDecimal.valueOf(meses))
                .divide(BigDecimal.valueOf(12), 2, RoundingMode.HALF_UP);
    }

    private BigDecimal calcularMultaFgts(BigDecimal salario, LocalDate admissao, LocalDate demissao) {
        long meses = ChronoUnit.MONTHS.between(admissao, demissao);
        BigDecimal fgts = salario.multiply(BigDecimal.valueOf(0.08)).multiply(BigDecimal.valueOf(meses));
        return fgts.multiply(BigDecimal.valueOf(0.40)).setScale(2, RoundingMode.HALF_UP);
    }
}
