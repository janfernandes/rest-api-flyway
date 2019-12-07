package br.com.app.service;

import java.util.List;

/**
 * Interface StatusRegistro
 * 
 * @author Danilo Humberto
 *
 */

import br.com.app.model.Client;

public interface ClientService {
	public List<Client> buscarTodos(String ... sorting);
	
	public Client buscarPorId(Long id);

	public void salvar(Client entity);
	
	public void salvarTodos(List<Client> entities);

	public void excluir(Client entity);
	
	public void excluirItens(List<Client> entities);

	public void atualizar(Client entity);

	public void excluirTodos();
}
