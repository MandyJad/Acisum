package br.com.acisum.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import javax.faces.context.FacesContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import br.com.acisum.domain.Cifra;

public class ArquivosUtil {
	
	private static final String CAMINHO = "/Acisum/Uploads/cifras/";
	private static final String CAMINHO_IMG = "/Acisum/Uploads/imagens/cantor_";

	public static void gerarPDF(Cifra cifra) {
		try {
			FacesContext context = FacesContext.getCurrentInstance();
			HttpServletResponse response = (HttpServletResponse) context.getExternalContext().getResponse();

			byte[] arquivo = null;
			File file = new File(CAMINHO + cifra.getId() + ".pdf");
			arquivo = fileToByte(file);

			response.setContentType("application/pdf");
			response.setContentLength(arquivo.length);
			ServletOutputStream outputStream = response.getOutputStream();
			outputStream.write(arquivo, 0, arquivo.length);
			outputStream.flush();
			outputStream.close();
			response.getOutputStream().flush();
			response.getOutputStream().close();
			context.responseComplete();
		} catch (Exception erro) {
			erro.printStackTrace();
		}
		
	}
	
	public static byte[] fileToByte(File imagem) throws Exception {
		FileInputStream fis = new FileInputStream(imagem);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		byte[] buffer = new byte[8192];
		int bytesRead = 0;
		while ((bytesRead = fis.read(buffer, 0, 8192)) != -1) {
			baos.write(buffer, 0, bytesRead);
		}
		fis.close();
		return baos.toByteArray();
	}
	
public static void salvarIMG(String nome, Long idCantor) {
		
		try {
			Path origem = Paths.get(nome);
			
			File diretorio = new File(CAMINHO_IMG);
			
			if (!diretorio.exists()) {
				diretorio.mkdirs();
				System.out.println("[DIRETÓRIO CRIADO] [" + CAMINHO_IMG + "]");
			} else {
				System.out.println("[DIRETÓRIO JÁ EXISTE] [" + CAMINHO_IMG +"]");
			}
			
			Path destino = Paths.get(CAMINHO_IMG + idCantor + ".jpg");
			
			Files.copy(origem, destino, StandardCopyOption.REPLACE_EXISTING);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void salvarPDF(Cifra cifra) {
		
		try {
			Path origem = Paths.get(cifra.getPdf());
			
			File diretorio = new File(CAMINHO);
			
			if (!diretorio.exists()) {
				diretorio.mkdirs();
				System.out.println("[DIRETÓRIO CRIADO] [" + CAMINHO + "]");
			} else {
				System.out.println("[DIRETÓRIO JÁ EXISTE] [" + CAMINHO +"]");
			}
			
			Path destino = Paths.get(CAMINHO + cifra.getId() + ".pdf");
			
			Files.copy(origem, destino, StandardCopyOption.REPLACE_EXISTING);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
}
