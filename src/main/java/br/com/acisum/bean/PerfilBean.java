package br.com.acisum.bean;

import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.omnifaces.util.Faces;
import org.omnifaces.util.Messages;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;

import br.com.acisum.dao.UsuarioDAO;
import br.com.acisum.domain.Cantor;
import br.com.acisum.domain.Usuario;
import br.com.acisum.util.ArquivosUtil;

@SuppressWarnings("serial")
@ManagedBean(name = "MBPerfil")
@ViewScoped
public class PerfilBean implements Serializable {

	private Usuario usuario;
	private Cantor cantor;
	private UsuarioDAO usuarioDAO;
	private String arquivo = "";
	private String senha;
	private String senhaAtual;
	private Usuario usuarioLogado = null;

	@PostConstruct
	public void init() {
		LoginBean autenticacaoBean = Faces.getSessionAttribute("MBLogin");
		usuario = autenticacaoBean.getUsuarioLogado();
		usuarioDAO = new UsuarioDAO();
	}

	public void salvar() {
		System.out.println("Nome: " + usuario.getCantor().getNome());
		try {
			if (senhaAtual == null || senhaAtual.isEmpty()) {
				Messages.addGlobalInfo("Entre com uma senha válida!");
				return;
			}
			if (!senhaAtual.equals(usuario.getSenha())) {
				Messages.addGlobalInfo("Senha atual diferente do cadastro!");
				return;
			}
			if (senha != null && !senha.isEmpty()) {
				usuario.setSenha(senha);
			}
			usuario = usuarioDAO.salvar(usuario);
			ArquivosUtil.salvarIMG(arquivo, usuario.getCantor().getId());
			Messages.addGlobalInfo("Usuário salvo com sucesso!");
			this.refresh();
		} catch (RuntimeException erro) {
			System.err.println("[SALVAR USUARIO]: " + erro);
			Messages.addGlobalError("Ocorreu um erro, tente novamente mais tarde.");
		}
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

	
	public void excluir(){
		try {
		usuarioDAO = new UsuarioDAO();
		cantor = new Cantor();
		
		usuarioDAO.excluir(usuario);
		this.usuarioLogado = null;
		Faces.redirect("pages/home.xhtml");
		
		} catch (Exception e) {
			System.out.println("Erro ao excluir usuário: " +e);
			Messages.addFlashGlobalError("Erro ao excluir usuário!");
		}
		
		
	}
	
	
	public void refresh() {
		FacesContext context = FacesContext.getCurrentInstance();
		Application application = context.getApplication();
		ViewHandler viewHandler = application.getViewHandler();
		UIViewRoot viewRoot = viewHandler.createView(context, context.getViewRoot().getViewId());
		context.setViewRoot(viewRoot);
		context.renderResponse();
	}
	
	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
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

	public boolean isUsuarioSessao() {
		return usuarioLogado == null ? false : true;
	}
	
	public void setUsuarioLogado(Usuario usuarioLogado) {
		this.usuarioLogado = usuarioLogado;
	}

	public String getSenhaAtual() {
		return senhaAtual;
	}

	public void setSenhaAtual(String senhaAtual) {
		this.senhaAtual = senhaAtual;
	}
}