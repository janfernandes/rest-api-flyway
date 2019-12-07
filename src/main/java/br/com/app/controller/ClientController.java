package br.com.app.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import br.com.app.model.Client;
import br.com.app.service.ClientService;
import br.com.app.util.ErroGenerico;
import lombok.Getter;

@RestController
@RequestMapping("/cliente")
public class ClientController {

	/**
	 * logger
	 */
	public static final Logger LOGGER = LoggerFactory.getLogger(ClientController.class);

	/**
	 * Injeção de dependencia.
	 */
	@Autowired
	@Getter
	private ClientService client;
	public ClientService getService() {
		return client;
	}

	/**
	 * Serviço que busca todos os registros da entidade
	 * 
	 * @return
	 */
	@GetMapping
	public ResponseEntity buscarTodos(@RequestParam(value = "sorting", required = false) String ... sortings) {

		try {
			LOGGER.info("Classe: {}, buscando todos registros.", getClass().getName());
			List<Client> listaRetorno = null;
			if (sortings != null && sortings.length > 0) {
				listaRetorno = client.buscarTodos(sortings);
			} else {
				listaRetorno = client.buscarTodos();
			}

			if (ObjectUtils.isEmpty(listaRetorno)) {
				LOGGER.info("Nenhum registro encontrado");
				return new ResponseEntity(new ErroGenerico("Nenhum registro encontrado"), HttpStatus.NO_CONTENT);
			}

			return ResponseEntity.ok(listaRetorno);
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "buscarTodos", e);
		}
	}

	/**
	 * Serviço que busca por id um registro da entidade.
	 * 
	 * @param id
	 * @return
	 */
	@GetMapping("/id/{id}")
	public ResponseEntity buscarPorId(@PathVariable("id") Long id) {
		try {
			LOGGER.info("Classe: {}, buscando com o id {}", getClass().getName(), id);
			Client entity = client.buscarPorId(id);
			if (entity == null) {
				LOGGER.error("Id {} não encontrado.", id);
				return new ResponseEntity(new ErroGenerico("Id " + id + " não encontrado"), HttpStatus.NOT_FOUND);
			}
			return ResponseEntity.ok(entity);
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "buscarPorId", e);
		}
	}

	/**
	 * Serviço que cria um objeto do tipo da entidade.
	 * 
	 * @param entity
	 * @param ucBuilder
	 * @return
	 */
	@PostMapping
	public ResponseEntity criar(@RequestBody Client entity) {
		try {
			LOGGER.info("Classe: {}, criando registro: {}", getClass().getName(), entity);
			client.salvar(entity);
			
			return ResponseEntity.ok(entity);
			
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "criar", e);
		}
	}

	/**
	 * Serviço que atualiza um registro da entidade.
	 * 
	 * @param usuario
	 * @return
	 */
	@PutMapping("/{id}")
	public ResponseEntity atualizar(@RequestBody Client entity, @PathVariable Long id) {
		LOGGER.info("Classe: {}, atualizar registro c/ id {}", getClass().getName(), id);
		try {
			Client returnEntity = client.buscarPorId(id);

			if (returnEntity == null) {
				LOGGER.error("Não foi possível atualizar. O id {} não foi encontrado.", id);
				return new ResponseEntity(
						new ErroGenerico("Não foi possível atualizar. O id " + id + " não foi encontrado."),
						HttpStatus.NOT_FOUND);
			}
			returnEntity.setNome(entity.getNome());
			returnEntity.setTelefone(entity.getTelefone());
			returnEntity.setEndereco(entity.getEndereco());

			client.atualizar(returnEntity);

			return ResponseEntity.ok(returnEntity);
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "atualizar", e);
		}
	}

	/**
	 * Serviço que remove um registro da entidade a partir do ID.
	 * 
	 * @param id
	 * @return
	 */
	@DeleteMapping("/id/{id}")
	public ResponseEntity deletar(@PathVariable("id") Long id) {
		try {
			LOGGER.info("Classe: {}, removendo registro c/ id {}", getClass().getName(), id);

			Client entity = client.buscarPorId(id);

			if (entity == null) {
				LOGGER.error("Não foi possível remover. O id {} não foi encontrado.", id);
				return new ResponseEntity(
						new ErroGenerico("Não foi possível remover. O id " + id + " não foi encontrado."),
						HttpStatus.NOT_FOUND);
			}

			client.excluir(entity);

			return ResponseEntity.ok(entity);
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "deletar", e);
		}
	}

	/**
	 * Serviço que remove todos registros da entidade.
	 * 
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@DeleteMapping
	public ResponseEntity deletarTodos() {
		try {
			LOGGER.info("Classe: {}, removendo todos registros", getClass().getName());
			client.excluirTodos();
			return ResponseEntity.ok().build();
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "deletarTodos", e);
		}
	}
}
