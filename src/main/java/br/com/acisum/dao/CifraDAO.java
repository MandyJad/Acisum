package br.com.acisum.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import br.com.acisum.domain.Cifra;
import br.com.acisum.domain.ItemPlaylist;
import br.com.acisum.domain.ItemPlaylistIndicada;
import br.com.acisum.util.HibernateUtil;

@SuppressWarnings("serial")
public class CifraDAO extends GenericDAO<Cifra> {

	@SuppressWarnings("unchecked")
	public List<Cifra> listaPorGenero(Long generoid) throws RuntimeException {
		
		Session sessao = HibernateUtil.getFabricaDeSessoes().openSession();
		try {
			Criteria consulta = sessao.createCriteria(Cifra.class);
			consulta.add(Restrictions.eq("genero.id", generoid ));
			consulta.addOrder(Order.asc("nome"));
			List<Cifra> resultado = consulta.list();
			return resultado;
		} catch (RuntimeException erro) {
			throw erro;
		} finally {
			sessao.close();
		}
		
	}

	@SuppressWarnings("unchecked")
	public List<Cifra> listarPorCantor(Long id) throws RuntimeException {
		Session sessao = HibernateUtil.getFabricaDeSessoes().openSession();
		try {
			Criteria consulta = sessao.createCriteria(Cifra.class);
			consulta.createAlias("cantor", "c");
			consulta.add(Restrictions.eq("c.id", id));
			consulta.addOrder(Order.asc("nome"));
			List<Cifra> resultado = consulta.list();
			return resultado;
		} catch (RuntimeException erro) {
			throw erro;
		} finally {
			sessao.close();
		}
	}
	

	@SuppressWarnings("unchecked")
	public List<Cifra> listarPorCantorGenero(Long idCantor, Long idGenero) throws RuntimeException {
		Session sessao = HibernateUtil.getFabricaDeSessoes().openSession();
		try {
			Criteria consulta = sessao.createCriteria(Cifra.class);
			consulta.createAlias("cantor", "c");
			consulta.add(Restrictions.eq("c.id", idCantor));
			consulta.createAlias("genero", "g");
			consulta.add(Restrictions.eq("g.id", idGenero));
			consulta.addOrder(Order.asc("nome"));
			return consulta.list();
		} catch (RuntimeException erro) {
			throw erro;
		} finally {
			sessao.close();
		}
	}	
	
	@SuppressWarnings("unchecked")
	public List<Cifra> listarCifrasPorPlaylist(Long idPlaylist) throws RuntimeException {
		Session sessao = HibernateUtil.getFabricaDeSessoes().openSession();
		try {
			
			Criteria consulta = sessao.createCriteria(ItemPlaylist.class);
			consulta.createAlias("playlist", "p");
			consulta.add(Restrictions.eq("p.id", idPlaylist));
			
			List<ItemPlaylist> itens = new ArrayList<>();
			List<Cifra> cifras = new ArrayList<>();
			itens = consulta.list();
			for (ItemPlaylist item : itens) {
				cifras.add(item.getCifra());
			}
			return cifras;
		} catch (RuntimeException erro) {
			throw erro;
		} finally {
			sessao.close();
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<ItemPlaylistIndicada> listarCifrasIndicadasPorPlaylist(Long idPlaylist) throws RuntimeException {
		Session sessao = HibernateUtil.getFabricaDeSessoes().openSession();
		try {
			
			Criteria consulta = sessao.createCriteria(ItemPlaylistIndicada.class);
			consulta.createAlias("playlist", "p");
			consulta.add(Restrictions.eq("p.id", idPlaylist));
			consulta.addOrder(Order.desc("pedidos"));
			
			List<ItemPlaylistIndicada> itens = new ArrayList<>();
			itens = consulta.list();
			
			return itens;
		} catch (RuntimeException erro) {
			throw erro;
		} finally {
			sessao.close();
		}
	}

	public Cifra buscarPeloNome(String nome) {
		Session sessao = HibernateUtil.getFabricaDeSessoes().openSession();
		try {
			
			Criteria consulta = sessao.createCriteria(Cifra.class);
			consulta.add(Restrictions.eq("nome", nome));
			
			return (Cifra) consulta.uniqueResult();
		} catch (RuntimeException erro) {
			throw erro;
		} finally {
			sessao.close();
		}
	}
	
	
}
