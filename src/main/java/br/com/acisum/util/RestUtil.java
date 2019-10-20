package br.com.acisum.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class RestUtil {

	public static String consumeService(String endereco, String metodo, String parametros) {
		try {

			// Incializa a URL do Servi�o
			URL url = new URL(endereco);

			// Se conecta com a URL do Servi�o
			HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();

			// Escolhe o M�todo de Comunica��o (GET,POST...)
			conn.setRequestMethod(metodo);

			// sinaliza que esta conex�o deve enviar dados
			conn.setDoOutput(true);

			// Define o tipo de dados que ir�o trafegar
			conn.setRequestProperty("Accept", "application/json");

			// Recupera o Stream de Sa�da da Requisi��o, respons�vel por enviar os
			// par�metros de Entrada do Servi�o
			OutputStream os = conn.getOutputStream();
			// Escreve os dados no endere�o do servi�o
			os.write(parametros.getBytes());
			// Garante que os dados foram Escritos
			os.flush();

			// Verifica se o c�digo de resposta � difernte de 200 (Sucesso!), nesse caso
			// notifica um erro
			if (conn.getResponseCode() != 200) {
				throw new RuntimeException("Failed : HTTP Error code : " + conn.getResponseCode());
			}

			// Recupera o Stream de Entrada de Requisi��o, respons�vel por receber os
			// par�metros de Sa�da do Servi�o
			InputStreamReader in = new InputStreamReader(conn.getInputStream());
			BufferedReader br = new BufferedReader(in);

			// L� Os dados de resposta, enquanto estiverem dispon�veis
			String output = "";
			String aux;
			while ((aux = br.readLine()) != null) {
				if (aux != null)
					output = output.concat(aux);
			}
			// Encerra a conex�o com o Servi�o
			conn.disconnect();

			// Retorna os Dados como uma String, substituindo caracteres utilizados no transporte dos dados
			return output.replace("\\", "").replace("\"", "");
		} catch (Exception e) {
			System.out.println("Exception in NetClientGet:- " + e);
			return "";
		}
	}
	
	public static String consumeService(String endereco, String metodo, String parametros,String chaveSeguranca) {
		try {

			// Incializa a URL do Servi�o
			URL url = new URL(endereco);

			// Se conecta com a URL do Servi�o
			HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();

			// Escolhe o M�todo de Comunica��o (GET,POST...)
			conn.setRequestMethod(metodo);

			// sinaliza que esta conex�o deve enviar dados
			conn.setDoOutput(true);

			// Define o tipo de dados que ir�o trafegar
			conn.setRequestProperty("Accept", "application/json");
			
			conn.setRequestProperty("X-API-KEY", chaveSeguranca);

			// Recupera o Stream de Sa�da da Requisi��o, respons�vel por enviar os
			// par�metros de Entrada do Servi�o
			OutputStream os = conn.getOutputStream();
			// Escreve os dados no endere�o do servi�o
			os.write(parametros.getBytes());
			// Garante que os dados foram Escritos
			os.flush();

			// Verifica se o c�digo de resposta � difernte de 200 (Sucesso!), nesse caso
			// notifica um erro
			if (conn.getResponseCode() != 200) {
				throw new RuntimeException("Failed : HTTP Error code : " + conn.getResponseCode());
			}

			// Recupera o Stream de Entrada de Requisi��o, respons�vel por receber os
			// par�metros de Sa�da do Servi�o
			InputStreamReader in = new InputStreamReader(conn.getInputStream());
			BufferedReader br = new BufferedReader(in);

			// L� Os dados de resposta, enquanto estiverem dispon�veis
			String output = "";
			String aux;
			while ((aux = br.readLine()) != null) {
				if (aux != null)
					output = output.concat(aux);
			}
			// Encerra a conex�o com o Servi�o
			conn.disconnect();

			// Retorna os Dados como uma String, substituindo caracteres utilizados no transporte dos dados
			return output.replace("\\", "").replace("\"", "");
		} catch (Exception e) {
			System.out.println("Exception in NetClientGet:- " + e);
			return "";
		}

	}
	
	public static String consumeServiceBackforApp(String endereco, String metodo, String parametros) {
		try {

			// Incializa a URL do Servi�o
			URL url = new URL(endereco);

			// Se conecta com a URL do Servi�o
			HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();

			// Escolhe o M�todo de Comunica��o (GET,POST...)
			conn.setRequestMethod(metodo);

			// sinaliza que esta conex�o deve enviar dados
			conn.setDoOutput(true);

			// Define o tipo de dados que ir�o trafegar
			conn.setRequestProperty("Accept", "application/json");
			
			conn.setRequestProperty("X-Parse-Application-Id", "paVyy6SFoCDBTQlpXqNhFFwt5CxvDeEcHyCSCAbF");
			
			conn.setRequestProperty("X-Parse-REST-API-Key", "yaJAZbi5NhLOenTZZTtjS7KwKoUXIUk6fDA66QbL");
			
			conn.setRequestProperty("X-Parse-Revocable-Session", "1");

			// Recupera o Stream de Sa�da da Requisi��o, respons�vel por enviar os
			// par�metros de Entrada do Servi�o
			OutputStream os = conn.getOutputStream();
			// Escreve os dados no endere�o do servi�o
			os.write(parametros.getBytes());
			// Garante que os dados foram Escritos
			os.flush();

			// Verifica se o c�digo de resposta � difernte de 200 (Sucesso!), nesse caso
			// notifica um erro
			if (conn.getResponseCode() != 200) {
				throw new RuntimeException("Failed : HTTP Error code : " + conn.getResponseCode());
			}

			// Recupera o Stream de Entrada de Requisi��o, respons�vel por receber os
			// par�metros de Sa�da do Servi�o
			InputStreamReader in = new InputStreamReader(conn.getInputStream());
			BufferedReader br = new BufferedReader(in);

			// L� Os dados de resposta, enquanto estiverem dispon�veis
			String output = "";
			String aux;
			while ((aux = br.readLine()) != null) {
				if (aux != null)
					output = output.concat(aux);
			}
			// Encerra a conex�o com o Servi�o
			conn.disconnect();

			// Retorna os Dados como uma String, substituindo caracteres utilizados no transporte dos dados
			return output.replace("\\", "").replace("\"", "");
		} catch (Exception e) {
			System.out.println("Exception in NetClientGet:- " + e);
			return "";
		}
	}
}
