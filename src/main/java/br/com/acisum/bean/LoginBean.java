package br.com.acisum.bean;

import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import org.omnifaces.util.Faces;
import org.omnifaces.util.Messages;
import org.primefaces.PrimeFaces;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;

import br.com.acisum.dao.UsuarioDAO;
import br.com.acisum.domain.Cantor;
import br.com.acisum.domain.Usuario;
import br.com.acisum.util.ArquivosUtil;

@SuppressWarnings("serial")
@ManagedBean(name = "MBLogin")
@SessionScoped
public class LoginBean implements Serializable {

	private Usuario usuario;
	private Cantor cantor;
	private Usuario usuarioLogado = null;
	UsuarioDAO usuarioDAO;
	String arquivo = "";

	@PostConstruct
	public void iniciar() {
		usuario = new Usuario();
		usuario.setCantor(new Cantor());
		
		usuarioDAO = new UsuarioDAO();
	}
	
	public void upload(FileUploadEvent evento) {
		
		try {	
			UploadedFile arquivoUpload = evento.getFile();
			Path arquivoTemp = Files.createTempFile(null, null);
			Files.copy(arquivoUpload.getInputstream(), arquivoTemp, StandardCopyOption.REPLACE_EXISTING);
			arquivo = arquivoTemp.toString();
			Messages.addGlobalInfo("Upload realizado com sucesso");
		} catch (IOException erro) {
			Messages.addGlobalInfo("Ocorreu um erro ao tentar realizar o upload de arquivo");
			System.err.println("[UPLOAD]: " + erro);
		} 
	}
	
	public void salvar() {
		try {
			usuario.setCantor(cantor);
			Usuario user = usuarioDAO.salvar(usuario);
			ArquivosUtil.salvarIMG(arquivo, user.getCantor().getId());
			Messages.addGlobalInfo("Salvo com sucesso!");
			PrimeFaces.current().executeScript("PF('dlgNovo').hide();");
		} catch (RuntimeException erro) {
			System.err.println("[SALVAR NOVO USUARIO]: " + erro);
			Messages.addGlobalError("Ocorreu um erro, tente novamente mais tarde.");
		}
	}
	
	public void novo() {
		usuario = new Usuario();
		cantor = new Cantor();
	}

	public void logar() {
		try {

			Usuario user = usuarioDAO.autenticar(usuario);

			if (user == null) {
				Messages.addGlobalWarn("Email e/ou senha invalido!");
				return;
			} 

			this.usuarioLogado = user;
			System.out.println("Usuario Logado: " + this.usuario.getEmail());
			Faces.redirect("pages/cifras.xhtml");
		}catch(RuntimeException | IOException erro) {
			System.err.println("[AUTENTICAR]: " + erro.getMessage());
			Messages.addGlobalError("Ocorreu um erro, tente novamente mais tarde.");
		}
	}
	
	
	public void logout() {
		try {
			this.usuarioLogado = null;
			Faces.redirect("pages/home.xhtml");
		} catch (IOException erro) {
			System.err.println("Erro ao deslogar: " + erro);
		}
	}

	public Cantor getCantor() {
		return cantor;
	}

	public void setCantor(Cantor cantor) {
		this.cantor = cantor;
	}

	public Usuario getUsuarioLogado() {
		return usuarioLogado;
	}

	public void setUsuarioLogado(Usuario usuarioLogado) {
		this.usuarioLogado = usuarioLogado;
	}

	public boolean isUsuarioSessao() {
		return usuarioLogado == null ? false : true;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

}
