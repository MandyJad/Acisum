package br.com.acisum.util;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class HibernateContexto implements ServletContextListener{

	public void contextDestroyed(ServletContextEvent arg0) {
		HibernateUtil.getSessionFactory().close();
	}

	public void contextInitialized(ServletContextEvent arg0) {
		HibernateUtil.getSessionFactory();
	}
	
}
