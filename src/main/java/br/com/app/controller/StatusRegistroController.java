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

import br.com.app.model.StatusRegistro;
import br.com.app.service.StatusRegistroService;
import br.com.app.util.ErroGenerico;
import lombok.Getter;

/**
 * Classe controller que expoe os serviços da classe StatusRegistro
 * 
 * @author Danilo Humberto
 *
 */
@RestController
@RequestMapping("/statusRegistro")
public class StatusRegistroController {
	
	/**
	 * logger
	 */
	public static final Logger LOGGER = LoggerFactory.getLogger(StatusRegistroController.class);

	/**
	 * Injeção de dependencia.
	 */
	@Autowired
	@Getter
	private StatusRegistroService service;
	public StatusRegistroService getService() {
		return service;
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
			List<StatusRegistro> listaRetorno = null;
			if (sortings != null && sortings.length > 0) {
				listaRetorno = service.buscarTodos(sortings);
			} else {
				listaRetorno = service.buscarTodos();
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
	@GetMapping("id/{id}")
	public ResponseEntity buscarPorId(@PathVariable("id") Long id) {
		try {
			LOGGER.info("Classe: {}, buscando com o id {}", getClass().getName(), id);
			StatusRegistro entity = service.buscarPorId(id);
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
	public ResponseEntity criar(@RequestBody StatusRegistro entity) {
		try {
			LOGGER.info("Classe: {}, criando registro: {}", getClass().getName(), entity);
			service.salvar(entity);
			
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
	public ResponseEntity atualizar(@RequestBody StatusRegistro entity, @PathVariable Long id) {
		LOGGER.info("Classe: {}, atualizar registro c/ id {}", getClass().getName(), id);
		try {
			StatusRegistro returnEntity = service.buscarPorId(id);

			if (returnEntity == null) {
				LOGGER.error("Não foi possível atualizar. O id {} não foi encontrado.", id);
				return new ResponseEntity(
						new ErroGenerico("Não foi possível atualizar. O id " + id + " não foi encontrado."),
						HttpStatus.NOT_FOUND);
			}
			returnEntity.setAbreviacao(entity.getAbreviacao());
			returnEntity.setDescricao(entity.getDescricao());
			service.atualizar(returnEntity);

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
	@DeleteMapping("id/{id}")
	public ResponseEntity deletar(@PathVariable("id") Long id) {
		try {
			LOGGER.info("Classe: {}, removendo registro c/ id {}", getClass().getName(), id);

			StatusRegistro entity = service.buscarPorId(id);

			if (entity == null) {
				LOGGER.error("Não foi possível remover. O id {} não foi encontrado.", id);
				return new ResponseEntity(
						new ErroGenerico("Não foi possível remover. O id " + id + " não foi encontrado."),
						HttpStatus.NOT_FOUND);
			}

			service.excluir(entity);

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
			service.excluirTodos();
			return ResponseEntity.ok().build();
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "deletarTodos", e);
		}
	}

}
