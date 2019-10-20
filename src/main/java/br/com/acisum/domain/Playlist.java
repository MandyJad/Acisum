package br.com.acisum.domain;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

@SuppressWarnings("serial")
@Entity
@Table(name = "playlist")
public class Playlist extends GenericDomain {

	@Column(name = "nome", nullable = false)
	private String nome;
	
//	@Column(nullable = false, name = "data_apresentacao")
//	@Temporal(TemporalType.TIMESTAMP)
//	private Date dataApresentacao;
	
	@ManyToOne
	@JoinColumn(nullable = false, name = "id_cantor")
	private Cantor cantor;

	@Transient
	private List<Cifra> cifras;

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public List<Cifra> getCifras() {
		return cifras;
	}

	public void setCifras(List<Cifra> cifras) {
		this.cifras = cifras;
	}

	public Cantor getCantor() {
		return cantor;
	}

	public void setCantor(Cantor cantor) {
		this.cantor = cantor;
	}

}
