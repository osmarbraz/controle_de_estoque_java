package com.estoque.controle;

import com.estoque.TestUtils;
import com.estoque.dao.CategoriaDAO;
import com.estoque.dao.ProdutoDAO;
import com.estoque.modelo.Categoria;
import com.estoque.modelo.Produto;
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

public class ProdutoServletTest {

    private ProdutoServlet servlet;
    private ProdutoDAO mockProdutoDAO;
    private CategoriaDAO mockCategoriaDAO;
    private HttpServletRequest request;
    private HttpServletResponse response;
    private RequestDispatcher dispatcher;

    @BeforeEach
    void setUp() {
        servlet = new ProdutoServlet();
        mockProdutoDAO = mock(ProdutoDAO.class);
        mockCategoriaDAO = mock(CategoriaDAO.class);
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        dispatcher = mock(RequestDispatcher.class);

        when(request.getRequestDispatcher(anyString())).thenReturn(dispatcher);
        when(request.getContextPath()).thenReturn("/controle-estoque");

        TestUtils.setField(servlet, "produtoDAO", mockProdutoDAO);
        TestUtils.setField(servlet, "categoriaDAO", mockCategoriaDAO);
    }

    @Test
    void testDoGetListarPadrao() throws ServletException, IOException, SQLException {
        when(request.getParameter("acao")).thenReturn(null);
        when(mockProdutoDAO.buscarTodos()).thenReturn(Collections.emptyList());

        servlet.doGet(request, response);

        verify(request).setCharacterEncoding("UTF-8");
        verify(mockProdutoDAO).buscarTodos();
        verify(request).setAttribute(eq("produtos"), any());
        verify(request).getRequestDispatcher("/produto/list.jsp");
        verify(dispatcher).forward(request, response);
    }

    @Test
    void testDoGetNovo() throws ServletException, IOException, SQLException {
        when(request.getParameter("acao")).thenReturn("novo");
        when(mockCategoriaDAO.buscarTodos()).thenReturn(Collections.singletonList(new Categoria(1, "Cat")));

        servlet.doGet(request, response);

        verify(request).setAttribute(eq("produto"), any(Produto.class));
        verify(request).setAttribute(eq("categorias"), any());
        verify(request).getRequestDispatcher("/produto/form.jsp");
        verify(dispatcher).forward(request, response);
    }

    @Test
    void testDoGetEditarEncontrado() throws ServletException, IOException, SQLException {
        when(request.getParameter("acao")).thenReturn("editar");
        when(request.getParameter("id")).thenReturn("5");
        Produto p = new Produto();
        p.setProdutoId(5);
        when(mockProdutoDAO.buscarPorId(5)).thenReturn(p);
        when(mockCategoriaDAO.buscarTodos()).thenReturn(Collections.emptyList());

        servlet.doGet(request, response);

        verify(request).setAttribute("produto", p);
        verify(request).setAttribute(eq("categorias"), any());
        verify(request).getRequestDispatcher("/produto/form.jsp");
        verify(dispatcher).forward(request, response);
    }

    @Test
    void testDoGetEditarNaoEncontrado() throws ServletException, IOException, SQLException {
        when(request.getParameter("acao")).thenReturn("editar");
        when(request.getParameter("id")).thenReturn("99");
        when(mockProdutoDAO.buscarPorId(99)).thenReturn(null);

        servlet.doGet(request, response);

        verify(response).sendRedirect("/controle-estoque/servlet/produtos?msg=nao_encontrado");
    }

    @Test
    void testDoGetExcluirSucesso() throws ServletException, IOException, SQLException {
        when(request.getParameter("acao")).thenReturn("excluir");
        when(request.getParameter("id")).thenReturn("4");
        when(mockProdutoDAO.excluir(4)).thenReturn(true);

        servlet.doGet(request, response);

        verify(mockProdutoDAO).excluir(4);
        verify(response).sendRedirect("/controle-estoque/servlet/produtos?msg=sucesso_excluir");
    }

    @Test
    void testDoGetExcluirComErroVinculo() throws ServletException, IOException, SQLException {
        when(request.getParameter("acao")).thenReturn("excluir");
        when(request.getParameter("id")).thenReturn("4");
        doThrow(new RuntimeException("FK Error")).when(mockProdutoDAO).excluir(4);

        servlet.doGet(request, response);

        verify(mockProdutoDAO).excluir(4);
        verify(response).sendRedirect("/controle-estoque/servlet/produtos?msg=erro_excluir_vinculo");
    }

    @Test
    void testDoGetExceptionLancaServletException() throws SQLException {
        when(request.getParameter("acao")).thenReturn("listar");
        when(mockProdutoDAO.buscarTodos()).thenThrow(new SQLException("DB error"));

        ServletException ex = assertThrows(ServletException.class, () -> servlet.doGet(request, response));
        assertTrue(ex.getMessage().contains("Erro ao processar requisição em Produtos"));
    }

    @Test
    void testDoPostInserirSucessoComVirgulaNoPreco() throws ServletException, IOException, SQLException {
        when(request.getParameter("produtoId")).thenReturn(null);
        when(request.getParameter("descricao")).thenReturn(" Mouse Óptico ");
        when(request.getParameter("unidade")).thenReturn(" UN ");
        when(request.getParameter("precoCusto")).thenReturn("25,50");
        when(request.getParameter("precoVenda")).thenReturn("49,90");
        when(request.getParameter("quantidade")).thenReturn("10,5");
        when(request.getParameter("categoriaId")).thenReturn("2");

        servlet.doPost(request, response);

        verify(mockProdutoDAO).inserir(argThat(p -> "Mouse Óptico".equals(p.getDescricao())
                && "UN".equals(p.getUnidade())
                && Math.abs(p.getPrecoCusto() - 25.50) < 0.001
                && Math.abs(p.getPrecoVenda() - 49.90) < 0.001
                && Math.abs(p.getQuantidade() - 10.5) < 0.001
                && Integer.valueOf(2).equals(p.getCategoriaId())
                && p.getProdutoId() == null));
        verify(response).sendRedirect("/controle-estoque/servlet/produtos?msg=sucesso_salvar");
    }

    @Test
    void testDoPostInserirCamposNullEPadrao() throws ServletException, IOException, SQLException {
        when(request.getParameter("produtoId")).thenReturn("");
        when(request.getParameter("descricao")).thenReturn(null);
        when(request.getParameter("unidade")).thenReturn(null);
        when(request.getParameter("precoCusto")).thenReturn(null);
        when(request.getParameter("precoVenda")).thenReturn("");
        when(request.getParameter("quantidade")).thenReturn(null);
        when(request.getParameter("categoriaId")).thenReturn("");

        servlet.doPost(request, response);

        verify(mockProdutoDAO).inserir(argThat(p -> "".equals(p.getDescricao())
                && "UN".equals(p.getUnidade())
                && p.getPrecoCusto() == 0.0
                && p.getPrecoVenda() == 0.0
                && p.getQuantidade() == 0.0
                && p.getCategoriaId() == null));
        verify(response).sendRedirect("/controle-estoque/servlet/produtos?msg=sucesso_salvar");
    }

    @Test
    void testDoPostAtualizarSucesso() throws ServletException, IOException, SQLException {
        when(request.getParameter("produtoId")).thenReturn("8");
        when(request.getParameter("descricao")).thenReturn("Monitor");
        when(request.getParameter("unidade")).thenReturn("UN");
        when(request.getParameter("precoCusto")).thenReturn("300.00");
        when(request.getParameter("precoVenda")).thenReturn("550.00");
        when(request.getParameter("quantidade")).thenReturn("12");
        when(request.getParameter("categoriaId")).thenReturn("3");

        servlet.doPost(request, response);

        verify(mockProdutoDAO).atualizar(argThat(p -> Integer.valueOf(8).equals(p.getProdutoId())));
        verify(response).sendRedirect("/controle-estoque/servlet/produtos?msg=sucesso_salvar");
    }

    @Test
    void testDoPostComErroComIdECategoriaId() throws ServletException, IOException, SQLException {
        when(request.getParameter("produtoId")).thenReturn("8");
        when(request.getParameter("descricao")).thenReturn("Monitor Erro");
        when(request.getParameter("unidade")).thenReturn("UN");
        when(request.getParameter("categoriaId")).thenReturn("3");
        doThrow(new SQLException("DB error")).when(mockProdutoDAO).atualizar(any());
        when(mockCategoriaDAO.buscarTodos()).thenReturn(Collections.emptyList());

        servlet.doPost(request, response);

        verify(request).setAttribute(eq("erro"), contains("Erro ao salvar produto"));
        verify(request).setAttribute(eq("produto"), argThat(p -> ((Produto) p).getProdutoId() == 8 && ((Produto) p).getCategoriaId() == 3));
        verify(request).getRequestDispatcher("/produto/form.jsp");
        verify(dispatcher).forward(request, response);
    }

    @Test
    void testDoPostComErroSemIdESemCategoriaIdComFalhaEmBuscarCategorias() throws ServletException, IOException, SQLException {
        when(request.getParameter("produtoId")).thenReturn("");
        when(request.getParameter("descricao")).thenReturn("Teclado Erro");
        when(request.getParameter("unidade")).thenReturn("UN");
        when(request.getParameter("categoriaId")).thenReturn(null);
        doThrow(new RuntimeException("DB error")).when(mockProdutoDAO).inserir(any());
        when(mockCategoriaDAO.buscarTodos()).thenThrow(new SQLException("Falha em categorias"));

        servlet.doPost(request, response);

        verify(request).setAttribute(eq("erro"), contains("Erro ao salvar produto"));
        verify(request).setAttribute(eq("produto"), argThat(p -> ((Produto) p).getProdutoId() == null && ((Produto) p).getCategoriaId() == null));
        verify(request).getRequestDispatcher("/produto/form.jsp");
        verify(dispatcher).forward(request, response);
    }
}
