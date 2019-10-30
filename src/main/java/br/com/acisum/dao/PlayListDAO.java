package br.com.acisum.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projection;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import br.com.acisum.domain.ItemPlaylist;
import br.com.acisum.domain.ItemPlaylistIndicada;
import br.com.acisum.domain.Playlist;
import br.com.acisum.util.HibernateUtil;

@SuppressWarnings("serial")
public class PlayListDAO extends GenericDAO<Playlist> {

	public Playlist salvarPlayList(Playlist playList, List<ItemPlaylist> itens) throws RuntimeException {
		Session sessao = HibernateUtil.getFabricaDeSessoes().openSession();
		Transaction transacao = null;

		try {
			transacao = sessao.beginTransaction();
//			Playlist retorno = (Playlist) sessao.merge(playList);
			
			for(ItemPlaylist item : itens) {
				item.setPlaylist(playList);
				sessao.merge(item);
			}
			
			transacao.commit();
			return playList;
		} catch (RuntimeException erro) {
			if (transacao != null) {
				transacao.rollback();
			}
			throw erro;
		} finally {
			sessao.close();
		}
	}

	@SuppressWarnings("unchecked")
	public List<Playlist> listarPorCantor(Long idCantor) throws RuntimeException {
		Session sessao = HibernateUtil.getFabricaDeSessoes().openSession();
		try {

			Criteria consulta = sessao.createCriteria(Playlist.class);
			consulta.createAlias("cantor", "c");
			consulta.add(Restrictions.eq("c.id", idCantor));
			consulta.addOrder(Order.desc("id"));
			List<Playlist> resultado = consulta.list();
			return resultado;

		} catch (RuntimeException exception) {
			throw exception;
		} finally {
			sessao.close();
		}
	}

	@SuppressWarnings("unchecked")
	public List<Playlist> listarTodasPlaylists() throws RuntimeException {
		Session sessao = HibernateUtil.getFabricaDeSessoes().openSession();
		try {

			Criteria consulta = sessao.createCriteria(Playlist.class);
			consulta.addOrder(Order.desc("id"));
			List<Playlist> resultado = consulta.list();
			return resultado;

		} catch (RuntimeException exception) {
			throw exception;
		} finally {
			sessao.close();
		}
	}

	@SuppressWarnings("unchecked")
	public Playlist buscar(Long idCantor) {
		Session sessao = HibernateUtil.getFabricaDeSessoes().openSession();
		try {

			Criteria consulta = sessao.createCriteria(Playlist.class);
			consulta.createAlias("cantor", "c");
			consulta.add(Restrictions.eq("c.id", idCantor));
			consulta.addOrder(Order.desc("id"));
			List<Playlist> resultado = consulta.list();
			return resultado.isEmpty() ? null : resultado.get(0);

		} catch (RuntimeException exception) {
			throw exception;
		} finally {
			sessao.close();
		}
	}

	public void salvarPlayListIndicada(List<ItemPlaylistIndicada> lista) {
		Session sessao = HibernateUtil.getFabricaDeSessoes().openSession();
		Transaction transacao = null;

		try {
			transacao = sessao.beginTransaction();
			
			for(ItemPlaylistIndicada item : lista) {
				sessao.merge(item);
			}
			
			transacao.commit();
		} catch (RuntimeException erro) {
			if (transacao != null) {
				transacao.rollback();
			}
			throw erro;
		} finally {
			sessao.close();
		}
	}

	@SuppressWarnings("unchecked")
	public Playlist buscarPorId(Long playlistId) {
		Session sessao = HibernateUtil.getFabricaDeSessoes().openSession();
		try {

			Criteria consulta = sessao.createCriteria(Playlist.class);
			consulta.add(Restrictions.eq("id", playlistId));
			List<Playlist> resultado = consulta.list();
			return resultado.isEmpty() ? null : resultado.get(0);

		} catch (RuntimeException exception) {
			throw exception;
		} finally {
			sessao.close();
		}
	}
	
}
