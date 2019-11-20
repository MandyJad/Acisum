package br.com.acisum.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;

@SuppressWarnings("serial")
@Entity
@Table(name = "usuario")
public class Usuario extends GenericDomain {

	@OneToOne
	@JoinColumn(nullable = false, name = "id_cantor")
	private Cantor cantor;

	@Column(length = 100, nullable = false)
	@NotBlank(message = "E-mail é obrigatório!")
	@Email(message = "E-mail incorreto")
	private String email;

	@Column(length = 64, nullable = false)
	@NotEmpty(message = "Senha é obrigatório!")
	private String senha;
	
	public Cantor getCantor() {
		return cantor;
	}

	public void setCantor(Cantor cantor) {
		this.cantor = cantor;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

}
