package com.app.AppRH.repository;

import com.app.AppRH.models.Vaga;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VagaRepository extends JpaRepository<Vaga, Long> {

    Vaga findByCodigo(long codigo);
    List<Vaga> findByNome(String nome);
}
