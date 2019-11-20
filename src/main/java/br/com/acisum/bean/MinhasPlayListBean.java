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
import br.com.acisum.dao.ItemPlaylistDAO;
import br.com.acisum.dao.ItemPlaylistIndicadaDAO;
import br.com.acisum.dao.PlayListDAO;
import br.com.acisum.domain.Cifra;
import br.com.acisum.domain.ItemPlaylist;
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
	private ItemPlaylistIndicada ItemPlaylistIndicada;
	private Cifra cifra;
	private ItemPlaylist itemPlaylist;
	private ItemPlaylistDAO itemPlaylistDAO;
	

	@PostConstruct
	private void init() {
		LoginBean autenticacaoBean = Faces.getSessionAttribute("MBLogin");
		idCantor = autenticacaoBean.getUsuarioLogado().getCantor().getId();
		playlistDAO = new PlayListDAO();
		cifraDAO = new CifraDAO();
		playlists = buscarMinhasPlayList();
		playlist = new Playlist();
		
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
		} catch (RuntimeException erro) {
			System.err.println("[EXCLUIR PLAYLIST]: " + erro.getMessage());
			Messages.addGlobalError("Ocorreu um erro ao tentar excluir o local!");
		}
	}

	public void excluirCifra(ActionEvent  evento) {
		
		try {
			ItemPlaylistIndicada itemPlaylistIndicada = (ItemPlaylistIndicada) evento.getComponent().getAttributes().get("cifraSelecionada");
			ItemPlaylistIndicadaDAO itemPlaylistIndicadaDAO = new ItemPlaylistIndicadaDAO();
			ItemPlaylistDAO itemPlaylistDAO = new ItemPlaylistDAO();
			
			Long idPlaylist = playlist.getId();
			Long idCifra = itemPlaylistIndicada.getCifra().getId();
			ItemPlaylist itemPlaylist = itemPlaylistDAO.buscaItemPlaylist(idPlaylist, idCifra);

			itemPlaylistIndicadaDAO.excluir(itemPlaylistIndicada);
			if (itemPlaylist != null) {
				itemPlaylistDAO.excluir(itemPlaylist);
			}
			buscarCifras();
			
			Messages.addGlobalInfo("Cifra excluída com sucesso!");
			
		} catch (RuntimeException erro) {
			Messages.addGlobalError("Ocorreu um erro ao tentar excluir a cifra");
			erro.printStackTrace();
		}
		
	}

	public List<Cifra> buscarCifras() {
		try {
			if (playlist.getId() == null) {
				cifras = cifraDAO.listarPorCantor(idCantor);
			} else {
				cifras = cifraDAO.listarCifrasPorPlaylist(playlist.getId());
			}
			return cifras;

		} catch (RuntimeException erro) {
			System.err.println("[LISTAR CIFRAS]: " + erro.getMessage());
			Messages.addGlobalError("Ocorreu um erro ao tentar listar as Cifras!");
			return null;
		}
	}

	public List<Cifra> buscarCifrasPorCantor() {
		try {
			return cifraDAO.listarPorCantor(idCantor);
		} catch (RuntimeException erro) {
			System.err.println("[LISTAR CIFRAS POR CANTOR]: " + erro.getMessage());
			Messages.addGlobalError("Ocorreu um erro ao tentar listar as Cifras!");
			return null;
		}
	}

	public void buscarIndicadas() {
		itemPlayListIndicada = new ArrayList<>();
		try {
			if (playlist != null) {
				itemPlayListIndicada = new CifraDAO().listarCifrasIndicadasPorPlaylist(playlist.getId());
				buscarCifras();
			}
		} catch (RuntimeException erro) {
			System.err.println("[LISTAR CIFRAS]: " + erro.getMessage());
			Messages.addGlobalError("Ocorreu um erro ao tentar excluir a PlayList!");
		}
	}

	public List<ItemPlaylistIndicada> buscaConsolidada() {
		
		try {
			itemPlaylistConsolidada = new ArrayList<>();

			Long playlistId = null;
			if (playlist != null && playlist.getId() != null) { //Playlist selecionada
				playlistId = playlist.getId();
			} else if (idCantor != null) { //Playlist do Cantor
				playlistId = new PlayListDAO().buscar(idCantor).getId();
			} else { //Primeira Playlist do banco
				playlistId = new PlayListDAO().listarTodasPlaylists(idCantor).get(0).getId();
			}
			
			if (itemPlayListIndicada != null) {
				for (ItemPlaylistIndicada indicada : itemPlayListIndicada) {
					itemPlaylistConsolidada.add(indicada);
				}
			}
			final List<Cifra> cifrasPlaylist = cifraDAO.listarCifrasPorPlaylist(playlistId);
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

	public ItemPlaylistIndicada getItemPlaylistIndicada() {
		return ItemPlaylistIndicada;
	}

	public void setItemPlaylistIndicada(ItemPlaylistIndicada itemPlaylistIndicada) {
		ItemPlaylistIndicada = itemPlaylistIndicada;
	}

	public Cifra getCifra() {
		return cifra;
	}

	public void setCifra(Cifra cifra) {
		this.cifra = cifra;
	}

}