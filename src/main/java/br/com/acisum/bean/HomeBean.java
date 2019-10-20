package br.com.acisum.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.omnifaces.util.Messages;

import br.com.acisum.dao.CantorDAO;
import br.com.acisum.dao.CifraDAO;
import br.com.acisum.dao.PlayListDAO;
import br.com.acisum.domain.Cantor;
import br.com.acisum.domain.Cifra;
import br.com.acisum.domain.ItemPlaylistIndicada;
import br.com.acisum.domain.Playlist;

@SuppressWarnings("serial")
@ManagedBean(name = "MBHome")
@ViewScoped
public class HomeBean implements Serializable {

	private PlayListDAO playlistDAO;
	private Playlist playlist;
	private Cantor cantor;
	private List<Cantor> cantores;
	private List<Cifra> cifras;
	private List<Cifra> cifrasSelecionadas;
	
	@PostConstruct
	private void init() {
		playlistDAO = new PlayListDAO();
		cantores = buscarCantores();
		cantor = new Cantor();
		playlist = new Playlist();
	}
	
	public void indicar() {
		List<ItemPlaylistIndicada> novaLista = new ArrayList<>();
		if(cifrasSelecionadas != null && !cifrasSelecionadas.isEmpty()) {
			List<ItemPlaylistIndicada> listaSalva = new CifraDAO().listarCifrasIndicadasPorPlaylist(playlist.getId());
			for(ItemPlaylistIndicada item : listaSalva) {
				if(cifrasSelecionadas.contains(item.getCifra())) {
					cifrasSelecionadas.remove(item.getCifra());
					Integer valor = item.getPedidos() + 1;
					item.setPedidos(valor);
					novaLista.add(item);
				} 
			}
			
			for(Cifra c : cifrasSelecionadas) {
				ItemPlaylistIndicada itemNew = new ItemPlaylistIndicada();
				itemNew.setCifra(c);
				itemNew.setPedidos(1);
				itemNew.setPlaylist(playlist);
				novaLista.add(itemNew);
			}
			try {
				playlistDAO.salvarPlayListIndicada(novaLista);
				Messages.addGlobalInfo("Indicação salva com sucesso!");
				buscarCantores();
				cifrasSelecionadas = new ArrayList<Cifra>();
				cantor = new Cantor();
				cifras = new ArrayList<Cifra>();
			}catch(RuntimeException erro) {
				System.err.println("[SALVAR LISTA]: " + erro);
				Messages.addGlobalError("Ocorreu um erro ao tentar salvar Cifras!");
			}
			
		}
	}
	
	public void buscarCifras() {
		cifras = new ArrayList<Cifra>();
		try {
			cifras = new CifraDAO().listarPorCantor(cantor.getId());
			playlist = playlistDAO.buscar(cantor.getId());
		}catch(RuntimeException erro) {
			System.err.println("[LISTAR CIFRAS]: " + erro);
			Messages.addGlobalError("Ocorreu um erro ao tentar listar Cifras!");
		}
	}
	
	private List<Cantor> buscarCantores() {
		List<Cantor> cantores = new ArrayList<Cantor>();
		try {
			cantores = new CantorDAO().listar();
		}catch(RuntimeException erro) {
			System.err.println("[LISTAR CANTORES]: " + erro);
			Messages.addGlobalError("Ocorreu um erro ao tentar listar os Cantores!");
		}
		return cantores;
	}

	public Cantor getCantor() {
		return cantor;
	}
	public void setCantor(Cantor cantor) {
		this.cantor = cantor;
	}
	public List<Cantor> getCantores() {
		return cantores;
	}
	public void setCantores(List<Cantor> cantores) {
		this.cantores = cantores;
	}
	public List<Cifra> getCifras() {
		return cifras;
	}
	public void setCifras(List<Cifra> cifras) {
		this.cifras = cifras;
	}

	public Playlist getPlaylist() {
		return playlist;
	}

	public void setPlaylist(Playlist playlist) {
		this.playlist = playlist;
	}

	public List<Cifra> getCifrasSelecionadas() {
		return cifrasSelecionadas;
	}

	public void setCifrasSelecionadas(List<Cifra> cifrasSelecionadas) {
		this.cifrasSelecionadas = cifrasSelecionadas;
	}

}
