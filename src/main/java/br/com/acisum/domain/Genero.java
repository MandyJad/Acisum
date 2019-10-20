package br.com.acisum.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.validator.constraints.NotBlank;

@SuppressWarnings("serial")
@Entity
@Table(name = "genero")
public class Genero extends GenericDomain {
	
	@Column(length = 50, nullable = false) 
	@NotBlank(message = "Nome do Genero é obrigatório!")
	private String nome;

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}
	 
}
