package br.com.acisum.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.validator.constraints.NotBlank;

@SuppressWarnings("serial")
@Entity
@Table(name = "cifra")
public class Cifra extends GenericDomain {

	@Column(length = 150, nullable = false)
	@NotBlank(message = "Nome da Cifra é obrigatório!")
	private String nome;

	@Column(length = 200, nullable = false)
	private String pdf;

	@ManyToOne
	@JoinColumn(nullable = false, name = "id_cantor")
	private Cantor cantor;

	@ManyToOne
	@JoinColumn(nullable = false, name = "id_genero")
	private Genero genero;

	public Cantor getCantor() {
		return cantor;
	}

	public void setCantor(Cantor cantor) {
		this.cantor = cantor;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public Genero getGenero() {
		return genero;
	}

	public void setGenero(Genero genero) {
		this.genero = genero;
	}

	public String getPdf() {
		return pdf;
	}

	public void setPdf(String pdf) {
		this.pdf = pdf;
	}

}
