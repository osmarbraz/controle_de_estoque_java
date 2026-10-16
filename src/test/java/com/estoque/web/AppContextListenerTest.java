package com.estoque.web;

import com.estoque.configuracao.BancoDeDadosInicializador;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import javax.servlet.ServletContextEvent;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.*;

public class AppContextListenerTest {

    @Test
    void testContextInitializedSucesso() {
        AppContextListener listener = new AppContextListener();
        ServletContextEvent sce = mock(ServletContextEvent.class);

        try (MockedStatic<BancoDeDadosInicializador> mockedInit = mockStatic(BancoDeDadosInicializador.class)) {
            mockedInit.when(BancoDeDadosInicializador::inicializarSeNecessario).thenAnswer(inv -> null);

            assertDoesNotThrow(() -> listener.contextInitialized(sce));
            mockedInit.verify(BancoDeDadosInicializador::inicializarSeNecessario);
        }
    }

    @Test
    void testContextInitializedComException() {
        AppContextListener listener = new AppContextListener();
        ServletContextEvent sce = mock(ServletContextEvent.class);

        try (MockedStatic<BancoDeDadosInicializador> mockedInit = mockStatic(BancoDeDadosInicializador.class)) {
            mockedInit.when(BancoDeDadosInicializador::inicializarSeNecessario)
                    .thenThrow(new RuntimeException("Simulated DB Init Failure"));

            assertDoesNotThrow(() -> listener.contextInitialized(sce));
        }
    }

    @Test
    void testContextDestroyed() {
        AppContextListener listener = new AppContextListener();
        ServletContextEvent sce = mock(ServletContextEvent.class);

        assertDoesNotThrow(() -> listener.contextDestroyed(sce));
    }
}
