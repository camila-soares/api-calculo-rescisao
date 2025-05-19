package com.github.calculo.rescisao.repository;


import com.github.calculo.rescisao.model.RescisaoFuncionario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RescisaoFuncionarioRepository extends JpaRepository<RescisaoFuncionario, Long> {
}
