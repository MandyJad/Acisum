package br.com.acisum.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.validator.constraints.NotBlank;

@SuppressWarnings("serial")
@Entity
@Table(name = "cantor")
public class Cantor extends GenericDomain{

	@Column(nullable = false, length = 50)
	@NotBlank(message = "Nome é obrigatório")
	private String nome;
	
	@ManyToOne
	@JoinColumn(name = "id_playlist")
	private Playlist playlist;
	
	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public Playlist getPlaylist() {
		return playlist;
	}

	public void setPlaylist(Playlist playlist) {
		this.playlist = playlist;
	}
	
	
	
	
	
}
