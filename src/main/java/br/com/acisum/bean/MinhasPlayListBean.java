package br.com.acisum.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ActionEvent;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.omnifaces.util.Faces;
import org.omnifaces.util.Messages;

import antlr.debug.Event;
import br.com.acisum.dao.CifraDAO;
import br.com.acisum.dao.PlayListDAO;
import br.com.acisum.domain.Cifra;
import br.com.acisum.domain.ItemPlaylistIndicada;
import br.com.acisum.domain.Playlist;
import br.com.acisum.util.ArquivosUtil;
import br.com.acisum.util.HibernateUtil;

@SuppressWarnings("serial")
@ManagedBean(name = "MBMinhasPlaylist")
@ViewScoped
public class MinhasPlayListBean implements Serializable {
	
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

	public List<Cifra> buscarCifras() {
		try {
			if(playlist.getId() == null) {
				cifras = cifraDAO.listarPorCantor(idCantor);
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
			Messages.addGlobalError("Ocorreu um erro ao tentar excluir a PlayList!");
		}
	}
	
	public List<ItemPlaylistIndicada> buscaConsolidada() {
		final List<Cifra> cifras = buscarCifras();
		itemPlaylistConsolidada = new ArrayList<>(cifras.size());
		try {
			if(playlist != null) {
				itemPlayListIndicada = new CifraDAO().listarCifrasIndicadasPorPlaylist(playlist.getId());
			}
			
			for (Cifra cifra : cifras) {
				ItemPlaylistIndicada indicada = buscaItemPlaylistIndicada(cifra);
				if (indicada != null) {
					ItemPlaylistIndicada consolidada = new ItemPlaylistIndicada();
					consolidada.setCifra(cifra);
					consolidada.setPedidos(indicada.getPedidos());
					itemPlaylistConsolidada.add(consolidada);
					Collections.reverse(itemPlaylistConsolidada); 
					
				} else {
					ItemPlaylistIndicada consolidada = new ItemPlaylistIndicada();
					consolidada.setCifra(cifra);
					consolidada.setPedidos(0);
					itemPlaylistConsolidada.add(consolidada);
				}
	
			}
			
			return itemPlaylistConsolidada;
		} catch(RuntimeException erro) {
			System.err.println("[LISTAR CIFRAS]: " + erro.getMessage());
			Messages.addGlobalError("Ocorreu um erro ao tentar listar a PlayList!");
			return null;
		}
		
	}
	

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
	
	public List<ItemPlaylistIndicada> getItemPlaylistConsolidada() {
		return itemPlaylistConsolidada;
	}

	public void setItemPlaylistConsolidada(List<ItemPlaylistIndicada> itemPlaylistConsolidada) {
		this.itemPlaylistConsolidada = itemPlaylistConsolidada;
	}
	
}