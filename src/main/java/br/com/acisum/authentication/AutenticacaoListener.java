package br.com.acisum.authentication;

import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;

import org.omnifaces.util.Faces;

import br.com.acisum.bean.LoginBean;
import br.com.acisum.domain.Usuario;

@SuppressWarnings("serial")
public class AutenticacaoListener implements PhaseListener {

	@Override
	public void afterPhase(PhaseEvent event) {
		if (!ehPaginaPublica()) {
			
			LoginBean autenticacaoBean = Faces.getSessionAttribute("MBLogin");

			if (autenticacaoBean == null) {
				Faces.navigate("/pages/home.xhtml");
				return;
			}

			Usuario userLogado = autenticacaoBean.getUsuarioLogado();
			
			if (userLogado == null) {
				Faces.navigate("/pages/home.xhtml");
				return;
			}
		}
	}

	@Override
	public void beforePhase(PhaseEvent event) {
		
	}

	@Override
	public PhaseId getPhaseId() {
		return PhaseId.ANY_PHASE;
	}
	
	private boolean ehPaginaPublica() {
		String paginaAtual = Faces.getViewId();

		if (paginaAtual.contains("home.xhtml")) {
			return true;
		}

		return false;
	}

}
