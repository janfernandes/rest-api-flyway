package br.com.app.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import br.com.app.model.StatusRegistro;
import br.com.app.repository.StatusRegistroRepository;
import javassist.NotFoundException;

/**
 * Implementacao da classe StatusRegistroService
 * 
 * @author Danilo Humberto
 *
 */
@Service
public class StatusRegistroServiceImpl implements StatusRegistroService {
	
	@Autowired
	private StatusRegistroRepository statusRegistroRepository;
	
	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	public List<StatusRegistro> buscarTodos(String ... sorting) {
		Sort sort = null;
		if (sorting != null && sorting.length > 0) {
			sort = new Sort(Sort.Direction.ASC, sorting);
			return statusRegistroRepository.findAll(sort);
		}
		
		return statusRegistroRepository.findAll();
	}
	
	/**
	 * Método buscar usuário por id
	 * @throws NotFoundException 
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	public StatusRegistro buscarPorId(Long id) {
		Optional<StatusRegistro> ret = statusRegistroRepository.findById(id);
		if (!ret.isPresent()) {
            return null;
        }
        return ret.get();
	}

	/**
	 * Método que salva
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void salvar(StatusRegistro entity) {
		statusRegistroRepository.save(entity);
	}
	
	/**
	 * Método que salva
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void salvarTodos(List<StatusRegistro> entities) {
		statusRegistroRepository.saveAll(entities);
	}

	/**
	 * Método que exclui
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void excluir(StatusRegistro entity) {
		statusRegistroRepository.delete(entity);
	}
	
	/**
	 * Método que exclui listagem
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void excluirItens(List<StatusRegistro> entities) {
		statusRegistroRepository.deleteAll(entities);
	}

	/**
	 * Método que atualiza
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void atualizar(StatusRegistro entity) {
		salvar(entity);
	}

	/**
	 * Método que exclui todos usuários cadastrados.
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void excluirTodos() {
		statusRegistroRepository.deleteAll();
	}
}
