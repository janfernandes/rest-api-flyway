package br.com.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.app.model.StatusRegistro;

/**
 * Interface da classe StatusProjeto Repository
 * 
 * @author Danilo Humberto
 *
 */
@Repository
public interface StatusRegistroRepository extends JpaRepository<StatusRegistro, Long> {
	
	StatusRegistro findByDescricao(String descricao);
}
