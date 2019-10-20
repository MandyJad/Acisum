package br.com.acisum.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ActionEvent;

import org.omnifaces.util.Faces;
import org.omnifaces.util.Messages;

import br.com.acisum.dao.CifraDAO;
import br.com.acisum.dao.PlayListDAO;
import br.com.acisum.domain.Cifra;
import br.com.acisum.domain.ItemPlaylistIndicada;
import br.com.acisum.domain.Playlist;
import br.com.acisum.util.ArquivosUtil;

@SuppressWarnings("serial")
@ManagedBean(name = "MBMinhasPlaylist")
@ViewScoped
public class MinhasPlayListBean implements Serializable {
	
	private List<Playlist> playlists;
	private Long idCantor;
	private PlayListDAO playlistDAO;
	private Playlist playlist;
	private List<ItemPlaylistIndicada> itemPlayListIndicada;
	private List<Cifra> cifras;
	private CifraDAO cifraDAO;

	@PostConstruct
	private void init() {
		LoginBean autenticacaoBean = Faces.getSessionAttribute("MBLogin");
		idCantor = autenticacaoBean.getUsuarioLogado().getCantor().getId();
		playlistDAO = new PlayListDAO();
		cifraDAO = new CifraDAO();
		playlists = buscarMinhasPlayList();
		playlist = new Playlist();
		buscarCifras();
		
	}
	
	public void cifraPDF(ActionEvent evento) {
		Cifra cifra = (Cifra) evento.getComponent().getAttributes().get("cifraSelecionada");
		ArquivosUtil.gerarPDF(cifra);
	}
	
	public void itemIndicadoPDF(ActionEvent evento) {
		ItemPlaylistIndicada item = (ItemPlaylistIndicada) evento.getComponent().getAttributes().get("itemSelecionada");
		ArquivosUtil.gerarPDF(item.getCifra());
	}
	
	public void excluirMinhaPlay(ActionEvent evento) {
		Playlist playlist = (Playlist) evento.getComponent().getAttributes().get("playlistSelecionada");
		try {
			playlistDAO.excluir(playlist);
			playlists = buscarMinhasPlayList();
			buscarCifras();
			Messages.addGlobalInfo("PlayList excluida com sucesso!");
		}catch(RuntimeException erro) {
			System.err.println("[EXCLUIR PLAYLIST]: " + erro.getMessage());
			Messages.addGlobalError("Ocorreu um erro ao tentar excluir a PlayList!");
		}
	}

	private void buscarCifras() {
		try {
			if(playlist.getId() == null) {
				cifras = cifraDAO.listarPorCantor(idCantor);
			}else {
				cifras = cifraDAO.listarCifrasPorPlaylist(playlist.getId());
			}
			
		}catch(RuntimeException erro) {
			System.err.println("[LISTAR CIFRAS]: " + erro.getMessage());
			Messages.addGlobalError("Ocorreu um erro ao tentar listar as Cifras!");
		}
	}
	
	public void buscarIndicadas() {
		itemPlayListIndicada = new ArrayList<>();
		try {
			if(playlist != null){
				itemPlayListIndicada = new CifraDAO().listarCifrasIndicadasPorPlaylist(playlist.getId());
				buscarCifras();
			}
		}catch(RuntimeException erro) {
			System.err.println("[LISTAR CIFRAS]: " + erro.getMessage());
			Messages.addGlobalError("Ocorreu um erro ao tentar excluir a PlayList!");
		}
	}

	private List<Playlist> buscarMinhasPlayList() {
		List<Playlist> playlists = new ArrayList<>();
		try {
		
			playlists = playlistDAO.listarPorCantor(idCantor);
						
		}catch(RuntimeException erro) {
			System.err.println("[LISTAR PLAYLISTS]: " + erro.getMessage());
			Messages.addGlobalError("Ocorreu um erro ao tentar listar suas PlayList's");
		}
		return playlists;
	}

	public List<Cifra> getCifras() {
		return cifras;
	}

	public void setCifras(List<Cifra> cifras) {
		this.cifras = cifras;
	}

	public List<ItemPlaylistIndicada> getItemPlayListIndicada() {
		return itemPlayListIndicada;
	}

	public void setItemPlayListIndicada(List<ItemPlaylistIndicada> itemPlayListIndicada) {
		this.itemPlayListIndicada = itemPlayListIndicada;
	}

	public Playlist getPlaylist() {
		return playlist;
	}

	public void setPlaylist(Playlist playlist) {
		this.playlist = playlist;
	}

	public List<Playlist> getPlaylists() {
		return playlists;
	}

	public void setPlaylists(List<Playlist> playlists) {
		this.playlists = playlists;
	}
	
}
