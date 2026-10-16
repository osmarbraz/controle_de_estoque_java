package com.estoque.controle;

import com.estoque.TestUtils;
import com.estoque.dao.CidadeDAO;
import com.estoque.modelo.Cidade;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.contains;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

public class CidadeServletTest {

    private CidadeServlet servlet;
    private CidadeDAO mockDAO;
    private HttpServletRequest request;
    private HttpServletResponse response;
    private RequestDispatcher dispatcher;

    @BeforeEach
    void setUp() {
        servlet = new CidadeServlet();
        mockDAO = mock(CidadeDAO.class);
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        dispatcher = mock(RequestDispatcher.class);

        when(request.getRequestDispatcher(anyString())).thenReturn(dispatcher);
        when(request.getContextPath()).thenReturn("/controle-estoque");

        TestUtils.setField(servlet, "cidadeDAO", mockDAO);
    }

    @Test
    void testDoGetListarPadrao() throws ServletException, IOException, SQLException {
        when(request.getParameter("acao")).thenReturn(null);
        when(mockDAO.buscarTodos()).thenReturn(Collections.singletonList(new Cidade(1, "SP", "SP")));

        servlet.doGet(request, response);

        verify(request).setCharacterEncoding("UTF-8");
        verify(mockDAO).buscarTodos();
        verify(request).setAttribute(eq("cidades"), any());
        verify(request).getRequestDispatcher("/cidade/list.jsp");
        verify(dispatcher).forward(request, response);
    }

    @Test
    void testDoGetAcaoNovo() throws ServletException, IOException {
        when(request.getParameter("acao")).thenReturn("novo");

        servlet.doGet(request, response);

        verify(request).setAttribute(eq("cidade"), any(Cidade.class));
        verify(request).getRequestDispatcher("/cidade/form.jsp");
        verify(dispatcher).forward(request, response);
    }

    @Test
    void testDoGetEditarEncontrado() throws ServletException, IOException, SQLException {
        when(request.getParameter("acao")).thenReturn("editar");
        when(request.getParameter("id")).thenReturn("3");
        Cidade c = new Cidade(3, "Campinas", "SP");
        when(mockDAO.buscarPorId(3)).thenReturn(c);

        servlet.doGet(request, response);

        verify(request).setAttribute("cidade", c);
        verify(request).getRequestDispatcher("/cidade/form.jsp");
        verify(dispatcher).forward(request, response);
    }

    @Test
    void testDoGetEditarNaoEncontrado() throws ServletException, IOException, SQLException {
        when(request.getParameter("acao")).thenReturn("editar");
        when(request.getParameter("id")).thenReturn("99");
        when(mockDAO.buscarPorId(99)).thenReturn(null);

        servlet.doGet(request, response);

        verify(response).sendRedirect("/controle-estoque/servlet/cidades?msg=nao_encontrado");
    }

    @Test
    void testDoGetExcluirSucesso() throws ServletException, IOException, SQLException {
        when(request.getParameter("acao")).thenReturn("excluir");
        when(request.getParameter("id")).thenReturn("4");
        when(mockDAO.excluir(4)).thenReturn(true);

        servlet.doGet(request, response);

        verify(mockDAO).excluir(4);
        verify(response).sendRedirect("/controle-estoque/servlet/cidades?msg=sucesso_excluir");
    }

    @Test
    void testDoGetExcluirComErroVinculo() throws ServletException, IOException, SQLException {
        when(request.getParameter("acao")).thenReturn("excluir");
        when(request.getParameter("id")).thenReturn("4");
        doThrow(new RuntimeException("FK Error")).when(mockDAO).excluir(4);

        servlet.doGet(request, response);

        verify(mockDAO).excluir(4);
        verify(response).sendRedirect("/controle-estoque/servlet/cidades?msg=erro_excluir_vinculo");
    }

    @Test
    void testDoGetExceptionLancaServletException() throws SQLException {
        when(request.getParameter("acao")).thenReturn("listar");
        when(mockDAO.buscarTodos()).thenThrow(new SQLException("DB error"));

        ServletException ex = assertThrows(ServletException.class, () -> servlet.doGet(request, response));
        assertTrue(ex.getMessage().contains("Erro ao processar requisição em Cidades"));
    }

    @Test
    void testDoPostInserirSucesso() throws ServletException, IOException, SQLException {
        when(request.getParameter("cidadeId")).thenReturn(null);
        when(request.getParameter("nome")).thenReturn(" Curitiba ");
        when(request.getParameter("uf")).thenReturn(" pr ");

        servlet.doPost(request, response);

        verify(mockDAO).inserir(argThat(c -> "Curitiba".equals(c.getNome()) && "PR".equals(c.getUf()) && c.getCidadeId() == null));
        verify(response).sendRedirect("/controle-estoque/servlet/cidades?msg=sucesso_salvar");
    }

    @Test
    void testDoPostInserirCamposNull() throws ServletException, IOException, SQLException {
        when(request.getParameter("cidadeId")).thenReturn("");
        when(request.getParameter("nome")).thenReturn(null);
        when(request.getParameter("uf")).thenReturn(null);

        servlet.doPost(request, response);

        verify(mockDAO).inserir(argThat(c -> "".equals(c.getNome()) && "".equals(c.getUf())));
        verify(response).sendRedirect("/controle-estoque/servlet/cidades?msg=sucesso_salvar");
    }

    @Test
    void testDoPostAtualizarSucesso() throws ServletException, IOException, SQLException {
        when(request.getParameter("cidadeId")).thenReturn("2");
        when(request.getParameter("nome")).thenReturn("Santos");
        when(request.getParameter("uf")).thenReturn("SP");

        servlet.doPost(request, response);

        verify(mockDAO).atualizar(argThat(c -> "Santos".equals(c.getNome()) && "SP".equals(c.getUf()) && Integer.valueOf(2).equals(c.getCidadeId())));
        verify(response).sendRedirect("/controle-estoque/servlet/cidades?msg=sucesso_salvar");
    }

    @Test
    void testDoPostComErroComId() throws ServletException, IOException, SQLException {
        when(request.getParameter("cidadeId")).thenReturn("5");
        when(request.getParameter("nome")).thenReturn("Erro");
        when(request.getParameter("uf")).thenReturn("ER");
        doThrow(new SQLException("Erro")).when(mockDAO).atualizar(any());

        servlet.doPost(request, response);

        verify(request).setAttribute(eq("erro"), contains("Erro ao salvar cidade"));
        verify(request).setAttribute(eq("cidade"), argThat(c -> ((Cidade) c).getCidadeId() == 5));
        verify(request).getRequestDispatcher("/cidade/form.jsp");
        verify(dispatcher).forward(request, response);
    }

    @Test
    void testDoPostComErroSemId() throws ServletException, IOException, SQLException {
        when(request.getParameter("cidadeId")).thenReturn("");
        when(request.getParameter("nome")).thenReturn("Nova Cidade");
        when(request.getParameter("uf")).thenReturn("NC");
        doThrow(new RuntimeException("Erro")).when(mockDAO).inserir(any());

        servlet.doPost(request, response);

        verify(request).setAttribute(eq("erro"), contains("Erro ao salvar cidade"));
        verify(request).setAttribute(eq("cidade"), argThat(c -> ((Cidade) c).getCidadeId() == null));
        verify(request).getRequestDispatcher("/cidade/form.jsp");
        verify(dispatcher).forward(request, response);
    }
}
