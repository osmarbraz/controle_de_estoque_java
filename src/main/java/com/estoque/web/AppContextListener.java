package com.estoque.web;

import com.estoque.config.DatabaseInitializer;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.util.logging.Logger;

@WebListener
public class AppContextListener implements ServletContextListener {

    private static final Logger LOGGER = Logger.getLogger(AppContextListener.class.getName());

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        LOGGER.info("Inicializando Aplicacao Controle de Estoque...");
        try {
            DatabaseInitializer.initializeIfNeeded();
        } catch (Exception e) {
            LOGGER.severe("Falha ao inicializar o banco de dados na inicializacao da app: " + e.getMessage());
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        LOGGER.info("Finalizando Aplicacao Controle de Estoque...");
    }
}
