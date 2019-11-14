package br.com.acisum.dao;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;

import br.com.acisum.domain.Cantor;
import br.com.acisum.domain.Usuario;
import br.com.acisum.util.HibernateUtil;

@SuppressWarnings("serial")
public class UsuarioDAO extends GenericDAO<Usuario> {
	
	public Usuario autenticar(Usuario usuario) throws RuntimeException {
		Session sessao = HibernateUtil.getFabricaDeSessoes().openSession();
		try {

			Criteria consulta = sessao.createCriteria(Usuario.class);
			consulta.add(Restrictions.eq("email", usuario.getEmail()));
			consulta.add(Restrictions.eq("senha", usuario.getSenha()));
			return (Usuario) consulta.uniqueResult();

		} catch (RuntimeException exception) {
			throw exception;
		} finally {
			sessao.close();
		}
	}

	public Usuario salvar(Usuario usuario) {
		Session sessao = HibernateUtil.getFabricaDeSessoes().openSession();
		Transaction transacao = null;
		Usuario user = null;
		try {
			transacao = sessao.beginTransaction();
			Cantor cantor = new Cantor();
			cantor = (Cantor) sessao.merge(usuario.getCantor());
			usuario.setCantor(cantor);
			user = (Usuario) sessao.merge(usuario);
			transacao.commit();
		} catch (RuntimeException erro) {
			if (transacao != null) {
				transacao.rollback();
			}
			throw erro;
		} finally {
			sessao.close();
		}
		return user;
	}
	
	public Usuario buscarEmail(String email) throws RuntimeException {

		Session sessao = HibernateUtil.getFabricaDeSessoes().openSession();
		try {
			Criteria consulta = sessao.createCriteria(Usuario.class);
			consulta.add(Restrictions.eq("email", email));
			Usuario resultado = (Usuario) consulta.uniqueResult();
			return resultado;

		} catch (RuntimeException exception) {
			throw exception;
		} finally {
			sessao.close();
		}
	}
}
