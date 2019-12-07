package br.com.app.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import br.com.app.model.Client;
import br.com.app.repository.ClientRepository;
import javassist.NotFoundException;

@Service
public class ClientServiceImpl implements ClientService {
	
	@Autowired
	private ClientRepository clientRepository;
	
	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	public List<Client> buscarTodos(String ... sorting) {
		Sort sort = null;
		if (sorting != null && sorting.length > 0) {
			sort = new Sort(Sort.Direction.ASC, sorting);
			return clientRepository.findAll(sort);
		}
		
		return clientRepository.findAll();
	}
	
	/**
	 * Método buscar usuário por id
	 * @throws NotFoundException 
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	public Client buscarPorId(Long id) {
		Optional<Client> ret = clientRepository.findById(id);
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
	public void salvar(Client entity) {
		clientRepository.save(entity);
	}
	
	/**
	 * Método que salva
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void salvarTodos(List<Client> entities) {
		clientRepository.saveAll(entities);
	}

	/**
	 * Método que exclui
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void excluir(Client entity) {
		clientRepository.delete(entity);
	}
	
	/**
	 * Método que exclui listagem
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void excluirItens(List<Client> entities) {
		clientRepository.deleteAll(entities);
	}

	/**
	 * Método que atualiza
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void atualizar(Client entity) {
		salvar(entity);
	}

	/**
	 * Método que exclui todos usuários cadastrados.
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void excluirTodos() {
		clientRepository.deleteAll();
	}

}
