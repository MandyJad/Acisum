package br.com.acisum.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@SuppressWarnings("serial")
@Entity
@Table(name = "item_playlist_indicada")
public class ItemPlaylistIndicada extends GenericDomain{

	@ManyToOne
	@JoinColumn(nullable = false, name = "id_playlist")
	private Playlist playlist;

	@ManyToOne
	@JoinColumn(nullable = false, name = "id_cifra")
	private Cifra cifra;
	
	@Column(name = "pedidos")
	private Integer pedidos;

	public Playlist getPlaylist() {
		return playlist;
	}

	public void setPlaylist(Playlist playlist) {
		this.playlist = playlist;
	}

	public Cifra getCifra() {
		return cifra;
	}

	public void setCifra(Cifra cifra) {
		this.cifra = cifra;
	}

	public Integer getPedidos() {
		return pedidos;
	}

	public void setPedidos(Integer pedidos) {
		this.pedidos = pedidos;
	}
	
}
