package br.com.acisum.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.omnifaces.util.Faces;
import org.omnifaces.util.Messages;
import org.primefaces.PrimeFaces;
import org.primefaces.model.DualListModel;
import org.primefaces.model.StreamedContent;

import br.com.acisum.dao.CantorDAO;
import br.com.acisum.dao.CifraDAO;
import br.com.acisum.dao.GeneroDAO;
import br.com.acisum.dao.PlayListDAO;
import br.com.acisum.domain.Cifra;
import br.com.acisum.domain.Genero;
import br.com.acisum.domain.ItemPlaylist;
import br.com.acisum.domain.Playlist;

@SuppressWarnings("serial")
@ManagedBean(name = "MBPlaylist")
@ViewScoped
public class PlaylistBean implements Serializable {

	private CifraDAO cifraDAO;
	private GeneroDAO generoDAO;
	private PlayListDAO playlistDAO;
	
	private List<Cifra> cifrasPlayList;
	private List<Genero> generos;
	private Playlist playlist;
	private List<Playlist> playLists;
	
	private Genero genero;
	private Cifra cifra;
	private Long idCantor;
	
	private StreamedContent content;
	private DualListModel<Cifra> cifrasModel;

	@PostConstruct
	private void init() {
		LoginBean autenticacaoBean = Faces.getSessionAttribute("MBLogin");
		idCantor = autenticacaoBean.getUsuarioLogado().getCantor().getId();
		cifraDAO = new CifraDAO();	
		generoDAO = new GeneroDAO();
		playlistDAO = new PlayListDAO();
		playlist = new Playlist();
		cifrasPlayList = new ArrayList<Cifra>();
		
		generos = buscarGeneros();
		playLists = buscarPlaylists(idCantor);
		cifrasModel = new DualListModel<Cifra>(buscarCifrasCantor(idCantor), cifrasPlayList);
	}
	
	public void novaPlaylist() {
		playlist = new Playlist();
	}
	
	public void salvarPlayList() {
		try {
			playlist.setCantor(new CantorDAO().buscar(idCantor));
			playlist = playlistDAO.merge(playlist);
			generos = buscarGeneros();
			playLists = buscarPlaylists(idCantor);
			PrimeFaces.current().executeScript("PF('dlgNovoEstabelecimento').hide();");
			Messages.addGlobalInfo("PlayList salva com Sucesso!");
		}catch(RuntimeException erro) {
			System.err.println("[SALVAR PLAYLIST POR CANTOR E GENERO]: " + erro.getMessage());
			Messages.addGlobalError("Erro ao salvar a PlayList!");
		}
	}
	
	private List<Playlist> buscarPlaylists(Long idCantor) {
		List<Playlist> lista = new ArrayList<>();
		try {
			lista = playlistDAO.listarPorCantor(idCantor);
		}catch(RuntimeException erro) {
			System.err.println("[LISTAR PLAYLIST POR CANTOR E GENERO]: " + erro.getMessage());
			Messages.addGlobalError("Erro ao listar suas PlayList's!");
		}
		return lista;
	}

	public void salvarPlaylist() {
		
		cifrasPlayList = (ArrayList<Cifra>) cifrasModel.getTarget();
		
		if (cifrasPlayList == null || cifrasPlayList.size() == 0) {
			Messages.addGlobalWarn("Selecione no mínimo uma Cifra!");
			return;
		} else if(playlist == null || playlist.getId() == null) {
			Messages.addGlobalWarn("Selecione um local!");
			return;
		} else {
			playlist.setCantor(new CantorDAO().buscar(idCantor));
			List<ItemPlaylist> listaItens = new ArrayList<>();
			for(Cifra cifra : cifrasPlayList) {
				ItemPlaylist item = new ItemPlaylist();
				item.setCifra(cifra);
				listaItens.add(item);
			}
			try {
				playlistDAO.salvarPlayList(playlist, listaItens);
				genero = new Genero();
				cifrasPlayList = new ArrayList<>();
				cifrasModel = new DualListModel<Cifra>(buscarCifrasCantor(idCantor), cifrasPlayList);
				Messages.addGlobalInfo("PlayList salva com sucesso!");
			}catch(RuntimeException erro) {
				System.err.println("[SALVAR PLAYLIST]: " + erro.getMessage());
				Messages.addGlobalError("Erro ao salvar sua PlayList!");
			}
			
		}
	}
	
	public void buscarCifrasCantorGenero() {
		List<Cifra> cifras = new ArrayList<Cifra>();
		try {
			if(genero != null && genero.getId() != null) {
				cifras = cifraDAO.listarPorCantorGenero(idCantor, genero.getId());
				cifrasModel = new DualListModel<Cifra>(cifras, cifrasPlayList);
			}
		}catch(RuntimeException erro) {
			System.err.println("[LISTAR CIFRAS POR CANTOR E GENERO]: " + erro.getMessage());
			Messages.addGlobalError("Erro ao listar suas Cifras!");
		}
	}

	private List<Genero> buscarGeneros() {
		ArrayList<Genero> generos = new ArrayList<Genero>();
		try {
			generos = generoDAO.listar();
		}catch(RuntimeException erro) {
			System.err.println("[LISTAR GENEROS]: " + erro.getMessage());
			Messages.addGlobalError("Ocorreu um erro ao buscar os Estilos Musicais!");
		}
		return generos;
	}

	private List<Cifra> buscarCifrasCantor(Long idCantor) {
		List<Cifra> cifras = new ArrayList<Cifra>();
		try {
			cifras = cifraDAO.listarPorCantor(idCantor);
		}catch(RuntimeException erro) {
			System.err.println("[LISTAR CIFRAS POR CANTOR]: " + erro.getMessage());
			Messages.addGlobalError("Erro ao listar suas Cifras!");
		}
		
		return cifras;
	}

	public Playlist getPlaylist() {
		return playlist;
	}

	public void setPlaylist(Playlist playlist) {
		this.playlist = playlist;
	}

	public List<Playlist> getPlayLists() {
		return playLists;
	}

	public void setPlayLists(List<Playlist> playLists) {
		this.playLists = playLists;
	}

	public DualListModel<Cifra> getCifrasModel() {
		return cifrasModel;
	}

	public List<Cifra> getCifrasPlayList() {
		return cifrasPlayList;
	}

	public void setCifrasPlayList(ArrayList<Cifra> cifrasPlayList) {
		this.cifrasPlayList = cifrasPlayList;
	}

	public void setCifrasModel(DualListModel<Cifra> cifrasModel) {
		this.cifrasModel = cifrasModel;
	}

	public Genero getGenero() {
		return genero;
	}

	public void setGenero(Genero genero) {
		this.genero = genero;
	}

	public Cifra getCifra() {
		return cifra;
	}

	public void setCifra(Cifra cifra) {
		this.cifra = cifra;
	}

	public List<Genero> getGeneros() {
		return generos;
	}

	public void setGeneros(List<Genero> generos) {
		this.generos = generos;
	}

	public StreamedContent getContent() {
		return content;
	}

	public void setContent(StreamedContent content) {
		this.content = content;
	}

}
