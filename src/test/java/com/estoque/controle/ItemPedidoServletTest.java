package com.estoque.controle;

import com.estoque.TestUtils;
import com.estoque.dao.ItemPedidoDAO;
import com.estoque.dao.PedidoDAO;
import com.estoque.dao.ProdutoDAO;
import com.estoque.modelo.ItemPedido;
import com.estoque.modelo.Pedido;
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
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

public class ItemPedidoServletTest {

    private ItemPedidoServlet servlet;
    private PedidoDAO mockPedidoDAO;
    private ItemPedidoDAO mockItemPedidoDAO;
    private ProdutoDAO mockProdutoDAO;
    private HttpServletRequest request;
    private HttpServletResponse response;
    private RequestDispatcher dispatcher;

    @BeforeEach
    void setUp() {
        servlet = new ItemPedidoServlet();
        mockPedidoDAO = mock(PedidoDAO.class);
        mockItemPedidoDAO = mock(ItemPedidoDAO.class);
        mockProdutoDAO = mock(ProdutoDAO.class);
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        dispatcher = mock(RequestDispatcher.class);

        when(request.getRequestDispatcher(anyString())).thenReturn(dispatcher);
        when(request.getContextPath()).thenReturn("/controle-estoque");

        TestUtils.setField(servlet, "pedidoDAO", mockPedidoDAO);
        TestUtils.setField(servlet, "itemPedidoDAO", mockItemPedidoDAO);
        TestUtils.setField(servlet, "produtoDAO", mockProdutoDAO);
    }

    @Test
    void testDoGetPedidoIdInvalido() throws ServletException, IOException {
        when(request.getParameter("pedidoId")).thenReturn(null);

        servlet.doGet(request, response);

        verify(response).sendRedirect("/controle-estoque/servlet/pedidos?msg=pedido_invalido");

        when(request.getParameter("pedidoId")).thenReturn("");
        servlet.doGet(request, response);
        verify(response, times(2)).sendRedirect("/controle-estoque/servlet/pedidos?msg=pedido_invalido");
    }

    @Test
    void testDoGetExcluirItemExistente() throws ServletException, IOException, SQLException {
        when(request.getParameter("pedidoId")).thenReturn("10");
        when(request.getParameter("acao")).thenReturn("excluir");
        when(request.getParameter("produtoId")).thenReturn("5");

        ItemPedido item = new ItemPedido(10, 5, 2.0, 50.0);
        when(mockItemPedidoDAO.findByPedidoAndProduto(10, 5)).thenReturn(item);

        servlet.doGet(request, response);

        verify(mockProdutoDAO).atualizarEstoque(5, 2.0);
        verify(mockItemPedidoDAO).excluir(10, 5);
        verify(mockPedidoDAO).recalcularTotal(10);
        verify(response).sendRedirect("/controle-estoque/servlet/item-pedido?pedidoId=10&msg=item_removido");
    }

    @Test
    void testDoGetExcluirItemInexistente() throws ServletException, IOException, SQLException {
        when(request.getParameter("pedidoId")).thenReturn("10");
        when(request.getParameter("acao")).thenReturn("excluir");
        when(request.getParameter("produtoId")).thenReturn("5");

        when(mockItemPedidoDAO.findByPedidoAndProduto(10, 5)).thenReturn(null);

        servlet.doGet(request, response);

        verify(mockProdutoDAO, never()).atualizarEstoque(anyInt(), anyDouble());
        verify(mockItemPedidoDAO, never()).excluir(anyInt(), anyInt());
        verify(response).sendRedirect("/controle-estoque/servlet/item-pedido?pedidoId=10&msg=item_removido");
    }

    @Test
    void testDoGetCarregarPedidoNaoEncontrado() throws ServletException, IOException, SQLException {
        when(request.getParameter("pedidoId")).thenReturn("10");
        when(request.getParameter("acao")).thenReturn(null);
        when(mockPedidoDAO.buscarPorId(10)).thenReturn(null);

        servlet.doGet(request, response);

        verify(response).sendRedirect("/controle-estoque/servlet/pedidos?msg=nao_encontrado");
    }

    @Test
    void testDoGetCarregarPedidoSucesso() throws ServletException, IOException, SQLException {
        when(request.getParameter("pedidoId")).thenReturn("10");
        when(request.getParameter("acao")).thenReturn(null);
        Pedido pedido = new Pedido();
        pedido.setPedidoId(10);
        when(mockPedidoDAO.buscarPorId(10)).thenReturn(pedido);
        when(mockProdutoDAO.buscarTodos()).thenReturn(Collections.emptyList());

        servlet.doGet(request, response);

        verify(request).setAttribute("pedido", pedido);
        verify(request).setAttribute(eq("produtos"), any());
        verify(request).getRequestDispatcher("/pedido/items.jsp");
        verify(dispatcher).forward(request, response);
    }

    @Test
    void testDoGetExceptionLancaServletException() throws SQLException {
        when(request.getParameter("pedidoId")).thenReturn("10");
        when(mockPedidoDAO.buscarPorId(10)).thenThrow(new SQLException("DB error"));

        ServletException ex = assertThrows(ServletException.class, () -> servlet.doGet(request, response));
        assertTrue(ex.getMessage().contains("Erro ao carregar itens do pedido"));
    }

    @Test
    void testDoPostPedidoIdInvalido() throws ServletException, IOException {
        when(request.getParameter("pedidoId")).thenReturn(null);

        servlet.doPost(request, response);

        verify(response).sendRedirect("/controle-estoque/servlet/pedidos?msg=pedido_invalido");

        when(request.getParameter("pedidoId")).thenReturn("");
        servlet.doPost(request, response);
        verify(response, times(2)).sendRedirect("/controle-estoque/servlet/pedidos?msg=pedido_invalido");
    }

    @Test
    void testDoPostPedidoNaoEncontrado() throws ServletException, IOException, SQLException {
        when(request.getParameter("pedidoId")).thenReturn("12");
        when(mockPedidoDAO.buscarPorId(12)).thenReturn(null);

        servlet.doPost(request, response);

        verify(response).sendRedirect("/controle-estoque/servlet/pedidos?msg=nao_encontrado");
    }

    @Test
    void testDoPostPedidoFechado() throws ServletException, IOException, SQLException {
        when(request.getParameter("pedidoId")).thenReturn("12");
        Pedido p = new Pedido();
        p.setPedidoId(12);
        p.setSituacao("FECHADO");
        when(mockPedidoDAO.buscarPorId(12)).thenReturn(p);

        servlet.doPost(request, response);

        verify(response).sendRedirect("/controle-estoque/servlet/item-pedido?pedidoId=12&msg=erro_pedido_fechado");
    }

    @Test
    void testDoPostQuantidadeInvalidaZeroOuNegativa() throws ServletException, IOException, SQLException {
        when(request.getParameter("pedidoId")).thenReturn("12");
        when(request.getParameter("produtoId")).thenReturn("3");
        when(request.getParameter("quantidade")).thenReturn("0,0");
        when(request.getParameter("precoVenda")).thenReturn("10,0");

        Pedido p = new Pedido();
        p.setPedidoId(12);
        p.setSituacao("aberto");
        when(mockPedidoDAO.buscarPorId(12)).thenReturn(p);

        servlet.doPost(request, response);

        verify(response).sendRedirect("/controle-estoque/servlet/item-pedido?pedidoId=12&msg=erro_qtd_invalida");
    }

    @Test
    void testDoPostItemNovoSucesso() throws ServletException, IOException, SQLException {
        when(request.getParameter("pedidoId")).thenReturn("12");
        when(request.getParameter("produtoId")).thenReturn("3");
        when(request.getParameter("quantidade")).thenReturn("2,5");
        when(request.getParameter("precoVenda")).thenReturn("20,0");

        Pedido p = new Pedido();
        p.setPedidoId(12);
        p.setSituacao("aberto");
        when(mockPedidoDAO.buscarPorId(12)).thenReturn(p);
        when(mockItemPedidoDAO.findByPedidoAndProduto(12, 3)).thenReturn(null);

        servlet.doPost(request, response);

        verify(mockItemPedidoDAO).inserir(argThat(i -> i.getPedidoId() == 12 && i.getProdutoId() == 3 && i.getQuantidade() == 2.5));
        verify(mockProdutoDAO).atualizarEstoque(3, -2.5);
        verify(mockPedidoDAO).recalcularTotal(12);
        verify(response).sendRedirect("/controle-estoque/servlet/item-pedido?pedidoId=12&msg=item_adicionado");
    }

    @Test
    void testDoPostItemExistenteSucesso() throws ServletException, IOException, SQLException {
        when(request.getParameter("pedidoId")).thenReturn("12");
        when(request.getParameter("produtoId")).thenReturn("3");
        when(request.getParameter("quantidade")).thenReturn("5.0");
        when(request.getParameter("precoVenda")).thenReturn("20.0");

        Pedido p = new Pedido();
        p.setPedidoId(12);
        p.setSituacao("aberto");
        when(mockPedidoDAO.buscarPorId(12)).thenReturn(p);

        ItemPedido existente = new ItemPedido(12, 3, 2.0, 20.0);
        when(mockItemPedidoDAO.findByPedidoAndProduto(12, 3)).thenReturn(existente);

        servlet.doPost(request, response);

        // deltaEstoque = 2.0 - 5.0 = -3.0
        verify(mockProdutoDAO).atualizarEstoque(3, -3.0);
        verify(mockItemPedidoDAO).inserir(any(ItemPedido.class));
        verify(mockPedidoDAO).recalcularTotal(12);
        verify(response).sendRedirect("/controle-estoque/servlet/item-pedido?pedidoId=12&msg=item_adicionado");
    }

    @Test
    void testDoPostException() throws ServletException, IOException, SQLException {
        when(request.getParameter("pedidoId")).thenReturn("12");
        when(mockPedidoDAO.buscarPorId(12)).thenThrow(new SQLException("DB error"));

        servlet.doPost(request, response);

        verify(response).sendRedirect("/controle-estoque/servlet/item-pedido?pedidoId=12&msg=erro_adicionar_item");
    }
}
