package br.com.app.service;

import java.util.List;

import br.com.app.model.StatusRegistro;

/**
 * Interface StatusRegistro
 * 
 * @author Danilo Humberto
 *
 */

public interface StatusRegistroService {

	public List<StatusRegistro> buscarTodos(String ... sorting);
	
	public StatusRegistro buscarPorId(Long id);

	public void salvar(StatusRegistro entity);
	
	public void salvarTodos(List<StatusRegistro> entities);

	public void excluir(StatusRegistro entity);
	
	public void excluirItens(List<StatusRegistro> entities);

	public void atualizar(StatusRegistro entity);

	public void excluirTodos();
}
