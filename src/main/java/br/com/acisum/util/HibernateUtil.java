package br.com.acisum.util;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class HibernateUtil {

    private static final SessionFactory fabricaDeSessoes = criarFabricaDeSessoes();
    
    public static SessionFactory getFabricaDeSessoes() {
		return fabricaDeSessoes;
	}

    private static SessionFactory criarFabricaDeSessoes() {
        try {
            // Create the SessionFactory from hibernate.cfg.xml
            return new Configuration().configure().buildSessionFactory();
        }
        catch (Throwable ex) {
            // Make sure you log the exception, as it might be swallowed
            System.err.println("A fabrica de sessões não pode ser criada ." + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    public static SessionFactory getSessionFactory() {
        return fabricaDeSessoes;
    }

}