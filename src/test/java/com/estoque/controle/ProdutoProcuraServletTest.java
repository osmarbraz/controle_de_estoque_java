package com.estoque.controle;

import com.estoque.TestUtils;
import com.estoque.dao.ProdutoDAO;
import com.estoque.modelo.Produto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.SQLException;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ProdutoProcuraServletTest {

    private ProdutoProcuraServlet servlet;
    private ProdutoDAO mockProdutoDAO;
    private HttpServletRequest request;
    private HttpServletResponse response;
    private StringWriter stringWriter;

    @BeforeEach
    void setUp() throws IOException {
        servlet = new ProdutoProcuraServlet();
        mockProdutoDAO = mock(ProdutoDAO.class);
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);

        stringWriter = new StringWriter();
        when(response.getWriter()).thenAnswer(inv -> new PrintWriter(stringWriter));

        TestUtils.setField(servlet, "produtoDAO", mockProdutoDAO);
    }

    @Test
    void testDoGetSucesso() throws ServletException, IOException, SQLException {
        when(request.getParameter("q")).thenReturn("teclado");
        Produto p = new Produto();
        p.setProdutoId(1);
        p.setDescricao("Teclado Mecânico");
        when(mockProdutoDAO.buscarPorDescricao("teclado")).thenReturn(Collections.singletonList(p));

        servlet.doGet(request, response);

        verify(request).setCharacterEncoding("UTF-8");
        verify(response).setContentType("application/json; charset=UTF-8");
        String json = stringWriter.toString();
        assertTrue(json.contains("Teclado Mecânico"));
    }

    @Test
    void testDoGetException() throws ServletException, IOException, SQLException {
        when(request.getParameter("q")).thenReturn("erro");
        when(mockProdutoDAO.buscarPorDescricao("erro")).thenThrow(new SQLException("Erro no banco"));

        servlet.doGet(request, response);

        verify(response).setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        String json = stringWriter.toString();
        assertTrue(json.contains("error"));
        assertTrue(json.contains("Erro no banco"));
    }
}
