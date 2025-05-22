package com.github.calculo.rescisao.repository;


import com.github.calculo.rescisao.model.RescisaoFuncionario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface RescisaoFuncionarioRepository extends JpaRepository<RescisaoFuncionario, Long> {

    @Query(value = "select r from RescisaoFuncionario r where r.funcionario.id = ?1")
    RescisaoFuncionario findByIdFuncionario(Long id);
}
