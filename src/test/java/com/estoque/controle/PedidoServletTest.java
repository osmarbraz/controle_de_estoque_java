package com.estoque.controle;

import com.estoque.TestUtils;
import com.estoque.dao.ClienteDAO;
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
import static org.mockito.ArgumentMatchers.contains;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

public class PedidoServletTest {

    private PedidoServlet servlet;
    private PedidoDAO mockPedidoDAO;
    private ClienteDAO mockClienteDAO;
    private ProdutoDAO mockProdutoDAO;
    private HttpServletRequest request;
    private HttpServletResponse response;
    private RequestDispatcher dispatcher;

    @BeforeEach
    void setUp() {
        servlet = new PedidoServlet();
        mockPedidoDAO = mock(PedidoDAO.class);
        mockClienteDAO = mock(ClienteDAO.class);
        mockProdutoDAO = mock(ProdutoDAO.class);
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        dispatcher = mock(RequestDispatcher.class);

        when(request.getRequestDispatcher(anyString())).thenReturn(dispatcher);
        when(request.getContextPath()).thenReturn("/controle-estoque");

        TestUtils.setField(servlet, "pedidoDAO", mockPedidoDAO);
        TestUtils.setField(servlet, "clienteDAO", mockClienteDAO);
        TestUtils.setField(servlet, "produtoDAO", mockProdutoDAO);
    }

    @Test
    void testDoGetListarPadrao() throws ServletException, IOException, SQLException {
        when(request.getParameter("acao")).thenReturn(null);
        when(mockPedidoDAO.buscarTodos()).thenReturn(Collections.emptyList());

        servlet.doGet(request, response);

        verify(request).setCharacterEncoding("UTF-8");
        verify(mockPedidoDAO).buscarTodos();
        verify(request).setAttribute(eq("pedidos"), any());
        verify(request).getRequestDispatcher("/pedido/list.jsp");
        verify(dispatcher).forward(request, response);
    }

    @Test
    void testDoGetNovo() throws ServletException, IOException, SQLException {
        when(request.getParameter("acao")).thenReturn("novo");
        when(mockClienteDAO.buscarTodos()).thenReturn(Collections.emptyList());

        servlet.doGet(request, response);

        verify(request).setAttribute(eq("pedido"), any(Pedido.class));
        verify(request).setAttribute(eq("clientes"), any());
        verify(request).getRequestDispatcher("/pedido/form.jsp");
        verify(dispatcher).forward(request, response);
    }

    @Test
    void testDoGetVisualizarEncontrado() throws ServletException, IOException, SQLException {
        when(request.getParameter("acao")).thenReturn("visualizar");
        when(request.getParameter("id")).thenReturn("5");
        Pedido p = new Pedido();
        p.setPedidoId(5);
        when(mockPedidoDAO.buscarPorId(5)).thenReturn(p);

        servlet.doGet(request, response);

        verify(request).setAttribute("pedido", p);
        verify(request).getRequestDispatcher("/pedido/view.jsp");
        verify(dispatcher).forward(request, response);
    }

    @Test
    void testDoGetVisualizarNaoEncontrado() throws ServletException, IOException, SQLException {
        when(request.getParameter("acao")).thenReturn("visualizar");
        when(request.getParameter("id")).thenReturn("99");
        when(mockPedidoDAO.buscarPorId(99)).thenReturn(null);

        servlet.doGet(request, response);

        verify(response).sendRedirect("/controle-estoque/servlet/pedidos?msg=nao_encontrado");
    }

    @Test
    void testDoGetStatusFechadoComItens() throws ServletException, IOException, SQLException {
        when(request.getParameter("acao")).thenReturn("status");
        when(request.getParameter("id")).thenReturn("10");
        when(request.getParameter("situacao")).thenReturn("fechado");

        Pedido p = new Pedido();
        p.setPedidoId(10);
        p.setSituacao("aberto");
        ItemPedido item = new ItemPedido(10, 2, 3.0, 10.0);
        p.setItens(Collections.singletonList(item));

        when(mockPedidoDAO.buscarPorId(10)).thenReturn(p);

        servlet.doGet(request, response);

        verify(mockProdutoDAO).atualizarEstoque(2, -3.0);
        verify(mockPedidoDAO).atualizarSituacao(10, "fechado");
        verify(response).sendRedirect("/controle-estoque/servlet/pedidos?msg=situacao_atualizada");
    }

    @Test
    void testDoGetStatusAbertoComItens() throws ServletException, IOException, SQLException {
        when(request.getParameter("acao")).thenReturn("status");
        when(request.getParameter("id")).thenReturn("10");
        when(request.getParameter("situacao")).thenReturn("aberto");

        Pedido p = new Pedido();
        p.setPedidoId(10);
        p.setSituacao("fechado");
        ItemPedido item = new ItemPedido(10, 2, 3.0, 10.0);
        p.setItens(Collections.singletonList(item));

        when(mockPedidoDAO.buscarPorId(10)).thenReturn(p);

        servlet.doGet(request, response);

        verify(mockProdutoDAO).atualizarEstoque(2, 3.0);
        verify(mockPedidoDAO).atualizarSituacao(10, "aberto");
        verify(response).sendRedirect("/controle-estoque/servlet/pedidos?msg=situacao_atualizada");
    }

    @Test
    void testDoGetStatusOutraSituacaoOuItensNull() throws ServletException, IOException, SQLException {
        when(request.getParameter("acao")).thenReturn("status");
        when(request.getParameter("id")).thenReturn("10");
        when(request.getParameter("situacao")).thenReturn("cancelado");

        Pedido p = new Pedido();
        p.setPedidoId(10);
        p.setSituacao("aberto");
        p.setItens(null);

        when(mockPedidoDAO.buscarPorId(10)).thenReturn(p);

        servlet.doGet(request, response);

        verify(mockProdutoDAO, never()).atualizarEstoque(anyInt(), anyDouble());
        verify(mockPedidoDAO).atualizarSituacao(10, "cancelado");
        verify(response).sendRedirect("/controle-estoque/servlet/pedidos?msg=situacao_atualizada");
    }

    @Test
    void testDoGetStatusMesmaSituacaoOuPedidoNull() throws ServletException, IOException, SQLException {
        when(request.getParameter("acao")).thenReturn("status");
        when(request.getParameter("id")).thenReturn("10");
        when(request.getParameter("situacao")).thenReturn("aberto");

        Pedido p = new Pedido();
        p.setPedidoId(10);
        p.setSituacao("aberto");

        when(mockPedidoDAO.buscarPorId(10)).thenReturn(p);

        servlet.doGet(request, response);

        verify(mockPedidoDAO, never()).atualizarSituacao(anyInt(), anyString());
        verify(response).sendRedirect("/controle-estoque/servlet/pedidos?msg=situacao_atualizada");

        // Status null
        when(request.getParameter("situacao")).thenReturn(null);
        servlet.doGet(request, response);
        verify(mockPedidoDAO, never()).atualizarSituacao(anyInt(), anyString());
    }

    @Test
    void testDoGetExcluirSucesso() throws ServletException, IOException, SQLException {
        when(request.getParameter("acao")).thenReturn("excluir");
        when(request.getParameter("id")).thenReturn("8");
        when(mockPedidoDAO.excluir(8)).thenReturn(true);

        servlet.doGet(request, response);

        verify(mockPedidoDAO).excluir(8);
        verify(response).sendRedirect("/controle-estoque/servlet/pedidos?msg=sucesso_excluir");
    }

    @Test
    void testDoGetExcluirComErro() throws ServletException, IOException, SQLException {
        when(request.getParameter("acao")).thenReturn("excluir");
        when(request.getParameter("id")).thenReturn("8");
        doThrow(new RuntimeException("DB error")).when(mockPedidoDAO).excluir(8);

        servlet.doGet(request, response);

        verify(mockPedidoDAO).excluir(8);
        verify(response).sendRedirect("/controle-estoque/servlet/pedidos?msg=erro_excluir");
    }

    @Test
    void testDoGetExceptionLancaServletException() throws SQLException {
        when(request.getParameter("acao")).thenReturn("listar");
        when(mockPedidoDAO.buscarTodos()).thenThrow(new SQLException("DB error"));

        ServletException ex = assertThrows(ServletException.class, () -> servlet.doGet(request, response));
        assertTrue(ex.getMessage().contains("Erro ao processar requisição em Pedidos"));
    }

    @Test
    void testDoPostInserirSucessoComDataFormatada() throws ServletException, IOException, SQLException {
        when(request.getParameter("pedidoId")).thenReturn(null);
        when(request.getParameter("clienteId")).thenReturn("2");
        when(request.getParameter("data")).thenReturn("2026-05-10");
        when(request.getParameter("situacao")).thenReturn("aberto");

        doAnswer(invocation -> {
            Pedido ped = invocation.getArgument(0);
            ped.setPedidoId(15);
            return true;
        }).when(mockPedidoDAO).inserir(any(Pedido.class));

        servlet.doPost(request, response);

        verify(mockPedidoDAO).inserir(
                argThat(p -> p.getClienteId() == 2 && "aberto".equals(p.getSituacao()) && p.getTotal() == 0.0));
        verify(response).sendRedirect("/controle-estoque/servlet/item-pedido?pedidoId=15&msg=pedido_criado");
    }

    @Test
    void testDoPostInserirSemDataESituacaoVazia() throws ServletException, IOException, SQLException {
        when(request.getParameter("pedidoId")).thenReturn("");
        when(request.getParameter("clienteId")).thenReturn(null);
        when(request.getParameter("data")).thenReturn("");
        when(request.getParameter("situacao")).thenReturn("");

        doAnswer(invocation -> {
            Pedido ped = invocation.getArgument(0);
            ped.setPedidoId(20);
            return true;
        }).when(mockPedidoDAO).inserir(any(Pedido.class));

        servlet.doPost(request, response);

        verify(mockPedidoDAO).inserir(argThat(p -> p.getClienteId() == null && "aberto".equals(p.getSituacao())));
        verify(response).sendRedirect("/controle-estoque/servlet/item-pedido?pedidoId=20&msg=pedido_criado");
    }

    @Test
    void testDoPostAtualizarCabecalhoSucesso() throws ServletException, IOException, SQLException {
        when(request.getParameter("pedidoId")).thenReturn("15");
        when(request.getParameter("clienteId")).thenReturn("4");
        when(request.getParameter("data")).thenReturn("2026-06-01");
        when(request.getParameter("situacao")).thenReturn("fechado");

        servlet.doPost(request, response);

        verify(mockPedidoDAO).atualizarCabecalho(
                argThat(p -> p.getPedidoId() == 15 && p.getClienteId() == 4 && "fechado".equals(p.getSituacao())));
        verify(response).sendRedirect("/controle-estoque/servlet/item-pedido?pedidoId=15&msg=pedido_atualizado");
    }

    @Test
    void testDoPostComErroComIdEClienteId() throws ServletException, IOException, SQLException {
        when(request.getParameter("pedidoId")).thenReturn("15");
        when(request.getParameter("clienteId")).thenReturn("4");
        when(request.getParameter("data")).thenReturn("data_invalida_trigger_exception");
        when(request.getParameter("situacao")).thenReturn("aberto");
        when(mockClienteDAO.buscarTodos()).thenReturn(Collections.emptyList());

        servlet.doPost(request, response);

        verify(request).setAttribute(eq("erro"), contains("Erro ao salvar cabeçalho do pedido"));
        verify(request).setAttribute(eq("pedido"),
                argThat(p -> ((Pedido) p).getPedidoId() == 15 && ((Pedido) p).getClienteId() == 4));
        verify(request).getRequestDispatcher("/pedido/form.jsp");
        verify(dispatcher).forward(request, response);
    }

    @Test
    void testDoPostComErroSemIdESemClienteIdComFalhaEmBuscarClientes()
            throws ServletException, IOException, SQLException {
        when(request.getParameter("pedidoId")).thenReturn("");
        when(request.getParameter("clienteId")).thenReturn("");
        when(request.getParameter("data")).thenReturn("data_invalida");
        when(mockClienteDAO.buscarTodos()).thenThrow(new SQLException("Falha em clientes"));

        servlet.doPost(request, response);

        verify(request).setAttribute(eq("erro"), contains("Erro ao salvar cabeçalho do pedido"));
        verify(request).setAttribute(eq("pedido"),
                argThat(p -> ((Pedido) p).getPedidoId() == null && ((Pedido) p).getClienteId() == null));
        verify(request).getRequestDispatcher("/pedido/form.jsp");
        verify(dispatcher).forward(request, response);
    }
}
