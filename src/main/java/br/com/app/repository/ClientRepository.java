package br.com.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.app.model.Client;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {
	Client findByNome(String nome);
}
