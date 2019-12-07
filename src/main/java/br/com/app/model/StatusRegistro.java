package br.com.app.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.Size;

/**
 * Classe POJO StatusRegistro
 * 
 * @author Danilo Humberto
 *
 */
@Entity
@Table(name = "STATUS_REGISTRO")
public class StatusRegistro {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -5459614573103535997L;

	/**
	 * Id
	 */
	@Id
	@SequenceGenerator(name = "pk_sequence_status_registro", sequenceName = "status_registro_id_seq", allocationSize = 1, initialValue = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "pk_sequence_status_registro")
	@Column(name = "ID", nullable = false)
	private Long id;

	/**
	 * DESCRICAO
	 */
	@Column(name = "DESCRICAO", nullable = false)
	@Size(max = 50)
	private String descricao;

	/**
	 * ABREVIACAO
	 */
	@Column(name = "abreviacao", nullable = false)
	@Size(max = 3)
	private String abreviacao;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public String getAbreviacao() {
		return abreviacao;
	}

	public void setAbreviacao(String abreviacao) {
		this.abreviacao = abreviacao;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((abreviacao == null) ? 0 : abreviacao.hashCode());
		result = prime * result + ((descricao == null) ? 0 : descricao.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		StatusRegistro other = (StatusRegistro) obj;
		if (abreviacao == null) {
			if (other.abreviacao != null)
				return false;
		} else if (!abreviacao.equals(other.abreviacao))
			return false;
		if (descricao == null) {
			if (other.descricao != null)
				return false;
		} else if (!descricao.equals(other.descricao))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

}
