package br.com.acisum.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.persistence.Id;
import javax.servlet.http.HttpServletRequest;

import org.omnifaces.util.Messages;

import br.com.acisum.dao.CifraDAO;
import br.com.acisum.dao.PlayListDAO;
import br.com.acisum.domain.Cantor;
import br.com.acisum.domain.Cifra;
import br.com.acisum.domain.ItemPlaylistIndicada;
import br.com.acisum.domain.Playlist;

@SuppressWarnings("serial")
@ManagedBean(name = "MBClientePlaylist")
@ViewScoped
public class ClientePlayListBean implements Serializable {
	
	private List<Playlist> playlists;
	private Long idCantor;
	private PlayListDAO playlistDAO;
	private Playlist playlist;
	private List<ItemPlaylistIndicada> itemPlayListIndicada;
	private List<ItemPlaylistIndicada> itemPlaylistConsolidada;
	private List<Cifra> cifras;
	private CifraDAO cifraDAO;
	

	@PostConstruct
	private void init() {
		playlistDAO = new PlayListDAO();
		cifraDAO = new CifraDAO();
		playlist = recuperarPlaylist();
		playlists = buscarMinhasPlayList();
		buscarCifras();
	}

	private Playlist recuperarPlaylist() {
		final HttpServletRequest req = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext()
				.getRequest();
		String playlistId = req.getParameter("playlist");
		if (playlistId == null) {
			return new Playlist();
		}
		try {
			return playlistDAO.buscarPorId(Long.parseLong(playlistId));
		} catch (NumberFormatException e) {
			System.err.println("Valor do id de playlist não é válido: " + playlistId);
			return new Playlist();
		}
	}

	public List<Cifra> buscarCifrasPorCantor() {
		try {
			return cifraDAO.listarPorCantor(playlist.getCantor().getId());
		} catch (RuntimeException erro) {
			System.err.println("[LISTAR CIFRAS POR CANTOR]: " + erro.getMessage());
			Messages.addGlobalError("Ocorreu um erro ao tentar listar as Cifras!");
			return null;
		}
	}

	public List<Cifra> buscarCifras() {
		try {
			if(playlist.getId() == null) {
				System.err.println("Não possui playlist");
			}else {
				cifras = cifraDAO.listarCifrasPorPlaylist(playlist.getId());
			}
			return cifras;
			
		}catch(RuntimeException erro) {
			System.err.println("[LISTAR CIFRAS]: " + erro.getMessage());
			Messages.addGlobalError("Ocorreu um erro ao tentar listar as Cifras!");
			return null;
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
			Messages.addGlobalError("Ocorreu um erro ao tentar listar as cifras!");
		}
	}
	
	public List<ItemPlaylistIndicada> buscaConsolidada() {
		try {
			itemPlaylistConsolidada = new ArrayList<>();
			if (playlist != null) {
				itemPlayListIndicada = new CifraDAO().listarCifrasIndicadasPorPlaylist(playlist.getId());
			}
			
			if (itemPlayListIndicada != null) {
				for (ItemPlaylistIndicada indicada : itemPlayListIndicada) {
					itemPlaylistConsolidada.add(indicada);
				}
			}
			final List<Cifra> cifrasPlaylist = cifraDAO.listarCifrasPorPlaylist(playlist.getId());
			for (Cifra itemPlaylist : cifrasPlaylist) {
				if (!buscaCifraEmLista(itemPlaylist, itemPlaylistConsolidada)) {
					ItemPlaylistIndicada itemPlaylistCifra = new ItemPlaylistIndicada();
					itemPlaylistCifra.setCifra(itemPlaylist);
					itemPlaylistCifra.setPedidos(0);
					itemPlaylistConsolidada.add(itemPlaylistCifra);
				}
			}

			return itemPlaylistConsolidada;
		} catch(RuntimeException erro) {
			System.err.println("[LISTAR CIFRAS]: " + erro.getMessage());
			return null;
		}
		
	}
	

	private boolean buscaCifraEmLista(Cifra cifra, List<ItemPlaylistIndicada> lista) {
		for (ItemPlaylistIndicada item : lista) {
			if (item.getCifra().equals(cifra)) {
				return true;
			}
		}
		return false;
	}

	@SuppressWarnings("unused")
	private ItemPlaylistIndicada buscaItemPlaylistIndicada(Cifra cifra) {
		if (itemPlayListIndicada == null) {
			return null;
		}
		for (ItemPlaylistIndicada indicada : itemPlayListIndicada) {
			if (indicada.getCifra().equals(cifra)) {
				return indicada;
			}
		}
		return null;
	}
	
	private List<Playlist> buscarMinhasPlayList() {
		List<Playlist> playlists = new ArrayList<>();
		try {
		
			playlists = playlistDAO.listarTodasPlaylists(idCantor);
						
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
	
	public List<ItemPlaylistIndicada> getItemPlaylistConsolidada() {
		return itemPlaylistConsolidada;
	}

	public void setItemPlaylistConsolidada(List<ItemPlaylistIndicada> itemPlaylistConsolidada) {
		this.itemPlaylistConsolidada = itemPlaylistConsolidada;
	}

	public Long getIdCantor() {
		return idCantor;
	}

	public void setIdCantor(Long idCantor) {
		this.idCantor = idCantor;
	}
	
}