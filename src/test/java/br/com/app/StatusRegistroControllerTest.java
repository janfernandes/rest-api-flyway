package br.com.app;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.Arrays;

import javax.servlet.ServletContext;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockServletContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.app.controller.StatusRegistroController;
import br.com.app.model.StatusRegistro;
import br.com.app.service.StatusRegistroService;

@SpringBootTest
@RunWith(SpringRunner.class)
@ActiveProfiles("test")
public class StatusRegistroControllerTest {
	private MockMvc mockMvc;

	@Autowired
	private WebApplicationContext context;

	@MockBean
	private StatusRegistroService statusRegistroServiceMock;

	StatusRegistro str1, str2;

	@Before
	public void setup() {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(this.context).build();

		str1 = new StatusRegistro();
		str2 = new StatusRegistro();

		str1.setDescricao("descricao");
		str1.setId(1L);

		str1.setDescricao("descricao");
		str1.setId(2L);
	}

	@Test
	public void givenContext_whenServletContext_thenItProvidesController() {
		ServletContext servletContext = context.getServletContext();

		assertNotNull(servletContext);
		assertTrue(servletContext instanceof MockServletContext);
		assertNotNull(context.getBeansOfType(StatusRegistroController.class));
		assertNotNull(this.statusRegistroServiceMock);
	}

	private void checkAtributesStatusProjeto(ResultActions resultActions, Boolean isArray) throws Exception {
		String jsonIndex = isArray ? "$[0]." : "$.";

		resultActions.andExpect(jsonPath(jsonIndex + "descricao", is(str1.getDescricao())))
				.andExpect(jsonPath(jsonIndex + "abreviacao", is(str1.getAbreviacao())));
	}

	@Test
	public void buscarTodos() throws Exception {
		when(statusRegistroServiceMock.buscarTodos()).thenReturn(Arrays.asList(str1, str2));

		ResultActions resultActions = this.mockMvc
				.perform(get("/statusRegistro").accept(MediaType.APPLICATION_JSON_UTF8_VALUE)).andDo(print())
				.andExpect(status().isOk()).andExpect(jsonPath("$", hasSize(2)));

		this.checkAtributesStatusProjeto(resultActions, true);

		verify(statusRegistroServiceMock, times(1)).buscarTodos();
		verifyNoMoreInteractions(statusRegistroServiceMock);
	}

	@Test
	public void buscarTodosNaoEncontrado() throws Exception {
		when(statusRegistroServiceMock.buscarTodos()).thenReturn(new ArrayList<StatusRegistro>());

		this.mockMvc.perform(get("/statusRegistro").accept(MediaType.APPLICATION_JSON_UTF8_VALUE)).andDo(print())
				.andExpect(content().string(containsString("Nenhum registro encontrado")))
				.andExpect(status().isNoContent());

		verify(statusRegistroServiceMock, times(1)).buscarTodos();
		verifyNoMoreInteractions(statusRegistroServiceMock);
	}

	@Test
	public void buscarTodosException() throws Exception {
		when(statusRegistroServiceMock.buscarTodos()).thenThrow(NullPointerException.class);

		this.mockMvc.perform(get("/statusRegistro").accept(MediaType.APPLICATION_JSON_UTF8_VALUE)).andDo(print())
				.andExpect(status().isBadRequest());

		verify(statusRegistroServiceMock, times(1)).buscarTodos();
		verifyNoMoreInteractions(statusRegistroServiceMock);
	}

	@Test
	public void buscarPorId() throws Exception {
		when(statusRegistroServiceMock.buscarPorId(str1.getId())).thenReturn(str1);

		ResultActions resultActions = this.mockMvc.perform(get("/statusRegistro/id/{id}", str1.getId().toString())
				.contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)).andDo(print()).andExpect(status().isOk());

		this.checkAtributesStatusProjeto(resultActions, false);

		verify(statusRegistroServiceMock, times(1)).buscarPorId(str1.getId());
		verifyNoMoreInteractions(statusRegistroServiceMock);
	}

	@Test
	public void buscarPorIdNaoEncontrado() throws Exception {
		when(statusRegistroServiceMock.buscarPorId(str1.getId())).thenReturn(null);

		this.mockMvc
				.perform(get("/statusRegistro/id/{id}", str1.getId().toString())
						.contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
				.andDo(print()).andExpect(content().string(containsString(" não encontrado")))
				.andExpect(status().isNotFound());

		verify(statusRegistroServiceMock, times(1)).buscarPorId(str1.getId());
		verifyNoMoreInteractions(statusRegistroServiceMock);
	}

	@Test
	public void buscarPorIdException() throws Exception {
		when(statusRegistroServiceMock.buscarPorId(str1.getId())).thenThrow(NullPointerException.class);

		this.mockMvc
				.perform(get("/statusRegistro/id/{id}", (str1.getId()))
						.contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
				.andDo(print()).andExpect(status().isBadRequest());

		verify(statusRegistroServiceMock, times(1)).buscarPorId(str1.getId());
		verifyNoMoreInteractions(statusRegistroServiceMock);
	}

	@Test
	public void atualizar() throws Exception {
		when(statusRegistroServiceMock.buscarPorId(str1.getId())).thenReturn(str1);

		this.mockMvc
				.perform(
						put("/statusRegistro/{id}", str1.getId()).contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
								.content(new ObjectMapper().writeValueAsString(str1)))
				.andDo(print()).andExpect(status().isOk());

		verify(statusRegistroServiceMock, times(1)).buscarPorId(str1.getId());
		verify(statusRegistroServiceMock, times(1)).atualizar(str1);
		verifyNoMoreInteractions(statusRegistroServiceMock);
	}

	@Test
	public void atualizarIdNaoEncontrado() throws Exception {
		when(statusRegistroServiceMock.buscarPorId(str1.getId())).thenReturn(null);

		this.mockMvc
				.perform(
						put("/statusRegistro/{id}", str1.getId()).contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
								.content(new ObjectMapper().writeValueAsString(str1)))
				.andDo(print()).andExpect(content().string(containsString("Não foi possível atualizar. O id")))
				.andExpect(status().isNotFound());

		verify(statusRegistroServiceMock, times(1)).buscarPorId(str1.getId());
		verifyNoMoreInteractions(statusRegistroServiceMock);
	}

	@Test
	public void atualizarException() throws Exception {
		when(statusRegistroServiceMock.buscarPorId(str1.getId())).thenReturn(str1);

		doThrow(NullPointerException.class).when(statusRegistroServiceMock).atualizar(str1);

		this.mockMvc
				.perform(
						put("/statusRegistro/{id}", str1.getId()).contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
								.content(new ObjectMapper().writeValueAsString(str1)))
				.andDo(print()).andExpect(status().isBadRequest());

		verify(statusRegistroServiceMock, times(1)).buscarPorId(str1.getId());
		verify(statusRegistroServiceMock, times(1)).atualizar(str1);
		verifyNoMoreInteractions(statusRegistroServiceMock);
	}

	@Test
	public void criar() throws Exception {
		this.mockMvc
				.perform(post("/statusRegistro/").contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
						.content(new ObjectMapper().writeValueAsString(str1)))
				.andDo(print()).andExpect(status().isOk());

		verify(statusRegistroServiceMock, times(1)).salvar(str1);
		verifyNoMoreInteractions(statusRegistroServiceMock);
	}

	@Test
	public void criarException() throws Exception {
		doThrow(NullPointerException.class).when(statusRegistroServiceMock).salvar(str1);

		this.mockMvc
				.perform(post("/statusRegistro/").contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
						.content(new ObjectMapper().writeValueAsString(str1)))
				.andDo(print()).andExpect(status().isBadRequest());

		verify(statusRegistroServiceMock, times(1)).salvar(str1);
		verifyNoMoreInteractions(statusRegistroServiceMock);
	}
	
	@Test
	public void excluirPorId() throws Exception {
		when(statusRegistroServiceMock.buscarPorId(str1.getId())).thenReturn(str1);

		this.mockMvc.perform(
				delete("/statusRegistro/id/{id}", str1.getId()).contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
				.andDo(print()).andExpect(status().isOk());

		verify(statusRegistroServiceMock, times(1)).buscarPorId(str1.getId());
		verify(statusRegistroServiceMock, times(1)).excluir(str1);
		verifyNoMoreInteractions(statusRegistroServiceMock);
	}

	@Test
	public void excluirPorIdException() throws Exception {
		when(statusRegistroServiceMock.buscarPorId(str1.getId())).thenReturn(str1);

		doThrow(NullPointerException.class).when(statusRegistroServiceMock).excluir(str1);

		this.mockMvc
				.perform(delete("/statusRegistro/id/{id}", str1.getId())
						.contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
				.andDo(print()).andExpect(status().isBadRequest());

		verify(statusRegistroServiceMock, times(1)).buscarPorId(str1.getId());
		verify(statusRegistroServiceMock, times(1)).excluir(str1);
		verifyNoMoreInteractions(statusRegistroServiceMock);
	}

	@Test
	public void excluirPorIdIdNaoEncontrado() throws Exception {
		when(statusRegistroServiceMock.buscarPorId(str1.getId())).thenReturn(null);

		this.mockMvc
				.perform(delete("/statusRegistro/id/{id}", str1.getId())
						.contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
				.andDo(print()).andExpect(content().string(containsString("Não foi possível remover. O id ")))
				.andExpect(status().isNotFound());

		verify(statusRegistroServiceMock, times(1)).buscarPorId(str1.getId());
		verifyNoMoreInteractions(statusRegistroServiceMock);
	}
	
	@Test
	public void excluirTodos() throws Exception {
		this.mockMvc.perform(delete("/statusRegistro/").contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
				.andDo(print()).andExpect(status().isOk());

		verify(statusRegistroServiceMock, times(1)).excluirTodos();
		verifyNoMoreInteractions(statusRegistroServiceMock);
	}

	@Test
	public void excluirTodosException() throws Exception {
		doThrow(NullPointerException.class).when(statusRegistroServiceMock).excluirTodos();

		this.mockMvc.perform(delete("/statusRegistro/").contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
				.andDo(print()).andExpect(status().isBadRequest());

		verify(statusRegistroServiceMock, times(1)).excluirTodos();
		verifyNoMoreInteractions(statusRegistroServiceMock);
	}

	@Test
	
	public void getService() {
		assertNotNull(((StatusRegistroController) context.getBean("statusRegistroController")).getService());
	}
}
