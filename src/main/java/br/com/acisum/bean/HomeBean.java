package br.com.acisum.bean;


import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.omnifaces.util.Faces;
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
	private Cifra cifraSelecionada;
	
	@PostConstruct
	private void init() {
		
		playlistDAO = new PlayListDAO();
		cantores = buscarCantores();
		cantor = new Cantor();
		playlist = new Playlist();
	}
	
	public void indicar() throws IOException {
		String ipUsuario = getIPUsuario();
		List<ItemPlaylistIndicada> novaLista = new ArrayList<>();
		if(cifraSelecionada != null) {
			List<ItemPlaylistIndicada> listaSalva = new CifraDAO().listarCifrasIndicadasPorPlaylist(playlist.getId());
			ItemPlaylistIndicada novaIndicacao = new ItemPlaylistIndicada();
			novaIndicacao.setPlaylist(playlist);
			novaIndicacao.setCifra(cifraSelecionada);
			novaIndicacao.setPedidos(1);
			
			for(ItemPlaylistIndicada item : listaSalva) {
				if(cifraSelecionada.equals(item.getCifra())) {
					Integer valor = item.getPedidos() + 1;
					item.setPedidos(valor);
					novaIndicacao = item;
				}
			}
			novaLista.add(novaIndicacao);
			
			try {
				playlistDAO.salvarPlayListIndicada(novaLista);
				Messages.addGlobalInfo("Indicação salva com sucesso!");
				buscarCantores();
				cifraSelecionada  = new Cifra();
				cantor = new Cantor();
				cifras = new ArrayList<Cifra>();
				Faces.redirect("pages/cliente_playlist.xhtml?playlist=" + playlist.getId());
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
	
	private String getIPUsuario() {
        final ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
        final Object requestObj = context.getRequest();
 
        if(requestObj instanceof HttpServletRequest) {
            HttpServletRequest httpRequest = (HttpServletRequest)requestObj;
            return httpRequest.getRemoteAddr();
        }
        return null; // Não foi possível identificar tipo de request
    }
	
	public void eMusico() {	
		addMessage("Caro Cliente, não é necessário cadastro para escolher sua música", "Basta selecionar o músico na barra de opções");
	}
	
	public void addMessage(String summary, String detail) {
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, summary, detail);
        FacesContext.getCurrentInstance().addMessage(null, message);
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

	public Cifra getCifraSelecionada() {
		return cifraSelecionada;
	}

	public void setCifraSelecionada(Cifra cifraSelecionada) {
		this.cifraSelecionada = cifraSelecionada;
	}


}
