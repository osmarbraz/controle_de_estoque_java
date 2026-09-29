package com.estoque.web;

import com.estoque.configuracao.BancoDeDadosInicializador;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.util.logging.Logger;

@WebListener
/**
 * Classe responsável por gerenciar a lógica de AppContextListener.
 */
public class AppContextListener implements ServletContextListener {

    private static final Logger LOGGER = Logger.getLogger(AppContextListener.class.getName());

    @Override
    /**
     * Executa a operação de contextInitialized.
     * 
     * @param sce Parâmetro necessário para a operação
     */
    public void contextInitialized(ServletContextEvent sce) {
        LOGGER.info("Inicializando Aplicacao Controle de Estoque...");
        try {
            BancoDeDadosInicializador.inicializarSeNecessario();
        } catch (Exception e) {
            LOGGER.severe("Falha ao inicializar o banco de dados na inicializacao da app: " + e.getMessage());
        }
    }

    @Override
    /**
     * Executa a operação de contextDestroyed.
     * 
     * @param sce Parâmetro necessário para a operação
     */
    public void contextDestroyed(ServletContextEvent sce) {
        LOGGER.info("Finalizando Aplicacao Controle de Estoque...");
    }
}
