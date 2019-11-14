
package br.com.acisum.bean;

import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ActionEvent;

import org.omnifaces.util.Faces;
import org.omnifaces.util.Messages;
import org.primefaces.PrimeFaces;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;

import br.com.acisum.dao.CantorDAO;
import br.com.acisum.dao.CifraDAO;
import br.com.acisum.dao.GeneroDAO;
import br.com.acisum.domain.Cifra;
import br.com.acisum.domain.Genero;
import br.com.acisum.util.ArquivosUtil;

@SuppressWarnings("serial")
@ManagedBean(name = "MBCifra")
@ViewScoped
public class CifraBean implements Serializable {

	private Genero genero;
	private ArrayList<Genero> generos;
	private List<Cifra> cifras;
	private ArrayList<Cifra> uploadCifras;
	private Cifra cifraSelecionada;
	private Long idCantor;
		
	private GeneroDAO generoDAO;
	private CifraDAO cifraDAO;
	
	@PostConstruct
	private void init() {
		LoginBean autenticacaoBean = Faces.getSessionAttribute("MBLogin");
		idCantor = autenticacaoBean.getUsuarioLogado().getCantor().getId();
		genero = new Genero();
		generos = new ArrayList<>();
		uploadCifras = new ArrayList<Cifra>();
		cifras = new ArrayList<Cifra>();
		generoDAO = new GeneroDAO();
		cifraDAO = new CifraDAO();
		
		listarGeneros();
	}
	
	public void remover(ActionEvent evento) {
		try {
			cifraDAO.excluir((Cifra) evento.getComponent().getAttributes().get("cifraSelecionada"));
			listarCifras();
			Messages.addGlobalInfo("Cifra removida!");
		}catch(RuntimeException erro) {
			System.err.println("[REMOVER CIFRA]: " + erro.getMessage());
			Messages.addGlobalError("Ocorreu um erro ao remover a Cifra!");
		}
	}

	public void pegarPDF(ActionEvent evento) {
		Cifra cifra = (Cifra) evento.getComponent().getAttributes().get("cifraSelecionada");

		ArquivosUtil.gerarPDF(cifra);

	}
	
	public void listarCifras() {
		try {
			cifras = cifraDAO.listarPorCantor(idCantor);
		}catch(RuntimeException erro) {
			System.err.println("[LISTAR CIFRAS]: " + erro.getMessage());
			Messages.addGlobalError("Ocorreu um erro ao buscar suas Cifras");
		}
	}
	
	public void salvar() {
		try {
			
			if(genero == null) {
				Messages.addGlobalError("Selecione um Gênero!");
				return;
			}else if(genero.getId() == null) {
				Messages.addGlobalError("Selecione um Gênero!");
				return;
			}
			
			for(Cifra c : uploadCifras) {
				if (cifraExistente(c.getNome())) {
					Messages.addGlobalInfo("Cifra já existente!");
					continue;
				}
				
				c.setCantor(new CantorDAO().buscar(idCantor));
				c.setGenero(genero);
				c = cifraDAO.merge(c);
				
				ArquivosUtil.salvarPDF(c);
				Messages.addGlobalInfo("Cifra salva com sucesso!");
			}
			uploadCifras.clear(); 
			
		}catch (RuntimeException erro) {
			System.err.println("[SALVAR CIFRAS]: " + erro);
			Messages.addGlobalError("Ocorreu um erro ao salvar a Cifra");
		} 
	}
	
	private boolean cifraExistente(String nome) {
		return cifraDAO.buscarPeloNome(nome) != null;
	}

	public void upload(FileUploadEvent evento) {
		
		try {
			Cifra cifra = new Cifra();
			
			UploadedFile arquivoUpload = evento.getFile();
			Path arquivoTemp = Files.createTempFile(null, null);
			Files.copy(arquivoUpload.getInputstream(), arquivoTemp, StandardCopyOption.REPLACE_EXISTING);
			cifra.setPdf(arquivoTemp.toString());
			
			String nomeArquivo = arquivoUpload.getFileName();
			cifra.setNome(nomeArquivo);
			uploadCifras.add(cifra);
			Messages.addGlobalInfo("Clique no botão salvar");
		} catch (IOException erro) {
			Messages.addGlobalInfo("Ocorreu um erro ao tentar realizar o upload de arquivo");
			System.err.println("[UPLOAD]: " + erro);
		} 
	}
	
	public void novoGenero() {
		genero = new Genero();
	}
	
	public void salvarGenero() {
		try {
			generoDAO.merge(this.genero);
			listarGeneros();
			Messages.addGlobalInfo("Gênero salvo com sucesso!");
			PrimeFaces.current().executeScript("PF('dlgNovoGenero').hide();");
		}catch(RuntimeException erro) {
			System.err.println("[SALVAR GENERO]: " + erro.getMessage());
			Messages.addGlobalError("Ocorreu um erro ao salvar o Gênero");
		}
	}


	private void listarGeneros() {
		try {
			generos = generoDAO.listar();
		}catch(RuntimeException erro) {
			System.err.println("[LISTAR GENEROS]: " + erro.getMessage());
			Messages.addGlobalError("Ocorreu um erro ao carregar os Gêneros");
		}
	}

	public Cifra getCifraSelecionada() {
		return cifraSelecionada;
	}

	public void setCifraSelecionada(Cifra cifraSelecionada) {
		this.cifraSelecionada = cifraSelecionada;
	}

	public Genero getGenero() {
		return genero;
	}

	public void setGenero(Genero genero) {
		this.genero = genero;
	}

	public List<Genero> getGeneros() {
		return generos;
	}

	public void setGeneros(ArrayList<Genero> generos) {
		this.generos = generos;
	}

	public List<Cifra> getCifras() {
		return cifras;
	}

	public void setCifras(ArrayList<Cifra> cifras) {
		this.cifras = cifras;
	}

}
