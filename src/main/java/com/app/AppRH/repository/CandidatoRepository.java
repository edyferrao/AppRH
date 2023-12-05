package com.app.AppRH.repository;

import com.app.AppRH.models.Candidato;
import com.app.AppRH.models.Vaga;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CandidatoRepository extends JpaRepository<Candidato, Long> {

    Iterable<Candidato> findByVaga(Vaga vaga);
    Candidato findByRg(String rg);
    Candidato findById(long id);
    List<Candidato> findByNomeCandidato(String nomeCandidato);
}
