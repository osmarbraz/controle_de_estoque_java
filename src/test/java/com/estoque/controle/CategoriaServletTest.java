package com.estoque.controle;

import com.estoque.TestUtils;
import com.estoque.dao.CategoriaDAO;
import com.estoque.modelo.Categoria;
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
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

public class CategoriaServletTest {

    private CategoriaServlet servlet;
    private CategoriaDAO mockDAO;
    private HttpServletRequest request;
    private HttpServletResponse response;
    private RequestDispatcher dispatcher;

    @BeforeEach
    void setUp() {
        servlet = new CategoriaServlet();
        mockDAO = mock(CategoriaDAO.class);
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        dispatcher = mock(RequestDispatcher.class);

        when(request.getRequestDispatcher(anyString())).thenReturn(dispatcher);
        when(request.getContextPath()).thenReturn("/controle-estoque");

        TestUtils.setField(servlet, "categoriaDAO", mockDAO);
    }

    @Test
    void testDoGetListarPadrao() throws ServletException, IOException, SQLException {
        when(request.getParameter("acao")).thenReturn(null);
        when(mockDAO.buscarTodos()).thenReturn(Collections.singletonList(new Categoria(1, "Test")));

        servlet.doGet(request, response);

        verify(request).setCharacterEncoding("UTF-8");
        verify(mockDAO).buscarTodos();
        verify(request).setAttribute(eq("categorias"), any());
        verify(request).getRequestDispatcher("/categoria/list.jsp");
        verify(dispatcher).forward(request, response);
    }

    @Test
    void testDoGetAcaoNovo() throws ServletException, IOException {
        when(request.getParameter("acao")).thenReturn("novo");

        servlet.doGet(request, response);

        verify(request).setAttribute(eq("categoria"), any(Categoria.class));
        verify(request).getRequestDispatcher("/categoria/form.jsp");
        verify(dispatcher).forward(request, response);
    }

    @Test
    void testDoGetEditarEncontrado() throws ServletException, IOException, SQLException {
        when(request.getParameter("acao")).thenReturn("editar");
        when(request.getParameter("id")).thenReturn("5");
        Categoria cat = new Categoria(5, "Alimentos");
        when(mockDAO.buscarPorId(5)).thenReturn(cat);

        servlet.doGet(request, response);

        verify(request).setAttribute("categoria", cat);
        verify(request).getRequestDispatcher("/categoria/form.jsp");
        verify(dispatcher).forward(request, response);
    }

    @Test
    void testDoGetEditarNaoEncontrado() throws ServletException, IOException, SQLException {
        when(request.getParameter("acao")).thenReturn("editar");
        when(request.getParameter("id")).thenReturn("99");
        when(mockDAO.buscarPorId(99)).thenReturn(null);

        servlet.doGet(request, response);

        verify(response).sendRedirect("/controle-estoque/servlet/categorias?msg=nao_encontrado");
    }

    @Test
    void testDoGetExcluirSucesso() throws ServletException, IOException, SQLException {
        when(request.getParameter("acao")).thenReturn("excluir");
        when(request.getParameter("id")).thenReturn("3");
        when(mockDAO.excluir(3)).thenReturn(true);

        servlet.doGet(request, response);

        verify(mockDAO).excluir(3);
        verify(response).sendRedirect("/controle-estoque/servlet/categorias?msg=sucesso_excluir");
    }

    @Test
    void testDoGetExcluirComErroVinculo() throws ServletException, IOException, SQLException {
        when(request.getParameter("acao")).thenReturn("excluir");
        when(request.getParameter("id")).thenReturn("3");
        doThrow(new RuntimeException("FK constraint error")).when(mockDAO).excluir(3);

        servlet.doGet(request, response);

        verify(mockDAO).excluir(3);
        verify(response).sendRedirect("/controle-estoque/servlet/categorias?msg=erro_excluir_vinculo");
    }

    @Test
    void testDoGetExceptionLancaServletException() throws SQLException {
        when(request.getParameter("acao")).thenReturn("listar");
        when(mockDAO.buscarTodos()).thenThrow(new SQLException("DB error"));

        ServletException ex = assertThrows(ServletException.class, () -> servlet.doGet(request, response));
        assertTrue(ex.getMessage().contains("Erro ao processar requisição em Categorias"));
    }

    @Test
    void testDoPostInserirSucesso() throws ServletException, IOException, SQLException {
        when(request.getParameter("categoriaId")).thenReturn(null);
        when(request.getParameter("nome")).thenReturn(" Informática ");

        servlet.doPost(request, response);

        verify(mockDAO).inserir(argThat(c -> "Informática".equals(c.getNome()) && c.getCategoriaId() == null));
        verify(response).sendRedirect("/controle-estoque/servlet/categorias?msg=sucesso_salvar");
    }

    @Test
    void testDoPostInserirNomeNull() throws ServletException, IOException, SQLException {
        when(request.getParameter("categoriaId")).thenReturn("");
        when(request.getParameter("nome")).thenReturn(null);

        servlet.doPost(request, response);

        verify(mockDAO).inserir(argThat(c -> "".equals(c.getNome()) && c.getCategoriaId() == null));
        verify(response).sendRedirect("/controle-estoque/servlet/categorias?msg=sucesso_salvar");
    }

    @Test
    void testDoPostAtualizarSucesso() throws ServletException, IOException, SQLException {
        when(request.getParameter("categoriaId")).thenReturn("8");
        when(request.getParameter("nome")).thenReturn("Eletrônicos");

        servlet.doPost(request, response);

        verify(mockDAO).atualizar(argThat(c -> "Eletrônicos".equals(c.getNome()) && Integer.valueOf(8).equals(c.getCategoriaId())));
        verify(response).sendRedirect("/controle-estoque/servlet/categorias?msg=sucesso_salvar");
    }

    @Test
    void testDoPostComErroComId() throws ServletException, IOException, SQLException {
        when(request.getParameter("categoriaId")).thenReturn("8");
        when(request.getParameter("nome")).thenReturn("Eletrônicos");
        doThrow(new SQLException("DB constraint violation")).when(mockDAO).atualizar(any());

        servlet.doPost(request, response);

        verify(request).setAttribute(eq("erro"), contains("Erro ao salvar categoria"));
        verify(request).setAttribute(eq("categoria"), argThat(c -> ((Categoria) c).getCategoriaId() == 8));
        verify(request).getRequestDispatcher("/categoria/form.jsp");
        verify(dispatcher).forward(request, response);
    }

    @Test
    void testDoPostComErroSemId() throws ServletException, IOException, SQLException {
        when(request.getParameter("categoriaId")).thenReturn("");
        when(request.getParameter("nome")).thenReturn("Nova Categoria");
        doThrow(new RuntimeException("DB error")).when(mockDAO).inserir(any());

        servlet.doPost(request, response);

        verify(request).setAttribute(eq("erro"), contains("Erro ao salvar categoria"));
        verify(request).setAttribute(eq("categoria"), argThat(c -> ((Categoria) c).getCategoriaId() == null));
        verify(request).getRequestDispatcher("/categoria/form.jsp");
        verify(dispatcher).forward(request, response);
    }
}
