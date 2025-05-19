package com.github.calculo.rescisao;

import com.github.calculo.rescisao.dto.RescisaoDTO;
import com.github.calculo.rescisao.enums.TipoRescisao;
import com.github.calculo.rescisao.model.Funcionario;
import com.github.calculo.rescisao.service.RescisaoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class RescisaoServiceTest {

    @Autowired
    private RescisaoService service;

    @Test
    void calcularRescisao_SemJustaCausa_ComFeriasVencidas() {
        Funcionario f = new Funcionario();
        f.setNome("Teste");
        f.setSalarioMensal(new BigDecimal("3000"));
        f.setDataAdmissao(LocalDate.of(2023, 1, 1));
        f.setDataDemissao(LocalDate.of(2023, 6, 15));
        f.setTipoRescisao(TipoRescisao.SEM_JUSTA_CAUSA);
        f.setTemFeriasVencidas(true);

        RescisaoDTO dto = service.calcularRescisao(f);

        assertNotNull(dto.getTotal());
        assertTrue(dto.getTotal().compareTo(BigDecimal.ZERO) > 0);
    }

    @Test
    void calcularRescisao_JustaCausa_SemFeriasVencidas() {
        Funcionario f = new Funcionario();
        f.setNome("Teste2");
        f.setSalarioMensal(new BigDecimal("2500"));
        f.setDataAdmissao(LocalDate.of(2022, 3, 1));
        f.setDataDemissao(LocalDate.of(2023, 3, 1));
        f.setTipoRescisao(TipoRescisao.JUSTA_CAUSA);
        f.setTemFeriasVencidas(false);

        RescisaoDTO dto = service.calcularRescisao(f);

        assertNotNull(dto);
        assertTrue(dto.getTotal().compareTo(BigDecimal.ZERO) > 0);
        assertEquals(BigDecimal.ZERO, dto.getAvisoPrevio());
    }
}
