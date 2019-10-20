package br.com.acisum.bean;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

@ManagedBean
@RequestScoped
public class ImagemBean {

	@ManagedProperty("#{param.caminho}")
	private Long id;
	
	private StreamedContent foto;
	
	public StreamedContent getFoto() throws IOException {
		if(id == null){
			ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext(); 
			String diretorio = ec.getRealPath("./img/cantor_em_branco.jpg");
			Path path = Paths.get(diretorio);
			InputStream stream = Files.newInputStream(path);
			foto = new DefaultStreamedContent(stream);
		}else {
			Path path = Paths.get("C:/Acisum/Uploads/imagens/cantor_"+id+".jpg");
			InputStream stream = null;
			try {
				stream = Files.newInputStream(path);
			} catch (Exception e) {
				ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext(); 
				String diretorio = ec.getRealPath("./img/cantor_em_branco.jpg");
				path = Paths.get(diretorio);
				stream = Files.newInputStream(path);
				foto = new DefaultStreamedContent(stream);
			}
			foto = new DefaultStreamedContent(stream);
		}
		return foto;
	}
	
	public void setFoto(StreamedContent foto) {
		this.foto = foto;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
}
